package org.hisp.dhis.client.sdk.android.categoryoptiongroup;

import static org.hisp.dhis.client.sdk.android.api.network.NetworkUtils.call;

import org.hisp.dhis.client.sdk.core.categoryoptiongroup.CategoryOptionGroupApiClient;
import org.hisp.dhis.client.sdk.core.categoryoptiongroup.CategoryOptionGroupFilters;
import org.hisp.dhis.client.sdk.core.common.Fields;
import org.hisp.dhis.client.sdk.core.common.network.ApiException;
import org.hisp.dhis.client.sdk.models.category.CategoryOptionGroup;
import org.hisp.dhis.client.sdk.models.category.CategoryOptionGroupWrapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CategoryOptionGroupApiClientImpl implements CategoryOptionGroupApiClient {

    private final CategoryOptionGroupApiClientRetrofit mCategoryOptionGroupApiClientRetrofit;

    public CategoryOptionGroupApiClientImpl(
            CategoryOptionGroupApiClientRetrofit categoryOptionGroupApiClientRetrofit) {
        mCategoryOptionGroupApiClientRetrofit = categoryOptionGroupApiClientRetrofit;
    }

    @Override
    public List<CategoryOptionGroup> getCategoryOptionGroups(Fields fields,
            CategoryOptionGroupFilters categoryOptionGroupFilters) throws ApiException {

        Map<String, String> queryMap = new HashMap<>();
        addFilters(queryMap, categoryOptionGroupFilters);
        addCommonFields(fields, queryMap);
        return callCategoryOptionGroups(queryMap);
    }


    private void addFilters(Map<String, String> queryMap,
            CategoryOptionGroupFilters categoryOptionGroupFilters) {

        if (categoryOptionGroupFilters != null) {
            if (categoryOptionGroupFilters.getCategoryOptionGroupSetUid() != null
                    && !categoryOptionGroupFilters.getCategoryOptionGroupSetUid().isEmpty()) {
                queryMap.put("filter", "categoryOptionGroupSet.id:eq:"
                        + categoryOptionGroupFilters.getCategoryOptionGroupSetUid());
            }
            if (categoryOptionGroupFilters.getCategoryOptionUid() != null
                    && !categoryOptionGroupFilters.getCategoryOptionUid().isEmpty()) {
                queryMap.put("filter", "categoryOptions.id:eq:"
                        + categoryOptionGroupFilters.getCategoryOptionUid());
            }
        }
    }


    private void addCommonFields(Fields fields, Map<String, String> queryMap) {
        switch (fields) {
            case BASIC: {
                queryMap.put("fields", "id");
                break;
            }
            case ALL: {
                queryMap.put("fields",
                        "id,code,name,displayName,created,lastUpdated,access,shortName,"
                                + "dataDimensionType,publicAccess,displayShortName,"
                                + "externalAccess,dimensionItem,dimensionItemType,"
                                + "categoryOptions,attributeValues");
                break;
            }
        }
    }

    private List<CategoryOptionGroup> callCategoryOptionGroups(Map<String, String> queryMap) {
        CategoryOptionGroupWrapper response = call(
                mCategoryOptionGroupApiClientRetrofit.getCategoryOptionGroups(queryMap));
        List<CategoryOptionGroup> categoryOptionGroups = new ArrayList<>();
        categoryOptionGroups.addAll(response.getCategoryOptionGroups());
        return categoryOptionGroups;
    }



}
