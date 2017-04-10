package org.hisp.dhis.client.sdk.models.category;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.hisp.dhis.client.sdk.models.attribute.AttributeValue;
import org.hisp.dhis.client.sdk.models.common.base.BaseIdentifiableObject;

import java.util.List;


public class CategoryOptionGroup extends BaseIdentifiableObject {
    @JsonProperty("code")
    String code;
    @JsonProperty("shortName")
    String shortName;
    @JsonProperty("dataDimensionType")
    String dataDimensionType;
    @JsonProperty("publicAccess")
    String publicAccess;
    @JsonProperty("displayShortName")
    String displayShortName;
    @JsonProperty("externalAccess")
    boolean externalAccess;
    @JsonProperty("dimensionItem")
    String dimensionItem;
    @JsonProperty("dimensionItemType")
    String dimensionItemType;
    @JsonProperty("categoryOptions")
    List<CategoryOption> categoryOptions;
    @JsonProperty("attributeValues")
    List<AttributeValue> attributeValues;

    public CategoryOptionGroup() {
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getDataDimensionType() {
        return dataDimensionType;
    }

    public void setDataDimensionType(String dataDimensionType) {
        this.dataDimensionType = dataDimensionType;
    }

    public String getPublicAccess() {
        return publicAccess;
    }

    public void setPublicAccess(String publicAccess) {
        this.publicAccess = publicAccess;
    }

    public String getDisplayShortName() {
        return displayShortName;
    }

    public void setDisplayShortName(String displayShortName) {
        this.displayShortName = displayShortName;
    }

    public boolean isExternalAccess() {
        return externalAccess;
    }

    public void setExternalAccess(boolean externalAccess) {
        this.externalAccess = externalAccess;
    }

    public String getDimensionItem() {
        return dimensionItem;
    }

    public void setDimensionItem(String dimensionItem) {
        this.dimensionItem = dimensionItem;
    }

    public String getDimensionItemType() {
        return dimensionItemType;
    }

    public void setDimensionItemType(String dimensionItemType) {
        this.dimensionItemType = dimensionItemType;
    }

    public List<CategoryOption> getCategoryOptions() {
        return categoryOptions;
    }

    public void setCategoryOptions(
            List<CategoryOption> categoryOptions) {
        this.categoryOptions = categoryOptions;
    }

    public List<AttributeValue> getAttributeValues() {
        return attributeValues;
    }

    public void setAttributeValues(
            List<AttributeValue> attributeValues) {
        this.attributeValues = attributeValues;
    }
}
