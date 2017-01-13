
package org.hisp.dhis.client.sdk.core.attribute;

import org.hisp.dhis.client.sdk.core.common.network.ApiException;

/**
 * Created by idelcano on 12/01/2017.
 */

public interface AttributeController  {
    void pull() throws ApiException;
}
