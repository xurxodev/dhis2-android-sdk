package org.hisp.dhis.client.sdk.android.categoryoptiongroup;

import org.hisp.dhis.client.sdk.models.category.CategoryOptionGroup;
import org.hisp.dhis.client.sdk.models.category.CategoryOptionGroupWrapper;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

public interface CategoryOptionGroupApiClientRetrofit {

    @GET("categoryOptionGroups")
    Call<CategoryOptionGroupWrapper> getCategoryOptionGroups(
            @QueryMap Map<String, String> queryMap);

}
