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
package org.hisp.dhis.android.core.visualization.internal

import dagger.Reusable
import javax.inject.Inject
import org.hisp.dhis.android.core.arch.cleaners.internal.CollectionCleaner
import org.hisp.dhis.android.core.arch.db.stores.internal.IdentifiableObjectStore
import org.hisp.dhis.android.core.arch.db.stores.internal.LinkStore
import org.hisp.dhis.android.core.arch.handlers.internal.HandleAction
import org.hisp.dhis.android.core.arch.handlers.internal.IdentifiableHandlerImpl
import org.hisp.dhis.android.core.arch.handlers.internal.LinkHandler
import org.hisp.dhis.android.core.common.ObjectWithUid
import org.hisp.dhis.android.core.visualization.CategoryDimension
import org.hisp.dhis.android.core.visualization.DataDimensionItem
import org.hisp.dhis.android.core.visualization.Visualization
import org.hisp.dhis.android.core.visualization.VisualizationCategoryDimensionLink

@Reusable
internal class VisualizationHandler @Inject constructor(
    store: IdentifiableObjectStore<Visualization>,
    private val visualizationCollectionCleaner: CollectionCleaner<Visualization>,
    private val visualizationCategoryDimensionLinkStore: LinkStore<VisualizationCategoryDimensionLink>,
    private val dataDimensionItemStore: LinkStore<DataDimensionItem>,
    private val visualizationCategoryDimensionLinkHandler:
        LinkHandler<ObjectWithUid, VisualizationCategoryDimensionLink>,
    private val dataDimensionItemHandler: LinkHandler<DataDimensionItem, DataDimensionItem>
) : IdentifiableHandlerImpl<Visualization>(store) {

    override fun beforeCollectionHandled(
        oCollection: Collection<Visualization>
    ): Collection<Visualization> {
        visualizationCategoryDimensionLinkStore.delete()
        dataDimensionItemStore.delete()
        return oCollection
    }

    override fun afterObjectHandled(o: Visualization, action: HandleAction) {
        o.categoryDimensions()?.forEach { categoryDimension: CategoryDimension ->
            categoryDimension.category()?.let {
                visualizationCategoryDimensionLinkHandler.handleMany(
                    it.uid(), categoryDimension.categoryOptions()
                ) { categoryOption: ObjectWithUid ->
                    VisualizationCategoryDimensionLink.builder()
                        .visualization(o.uid())
                        .category(categoryDimension.category()?.uid())
                        .categoryOption(categoryOption.uid())
                        .build()
                }
            }
        }

        dataDimensionItemHandler.handleMany(o.uid(), o.dataDimensionItems()) {
            it.toBuilder().visualization(o.uid()).build()
        }
    }

    override fun afterCollectionHandled(oCollection: Collection<Visualization>?) {
        visualizationCollectionCleaner.deleteNotPresent(oCollection)
    }
}
