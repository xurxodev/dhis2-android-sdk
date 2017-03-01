package org.hisp.dhis.client.sdk.android.categoryoptiongroup;

import static org.hisp.dhis.client.sdk.android.api.network.NetworkUtils.getCollection;

import org.hisp.dhis.client.sdk.android.api.network.ApiResource;
import org.hisp.dhis.client.sdk.core.categoryoptiongroup.CategoryOptionGroupApiClient;
import org.hisp.dhis.client.sdk.core.common.Fields;
import org.hisp.dhis.client.sdk.core.common.network.ApiException;
import org.hisp.dhis.client.sdk.models.category.CategoryOptionGroup;
import org.joda.time.DateTime;

import java.util.List;
import java.util.Map;
import java.util.Set;

import retrofit2.Call;


public class CategoryOptionGroupApiClientImpl implements CategoryOptionGroupApiClient {

    private final CategoryOptionGroupApiClientRetrofit mCategoryOptionGroupApiClientRetrofit;

    public CategoryOptionGroupApiClientImpl(
            CategoryOptionGroupApiClientRetrofit categoryOptionGroupApiClientRetrofit) {
        mCategoryOptionGroupApiClientRetrofit = categoryOptionGroupApiClientRetrofit;
    }

    @Override
    public List<CategoryOptionGroup> getCategoryOptionGroups(Fields fields, DateTime lastUpdate,
            Set<String> uids) throws ApiException {

        ApiResource<CategoryOptionGroup> apiResource = new ApiResource<CategoryOptionGroup>() {
            static final String IDENTIFIABLE_PROPERTIES =
                    "id,name,displayName,created,lastUpdated,access,shortName,dataDimensionType,"
                            + "publicAccess,displayShortName,externalAccess,dimensionItem,"
                            + "dimensionItemType,categoryOptions,attributeValues";

            @Override
            public String getResourceName() {
                return "categoryOptionGroups";
            }

            @Override
            public String getBasicProperties() {
                return "id";
            }

            @Override
            public String getAllProperties() {
                return IDENTIFIABLE_PROPERTIES;
            }

            @Override
            public String getDescendantProperties() {
                return IDENTIFIABLE_PROPERTIES;
            }

            @Override
            public Call<Map<String, List<CategoryOptionGroup>>> getEntities(
                    Map<String, String> queryMap,
                    List<String> filters) throws ApiException {
                return mCategoryOptionGroupApiClientRetrofit.getCategoryOtionGroups(queryMap);
            }
        };

        List<CategoryOptionGroup> categoryOptionGroups = getCollection(apiResource, fields, null,
                null);

        return categoryOptionGroups;
    }
}
