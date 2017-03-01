package org.hisp.dhis.client.sdk.android.categoryoptiongroup;

import org.hisp.dhis.client.sdk.android.api.persistence.flow.CategoryOptionGroupFlow;
import org.hisp.dhis.client.sdk.android.common.AbsStore;
import org.hisp.dhis.client.sdk.core.categoryoptiongroup.CategoryOptionGroupStore;
import org.hisp.dhis.client.sdk.core.common.persistence.TransactionManager;
import org.hisp.dhis.client.sdk.models.category.CategoryOptionGroup;


public class CategoryOptionGroupStoreImpl extends
        AbsStore<CategoryOptionGroup, CategoryOptionGroupFlow> implements CategoryOptionGroupStore {
    private final TransactionManager mTransactionManager;

    public CategoryOptionGroupStoreImpl(TransactionManager transactionManager) {
        super(CategoryOptionGroupFlow.MAPPER);

        mTransactionManager = transactionManager;
    }
}
