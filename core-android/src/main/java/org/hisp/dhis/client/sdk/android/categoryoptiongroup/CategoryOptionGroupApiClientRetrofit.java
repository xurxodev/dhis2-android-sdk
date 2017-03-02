package org.hisp.dhis.client.sdk.android.categoryoptiongroup;

import org.hisp.dhis.client.sdk.models.category.CategoryOptionGroupWrapper;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface CategoryOptionGroupApiClientRetrofit {

    @GET("categoryOptionGroups")
    Call<CategoryOptionGroupWrapper> getCategoryOptionGroups(
            @Query("fields") String fields,
            @Query("filter") String categoryOptionGroupSetUid,
            @Query("filter") String categoryOptionUid);

}
