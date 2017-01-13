
package org.hisp.dhis.client.sdk.core.attribute;

import org.hisp.dhis.client.sdk.models.attribute.Attribute;
import org.hisp.dhis.client.sdk.utils.Preconditions;

import java.util.List;

/**
 * Created by idelcano on 12/01/2017.
 */

public class AttributeServiceImpl implements AttributeService {
    private final AttributeStore attributeStore;

    public AttributeServiceImpl(AttributeStore attributeStore) {
        this.attributeStore = attributeStore;
    }

    @Override
    public List<Attribute> list() {
        return attributeStore.queryAll();
    }

    @Override
    public Attribute get(long id) {
        return attributeStore.queryById(id);
    }

    @Override
    public boolean remove(Attribute object) {
        Preconditions.isNull(object, "Object must not be null");
        return attributeStore.delete(object);
    }

    @Override
    public boolean save(Attribute object) {
        Preconditions.isNull(object, "Object must not be null");
        return attributeStore.save(object);
    }
}