package org.hisp.dhis.client.sdk.core.categoryoptiongroup;

import org.hisp.dhis.client.sdk.core.common.Fields;
import org.hisp.dhis.client.sdk.core.common.network.ApiException;
import org.hisp.dhis.client.sdk.models.category.CategoryOptionGroup;

import java.util.List;

public interface CategoryOptionGroupApiClient {
    List<CategoryOptionGroup> getCategoryOptionGroups(Fields fields,
            CategoryOptionGroupFilters categoryOptionGroupFilters) throws
            ApiException;
}
