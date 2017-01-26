
package org.hisp.dhis.client.sdk.android.attributes;

import org.hisp.dhis.client.sdk.models.attribute.Attribute;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface AttributeApiClientRetrofit {

    @GET("attributes")
    Call<Map<String, List<Attribute>>> getAttributes(@QueryMap Map<String, String> queryMap,
            @Query("filter") List<String> filters);
}
