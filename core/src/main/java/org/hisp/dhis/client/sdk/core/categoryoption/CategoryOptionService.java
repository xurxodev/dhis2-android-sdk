
package org.hisp.dhis.client.sdk.core.categoryoption;

import org.hisp.dhis.client.sdk.core.common.services.Get;
import org.hisp.dhis.client.sdk.core.common.services.ListAll;
import org.hisp.dhis.client.sdk.core.common.services.Remove;
import org.hisp.dhis.client.sdk.core.common.services.Save;
import org.hisp.dhis.client.sdk.core.common.services.Service;
import org.hisp.dhis.client.sdk.models.attribute.Attribute;
import org.hisp.dhis.client.sdk.models.category.CategoryOption;

public interface CategoryOptionService extends Service, Save<CategoryOption>,
        Remove<CategoryOption>, Get<CategoryOption>,
        ListAll<CategoryOption> {
}
