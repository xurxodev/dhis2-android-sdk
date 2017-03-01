package org.hisp.dhis.client.sdk.core.categoryoptiongroup;

import org.hisp.dhis.client.sdk.core.common.Fields;
import org.hisp.dhis.client.sdk.core.common.network.ApiException;
import org.hisp.dhis.client.sdk.models.category.CategoryOptionGroup;
import org.joda.time.DateTime;

import java.util.List;
import java.util.Set;

public interface CategoryOptionGroupApiClient {
    List<CategoryOptionGroup> getCategoryOptionGroups(Fields fields, DateTime lastUpdate,
            Set<String> uids) throws
            ApiException;
}
