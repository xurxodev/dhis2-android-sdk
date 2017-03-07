package org.hisp.dhis.client.sdk.android.categoryoptiongroup;

import static org.hisp.dhis.client.sdk.android.api.network.NetworkUtils.call;

import org.hisp.dhis.client.sdk.core.categoryoptiongroup.CategoryOptionGroupApiClient;
import org.hisp.dhis.client.sdk.core.categoryoptiongroup.CategoryOptionGroupFilters;
import org.hisp.dhis.client.sdk.core.common.Fields;
import org.hisp.dhis.client.sdk.core.common.network.ApiException;
import org.hisp.dhis.client.sdk.models.category.CategoryOptionGroup;
import org.hisp.dhis.client.sdk.models.category.CategoryOptionGroupWrapper;

import java.util.ArrayList;
import java.util.List;


public class CategoryOptionGroupApiClientImpl implements CategoryOptionGroupApiClient {

    private final CategoryOptionGroupApiClientRetrofit mCategoryOptionGroupApiClientRetrofit;

    public CategoryOptionGroupApiClientImpl(
            CategoryOptionGroupApiClientRetrofit categoryOptionGroupApiClientRetrofit) {
        mCategoryOptionGroupApiClientRetrofit = categoryOptionGroupApiClientRetrofit;
    }

    @Override
    public List<CategoryOptionGroup> getCategoryOptionGroups(Fields fields,
            CategoryOptionGroupFilters categoryOptionGroupFilters) throws ApiException {

        String fieldsStr = addCommonFields(fields);
        String categoryOptionGroupSetUid = gatCategoryOptionSetUid(categoryOptionGroupFilters);
        String categoryOptionUid = gatCategoryOptionUid(categoryOptionGroupFilters);
        return callCategoryOptionGroups(fieldsStr, categoryOptionGroupSetUid, categoryOptionUid);
    }


    private String gatCategoryOptionSetUid(CategoryOptionGroupFilters categoryOptionGroupFilters) {
        if (categoryOptionGroupFilters != null) {
            if (categoryOptionGroupFilters.getCategoryOptionGroupSetUid() != null
                    && !categoryOptionGroupFilters.getCategoryOptionGroupSetUid().isEmpty()) {
                return "categoryOptionGroupSet.id:eq:"
                        + categoryOptionGroupFilters.getCategoryOptionGroupSetUid();
            }
        }
        return "";
    }

    private String gatCategoryOptionUid(CategoryOptionGroupFilters categoryOptionGroupFilters) {
        if (categoryOptionGroupFilters != null) {
            if (categoryOptionGroupFilters.getCategoryOptionUid() != null
                    && !categoryOptionGroupFilters.getCategoryOptionUid().isEmpty()) {
                return "categoryOptions.id:eq:"
                        + categoryOptionGroupFilters.getCategoryOptionUid();
            }
        }
        return "";
    }


    private String addCommonFields(Fields fields) {
        switch (fields) {
            case BASIC: {
                return "id";
            }
            case ALL: {
                return "id,code,name,displayName,created,lastUpdated,access,shortName,"
                                + "dataDimensionType,publicAccess,displayShortName,"
                                + "externalAccess,dimensionItem,dimensionItemType,"
                        + "categoryOptions,attributeValues";
            }
            default:
                return "id";
        }
    }

    private List<CategoryOptionGroup> callCategoryOptionGroups(String fieldsStr,
            String categoryOptionGroupSetUid, String categoryOptionUid) {
        CategoryOptionGroupWrapper response = call(
                mCategoryOptionGroupApiClientRetrofit.getCategoryOptionGroups(fieldsStr,
                        categoryOptionGroupSetUid, categoryOptionUid));
        List<CategoryOptionGroup> categoryOptionGroups = new ArrayList<>();
        categoryOptionGroups.addAll(response.getCategoryOptionGroups());
        return categoryOptionGroups;
    }



}
