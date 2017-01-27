
package org.hisp.dhis.client.sdk.models.attribute;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.hisp.dhis.client.sdk.models.common.base.BaseModel;

@JsonIgnoreProperties(ignoreUnknown = true)
public final class AttributeValue extends BaseModel {


    @JsonIgnore
    String attributeUId;

    @JsonProperty("attribute")
    Attribute attribute;

    @JsonProperty("value")
    String value;

    @JsonProperty("created")
    String created;

    @JsonProperty("lastUpdated")
    String lastUpdated;

    @JsonIgnore
    String referenceUId;

    @JsonIgnore
    String itemType;

    public AttributeValue() {
        // explicit empty constructor
    }

    public Attribute getAttribute() {
        return attribute;
    }

    public void setAttribute(Attribute attribute) {
        if (attribute != null && attribute.getUId() != null) {
            setAttributeUId(attribute.getUId());
        }
        this.attribute = attribute;
    }

    public String getAttributeUId() {
        return attributeUId;
    }

    public void setAttributeUId(String attributeUId) {
        this.attributeUId = attributeUId;
    }

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

    public String getReferenceUId() {
        return referenceUId;
    }

    public void setReferenceUId(String referenceUId) {
        this.referenceUId = referenceUId;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    @Override
    public String toString() {
        return "AttributeValue{" +
                "attributeUId='" + attributeUId + '\'' +
                ", attribute=" + attribute +
                ", value='" + value + '\'' +
                ", created='" + created + '\'' +
                ", lastUpdated='" + lastUpdated + '\'' +
                ", referenceUId='" + referenceUId + '\'' +
                ", itemType='" + itemType + '\'' +
                '}';
    }
}
