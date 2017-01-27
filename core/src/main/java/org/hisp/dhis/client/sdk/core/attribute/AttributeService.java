
package org.hisp.dhis.client.sdk.core.attribute;

import org.hisp.dhis.client.sdk.core.common.services.Get;
import org.hisp.dhis.client.sdk.core.common.services.ListAll;
import org.hisp.dhis.client.sdk.core.common.services.Remove;
import org.hisp.dhis.client.sdk.core.common.services.Save;
import org.hisp.dhis.client.sdk.core.common.services.Service;
import org.hisp.dhis.client.sdk.models.attribute.Attribute;

public interface AttributeService extends Service, Save<Attribute>,
        Remove<Attribute>, Get<Attribute>,
        ListAll<Attribute> {
}
