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
package org.hisp.dhis.android.core.category.internal;

import org.hisp.dhis.android.core.arch.db.access.DatabaseAdapter;
import org.hisp.dhis.android.core.arch.db.stores.internal.LinkChildStore;
import org.hisp.dhis.android.core.arch.db.stores.internal.StoreFactory;
import org.hisp.dhis.android.core.arch.db.stores.projections.internal.LinkTableChildProjection;
import org.hisp.dhis.android.core.arch.repositories.children.internal.ChildrenAppender;
import org.hisp.dhis.android.core.category.Category;
import org.hisp.dhis.android.core.category.CategoryCategoryComboLinkTableInfo;
import org.hisp.dhis.android.core.category.CategoryCombo;
import org.hisp.dhis.android.core.category.CategoryTableInfo;

final class CategoryCategoryComboChildrenAppender extends ChildrenAppender<CategoryCombo> {


    private static final LinkTableChildProjection CHILD_PROJECTION = new LinkTableChildProjection(
            CategoryTableInfo.TABLE_INFO,
            CategoryCategoryComboLinkTableInfo.Columns.CATEGORY_COMBO,
            CategoryCategoryComboLinkTableInfo.Columns.CATEGORY);

    private final LinkChildStore<CategoryCombo, Category> linkChildStore;

    private CategoryCategoryComboChildrenAppender(LinkChildStore<CategoryCombo, Category> linkChildStore) {
        this.linkChildStore = linkChildStore;
    }

    @Override
    protected CategoryCombo appendChildren(CategoryCombo categoryCombo) {
        CategoryCombo.Builder builder = categoryCombo.toBuilder();
        builder.categories(linkChildStore.getChildren(categoryCombo));
        return builder.build();
    }

    static ChildrenAppender<CategoryCombo> create(DatabaseAdapter databaseAdapter) {
        return new CategoryCategoryComboChildrenAppender(
                StoreFactory.linkChildStore(
                        databaseAdapter,
                        CategoryCategoryComboLinkTableInfo.TABLE_INFO,
                        CHILD_PROJECTION,
                        Category::create
                )
        );
    }
}