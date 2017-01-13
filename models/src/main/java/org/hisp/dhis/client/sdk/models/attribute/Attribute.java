
package org.hisp.dhis.client.sdk.models.attribute;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.hisp.dhis.client.sdk.models.common.base.BaseModel;

@JsonIgnoreProperties(ignoreUnknown = true)
public final class Attribute extends BaseModel {

    @JsonProperty("code")
    String code;

    @JsonProperty("id")
    String UId;

    @JsonProperty("name")
    String name;

    @JsonProperty("displayName")
    String displayName;

    @JsonProperty("lastUpdated")
    String lastUpdated;

    @JsonProperty("created")
    String created;

    @JsonProperty("valueType")
    String valueType;

    public Attribute() {
        // explicit empty constructor
    }

    public String getValueType() {
        return valueType;
    }

    public void setValueType(String valueType) {
        this.valueType = valueType;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getUId() {
        return UId;
    }

    public void setUId(String UId) {
        this.UId = UId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }
}
