package org.hisp.dhis.client.sdk.core.attribute;

import org.hisp.dhis.client.sdk.core.attribute.AttributeValueService;
import org.hisp.dhis.client.sdk.core.attribute.AttributeValueStore;
import org.hisp.dhis.client.sdk.models.attribute.AttributeValue;
import org.hisp.dhis.client.sdk.utils.Preconditions;

import java.util.List;

public class AttributeValueServiceImpl implements AttributeValueService {
    private final AttributeValueStore attributeValueStore;

    public AttributeValueServiceImpl(AttributeValueStore attributeValueStore) {
        this.attributeValueStore = attributeValueStore;
    }

    @Override
    public List<AttributeValue> list() {
        return attributeValueStore.queryAll();
    }

    @Override
    public AttributeValue get(long id) {
        return attributeValueStore.queryById(id);
    }

    @Override
    public boolean remove(AttributeValue object) {
        Preconditions.isNull(object, "Object must not be null");
        return attributeValueStore.delete(object);
    }

    @Override
    public boolean save(AttributeValue object) {
        Preconditions.isNull(object, "Object must not be null");
        return attributeValueStore.save(object);
    }
}