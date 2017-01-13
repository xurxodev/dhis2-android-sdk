
package org.hisp.dhis.client.sdk.core.attribute;

import org.hisp.dhis.client.sdk.core.common.Fields;
import org.hisp.dhis.client.sdk.core.common.network.ApiException;
import org.hisp.dhis.client.sdk.models.attribute.Attribute;
import org.joda.time.DateTime;

import java.util.List;
import java.util.Set;

/**
 * Created by idelcano on 12/01/2017.
 */

public interface AttributeApiClient {
    List<Attribute> getAttributes(
            Fields fields, DateTime lastUpdated, Set<String> uids) throws ApiException;
}
