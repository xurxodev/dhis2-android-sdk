package org.hisp.dhis.client.sdk.android.api.persistence.flow;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.Table;

import org.hisp.dhis.client.sdk.android.api.persistence.DbDhis;
import org.hisp.dhis.client.sdk.android.common.AbsMapper;
import org.hisp.dhis.client.sdk.models.attribute.Attribute;
import org.hisp.dhis.client.sdk.models.attribute.AttributeValue;

@Table(database = DbDhis.class)
public class AttributeValueFlow extends BaseModelFlow {

    public static final AttributeValueMapper MAPPER = new AttributeValueMapper();

    public AttributeValueFlow() {
    }


    @Column(name = "attribute")
    String attributeUId;

    AttributeFlow attribute;

    @Column(name = "value")
    String value;

    @Column(name = "created")
    String created;

    @Column(name = "lastUpdated")
    String lastUpdated;

    @Column(name = "reference")
    String reference;

    @Column(name = "itemType")
    String itemType;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public void setAttribute(AttributeFlow attribute) {
        this.attribute = attribute;
    }

    public AttributeFlow getAttribute() {
        return attribute;
    }

    public String getAttributeUId() {
        return attributeUId;
    }

    public void setAttributeUId(String attributeUId) {
        this.attributeUId = attributeUId;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public static class AttributeValueMapper extends
            AbsMapper<AttributeValue, AttributeValueFlow> {

        @Override
        public AttributeValueFlow mapToDatabaseEntity(AttributeValue attributeValue) {
            if (attributeValue == null) {
                return null;
            }

            AttributeValueFlow attributeValueFlow = new AttributeValueFlow();
            attributeValueFlow.setAttributeUId(attributeValue.getAttributeUId());
            attributeValueFlow.setValue(attributeValue.getValue());
            attributeValueFlow.setReference(attributeValue.getReferenceUId());
            attributeValueFlow.setCreated(attributeValue.getCreated());
            attributeValueFlow.setLastUpdated(attributeValue.getLastUpdated());
            attributeValueFlow.setItemType(attributeValue.getItemType());

            return attributeValueFlow;
        }

        @Override
        public AttributeValue mapToModel(AttributeValueFlow attributeValueFlow) {
            if (attributeValueFlow == null) {
                return null;
            }

            AttributeValue attributeValue = new AttributeValue();
            attributeValue.setAttributeUId(attributeValueFlow.getAttributeUId());
            attributeValue.setValue(attributeValueFlow.getValue());
            attributeValue.setReferenceUId(attributeValueFlow.getReference());
            attributeValue.setCreated(attributeValueFlow.getCreated());
            attributeValue.setLastUpdated(attributeValueFlow.getLastUpdated());
            attributeValue.setItemType(attributeValueFlow.getItemType());

            return attributeValue;
        }

        @Override
        public Class<AttributeValue> getModelTypeClass() {
            return AttributeValue.class;
        }

        @Override
        public Class<AttributeValueFlow> getDatabaseEntityTypeClass() {
            return AttributeValueFlow.class;
        }
    }
}