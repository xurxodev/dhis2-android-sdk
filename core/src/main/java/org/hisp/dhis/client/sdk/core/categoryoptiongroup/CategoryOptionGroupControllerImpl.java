package org.hisp.dhis.client.sdk.core.categoryoptiongroup;

import org.hisp.dhis.client.sdk.core.common.Fields;
import org.hisp.dhis.client.sdk.core.common.network.ApiException;
import org.hisp.dhis.client.sdk.core.common.persistence.DbOperation;
import org.hisp.dhis.client.sdk.core.common.persistence.DbOperationImpl;
import org.hisp.dhis.client.sdk.core.common.persistence.TransactionManager;
import org.hisp.dhis.client.sdk.core.common.preferences.DateType;
import org.hisp.dhis.client.sdk.core.common.preferences.LastUpdatedPreferences;
import org.hisp.dhis.client.sdk.core.common.preferences.ResourceType;
import org.hisp.dhis.client.sdk.core.systeminfo.SystemInfoController;
import org.hisp.dhis.client.sdk.models.category.CategoryOptionGroup;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

public class CategoryOptionGroupControllerImpl implements CategoryOptionGroupController {

    private final SystemInfoController mSystemInfoController;
    private final CategoryOptionGroupApiClient mCategoryOptionGroupApiClient;
    private final CategoryOptionGroupStore mCategoryOptionGroupStore;

    private final LastUpdatedPreferences mLastUpdatedPreferences;
    private final TransactionManager mTransactionManager;


    public CategoryOptionGroupControllerImpl(
            SystemInfoController systemInfoController,
            CategoryOptionGroupApiClient categoryOptionGroupApiClient,
            CategoryOptionGroupStore categoryOptionGroupStore,
            LastUpdatedPreferences lastUpdatedPreferences,
            TransactionManager transactionManager) {
        mSystemInfoController = systemInfoController;
        mCategoryOptionGroupApiClient = categoryOptionGroupApiClient;
        mCategoryOptionGroupStore = categoryOptionGroupStore;
        mLastUpdatedPreferences = lastUpdatedPreferences;
        mTransactionManager = transactionManager;
    }

    @Override
    public void pull(CategoryOptionGroupFilters categoryOptionGroupFilters) throws ApiException {

        DateTime serverTime = mSystemInfoController.getSystemInfo().getServerDate();
        List<CategoryOptionGroup> allCategoryOptionGroups =
                mCategoryOptionGroupApiClient.getCategoryOptionGroups(
                        Fields.ALL, categoryOptionGroupFilters);

        List<DbOperation> dbOperations = new ArrayList<>();
        for (CategoryOptionGroup categoryOptionGroup : allCategoryOptionGroups) {
            dbOperations.add(
                    DbOperationImpl.with(mCategoryOptionGroupStore).save(categoryOptionGroup));
            mTransactionManager.transact(dbOperations);

            mLastUpdatedPreferences.save(ResourceType.CATEGORY_OPTION_GROUPS, DateType.SERVER,
                    serverTime);
        }

    }

}
