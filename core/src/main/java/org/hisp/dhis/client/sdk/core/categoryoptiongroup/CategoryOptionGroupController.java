package org.hisp.dhis.client.sdk.core.categoryoptiongroup;

import org.hisp.dhis.client.sdk.core.common.network.ApiException;

public interface CategoryOptionGroupController {
    void pull(CategoryOptionGroupFilters categoryOptionGroupFilters) throws ApiException;
}
