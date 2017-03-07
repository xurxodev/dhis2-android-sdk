package org.hisp.dhis.client.sdk.models.category;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.hisp.dhis.client.sdk.models.pager.Pager;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CategoryOptionGroupWrapper {
    @JsonProperty("pager")
    private Pager mPager;
    @JsonProperty("categoryOptionGroups")
    private List<CategoryOptionGroup> mCategoryOptionGroups;

    public CategoryOptionGroupWrapper() {
    }

    public Pager getPager() {
        return mPager;
    }

    public void setPager(Pager pager) {
        mPager = pager;
    }

    public List<CategoryOptionGroup> getCategoryOptionGroups() {
        return mCategoryOptionGroups;
    }

    public void setCategoryOptionGroups(
            List<CategoryOptionGroup> categoryOptionGroups) {
        mCategoryOptionGroups = categoryOptionGroups;
    }
}
