
package org.hisp.dhis.client.sdk.android.attributes;

import org.hisp.dhis.client.sdk.android.api.persistence.flow.AttributeFlow;
import org.hisp.dhis.client.sdk.android.common.AbsStore;
import org.hisp.dhis.client.sdk.core.attribute.AttributeStore;
import org.hisp.dhis.client.sdk.core.common.persistence.TransactionManager;
import org.hisp.dhis.client.sdk.models.attribute.Attribute;

public class AttributeStoreImpl extends AbsStore<Attribute, AttributeFlow>
        implements AttributeStore {
    private final TransactionManager transactionManager;

    public AttributeStoreImpl(TransactionManager transactionManager) {
        super(AttributeFlow.MAPPER);

        this.transactionManager = transactionManager;
    }
}
