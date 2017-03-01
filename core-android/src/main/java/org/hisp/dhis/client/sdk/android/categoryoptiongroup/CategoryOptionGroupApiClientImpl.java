package org.hisp.dhis.client.sdk.android.categoryoptiongroup;

import org.hisp.dhis.client.sdk.core.categoryoptiongroup.CategoryOptionGroupApiClient;
import org.hisp.dhis.client.sdk.core.common.Fields;
import org.hisp.dhis.client.sdk.core.common.network.ApiException;
import org.hisp.dhis.client.sdk.models.category.CategoryOptionGroup;
import org.joda.time.DateTime;

import java.util.List;
import java.util.Set;


public class CategoryOptionGroupApiClientImpl implements CategoryOptionGroupApiClient {

    private final CategoryOptionGroupApiClientRetrofit mCategoryOptionGroupApiClientRetrofit;

    public CategoryOptionGroupApiClientImpl(
            CategoryOptionGroupApiClientRetrofit categoryOptionGroupApiClientRetrofit) {
        mCategoryOptionGroupApiClientRetrofit = categoryOptionGroupApiClientRetrofit;
    }

    @Override
    public List<CategoryOptionGroup> getCategoryOptionGroups(Fields fields, DateTime lastUpdate,
            Set<String> uids) throws ApiException {
        return null;
    }
}
