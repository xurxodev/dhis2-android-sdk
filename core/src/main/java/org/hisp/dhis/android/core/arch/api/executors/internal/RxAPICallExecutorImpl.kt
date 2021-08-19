/*
 *  Copyright (c) 2004-2021, University of Oslo
 *  All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *  Redistributions of source code must retain the above copyright notice, this
 *  list of conditions and the following disclaimer.
 *
 *  Redistributions in binary form must reproduce the above copyright notice,
 *  this list of conditions and the following disclaimer in the documentation
 *  and/or other materials provided with the distribution.
 *  Neither the name of the HISP project nor the names of its contributors may
 *  be used to endorse or promote products derived from this software without
 *  specific prior written permission.
 *
 *  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 *  ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 *  WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 *  DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 *  ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 *  (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 *  LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 *  ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 *  (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 *  SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.hisp.dhis.android.core.arch.api.executors.internal

import dagger.Reusable
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject
import org.hisp.dhis.android.core.arch.db.access.DatabaseAdapter
import org.hisp.dhis.android.core.arch.db.access.Transaction
import org.hisp.dhis.android.core.arch.db.stores.internal.ObjectStore
import org.hisp.dhis.android.core.maintenance.D2Error
import org.hisp.dhis.android.core.maintenance.internal.ForeignKeyCleaner

@Reusable
internal class RxAPICallExecutorImpl @Inject constructor(
    private val databaseAdapter: DatabaseAdapter,
    private val errorStore: ObjectStore<D2Error>,
    private val errorMapper: APIErrorMapper,
    private val foreignKeyCleaner: ForeignKeyCleaner
) : RxAPICallExecutor {

    override fun <P> wrapSingle(single: Single<P>, storeError: Boolean): Single<P> {
        return single.onErrorResumeNext { throwable: Throwable -> Single.error(mapAndStore(throwable, storeError)) }
    }

    override fun <P> wrapObservableTransactionally(
        observable: Observable<P>,
        cleanForeignKeys: Boolean
    ): Observable<P> {
        return Observable.fromCallable { databaseAdapter.beginNewTransaction() }
            .flatMap { transaction: Transaction ->
                observable
                    .doOnComplete { finishTransaction(transaction, cleanForeignKeys) }
                    .onErrorResumeNext { throwable: Throwable ->
                        transaction.end()
                        Observable.error(mapAndStore(throwable, true))
                    }
            }
    }

    override fun wrapCompletableTransactionally(completable: Completable, cleanForeignKeys: Boolean): Completable {
        return Single.fromCallable { databaseAdapter.beginNewTransaction() }
            .flatMapCompletable { transaction: Transaction ->
                completable
                    .doOnComplete { finishTransaction(transaction, cleanForeignKeys) }
                    .onErrorResumeNext { throwable: Throwable ->
                        transaction.end()
                        Completable.error(mapAndStore(throwable, true))
                    }
            }
    }

    private fun finishTransaction(t: Transaction, cleanForeignKeys: Boolean) {
        if (cleanForeignKeys) {
            foreignKeyCleaner.cleanForeignKeyErrors()
        }
        t.setSuccessful()
        t.end()
    }

    private fun mapAndStore(throwable: Throwable, storeError: Boolean): D2Error {
        val d2Error =
            if (throwable is D2Error) throwable
            else errorMapper.mapRetrofitException(throwable, errorMapper.rxObjectErrorBuilder)
        if (storeError) {
            errorStore.insert(d2Error)
        }
        return d2Error
    }
}
