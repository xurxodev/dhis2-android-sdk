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

package org.hisp.dhis.android.core.program.internal;

import org.hisp.dhis.android.core.arch.api.executors.internal.APIDownloader;
import org.hisp.dhis.android.core.arch.call.factories.internal.UidsCall;
import org.hisp.dhis.android.core.arch.handlers.internal.Handler;
import org.hisp.dhis.android.core.common.ObjectWithUid;
import org.hisp.dhis.android.core.common.internal.DataAccessFields;
import org.hisp.dhis.android.core.program.ProgramStage;
import org.hisp.dhis.android.core.program.ProgramStageDataElement;
import org.hisp.dhis.android.core.program.ProgramStageInternalAccessor;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import dagger.Reusable;
import io.reactivex.Single;

@Reusable
public final class ProgramStageCall implements UidsCall<ProgramStage> {

    private static final int MAX_UID_LIST_SIZE = 64;

    private final ProgramStageService service;
    private final Handler<ProgramStage> handler;
    private final APIDownloader apiDownloader;

    @Inject
    ProgramStageCall(ProgramStageService service,
                     Handler<ProgramStage> handler,
                     APIDownloader apiDownloader) {
        this.service = service;
        this.handler = handler;
        this.apiDownloader = apiDownloader;
    }

    @Override
    public Single<List<ProgramStage>> download(Set<String> uids) {
        return apiDownloader.downloadPartitioned(uids, MAX_UID_LIST_SIZE, handler, partitionUids -> {
            String accessDataReadFilter = "access.data." + DataAccessFields.read.eq(true).generateString();
            String programUidsFilterStr = "program." + ObjectWithUid.uid.in(partitionUids).generateString();
            return service.getProgramStages(
                    ProgramStageFields.allFields,
                    programUidsFilterStr,
                    accessDataReadFilter,
                    Boolean.FALSE);
        }, this::transform);
    }

    private ProgramStage transform(ProgramStage stage) {
        if (ProgramStageInternalAccessor.accessProgramStageDataElements(stage) == null) {
            return stage;
        } else {
            List<ProgramStageDataElement> psdes = new ArrayList<>();
            for (ProgramStageDataElement psde : ProgramStageInternalAccessor.accessProgramStageDataElements(stage)) {
                if (psde.dataElement() != null) {
                    psdes.add(psde);
                }
            }
            return ProgramStageInternalAccessor.insertProgramStageDataElements(stage.toBuilder(), psdes).build();
        }
    }
}