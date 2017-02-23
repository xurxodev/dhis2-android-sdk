
package org.hisp.dhis.client.sdk.android.categoryoption;

import static android.R.attr.id;
import static android.R.attr.level;

import static org.hisp.dhis.client.sdk.android.api.network.NetworkUtils.getCollection;

import org.hisp.dhis.client.sdk.android.api.network.ApiResource;
import org.hisp.dhis.client.sdk.android.attributes.AttributeApiClientRetrofit;
import org.hisp.dhis.client.sdk.core.attribute.AttributeApiClient;
import org.hisp.dhis.client.sdk.core.categoryoption.CategoryOptionApiClient;
import org.hisp.dhis.client.sdk.core.common.Fields;
import org.hisp.dhis.client.sdk.core.common.network.ApiException;
import org.hisp.dhis.client.sdk.models.attribute.Attribute;
import org.hisp.dhis.client.sdk.models.category.CategoryOption;
import org.joda.time.DateTime;

import java.util.List;
import java.util.Map;
import java.util.Set;

import retrofit2.Call;

public class CategoryOptionApiClientImpl implements CategoryOptionApiClient {
    private final CategoryOptionApiClientRetrofit categoryOptionApiClientRetrofit;

    public CategoryOptionApiClientImpl(CategoryOptionApiClientRetrofit categoryOptionApiClientRetrofit) {
        this.categoryOptionApiClientRetrofit = categoryOptionApiClientRetrofit;
    }

    @Override
    public List<CategoryOption> getCategoryOptions(Fields fields, DateTime lastUpdated, Set<String> uids)
            throws ApiException {
        ApiResource<CategoryOption> apiResource = new ApiResource<CategoryOption>() {
            static final String IDENTIFIABLE_PROPERTIES =
                    "id,shortName,displayName,created,lastUpdated,code";

            @Override
            public String getResourceName() {
                return "categoryOptions";
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
                return IDENTIFIABLE_PROPERTIES; // end
            }

            @Override
            public Call<Map<String, List<CategoryOption>>> getEntities(
                    Map<String, String> queryMap, List<String> filters) throws ApiException {
                return categoryOptionApiClientRetrofit.getCategoryOptions(queryMap);
            }
        };

        List<CategoryOption> categoryOptions = getCollection(apiResource, fields, null, null);

        return categoryOptions;
    }
}
