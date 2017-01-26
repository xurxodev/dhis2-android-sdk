
package org.hisp.dhis.client.sdk.core.attribute;

import org.hisp.dhis.client.sdk.core.common.network.ApiException;

public interface AttributeController {
    void pull() throws ApiException;
}
