package org.hisp.dhis.client.sdk.core.categoryoptiongroup;

import org.hisp.dhis.client.sdk.core.common.services.Get;
import org.hisp.dhis.client.sdk.core.common.services.ListAll;
import org.hisp.dhis.client.sdk.core.common.services.Remove;
import org.hisp.dhis.client.sdk.core.common.services.Save;
import org.hisp.dhis.client.sdk.core.common.services.Service;
import org.hisp.dhis.client.sdk.models.category.CategoryOptionGroup;

public interface CategoryOptionGroupService extends Service, Save<CategoryOptionGroup>,
        Remove<CategoryOptionGroup>, Get<CategoryOptionGroup>,
        ListAll<CategoryOptionGroup> {
}
