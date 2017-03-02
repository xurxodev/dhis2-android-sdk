
package org.hisp.dhis.client.sdk.android.categoryoption;

import org.hisp.dhis.client.sdk.android.api.persistence.flow.CategoryOptionFlow;
import org.hisp.dhis.client.sdk.android.common.AbsStore;
import org.hisp.dhis.client.sdk.core.categoryoption.CategoryOptionStore;
import org.hisp.dhis.client.sdk.core.common.persistence.TransactionManager;
import org.hisp.dhis.client.sdk.models.category.CategoryOption;

public class CategoryOptionStoreImpl extends AbsStore<CategoryOption, CategoryOptionFlow>
        implements CategoryOptionStore {
    private final TransactionManager transactionManager;

    public CategoryOptionStoreImpl(TransactionManager transactionManager) {
        super(CategoryOptionFlow.MAPPER);

        this.transactionManager = transactionManager;
    }
}
