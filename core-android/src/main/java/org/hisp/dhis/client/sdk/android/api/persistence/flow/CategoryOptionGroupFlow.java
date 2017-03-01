package org.hisp.dhis.client.sdk.android.api.persistence.flow;


import com.raizlabs.android.dbflow.annotation.Column;

import org.hisp.dhis.client.sdk.android.common.AbsMapper;
import org.hisp.dhis.client.sdk.android.common.Mapper;
import org.hisp.dhis.client.sdk.models.category.CategoryOptionGroup;

public class CategoryOptionGroupFlow extends BaseIdentifiableObjectFlow {

    private final String CATEGORY_OPTION_GROUP_SET_KEY = "categoryOptionGroupSet";

    public static final Mapper<CategoryOptionGroup, CategoryOptionGroupFlow> MAPPER =
            new CategoryOptionGroupMapper();

    @Column(name = "shortName")
    String shortName;
    @Column(name = "dataDimensionType")
    String dataDimensionType;
    @Column(name = "publicAccess")
    String publicAccess;
    @Column(name = "displayShortName")
    String displayShortName;
    @Column(name = "externalAccess")
    boolean externalAccess;
    @Column(name = "dimensionItem")
    String dimensionItem;
    @Column(name = "dimensionItemType")
    String dimensionItemType;


    public CategoryOptionGroupFlow() {
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

    public static class CategoryOptionGroupMapper extends
            AbsMapper<CategoryOptionGroup, CategoryOptionGroupFlow> {


        @Override
        public CategoryOptionGroupFlow mapToDatabaseEntity(
                CategoryOptionGroup categoryOptionGroup) {
            if (categoryOptionGroup == null) {
                return null;
            }

            CategoryOptionGroupFlow categoryOptionGroupFlow = new CategoryOptionGroupFlow();
            categoryOptionGroupFlow.setUId(categoryOptionGroup.getUId());
            categoryOptionGroupFlow.setName(categoryOptionGroup.getName());
            categoryOptionGroupFlow.setLastUpdated(categoryOptionGroup.getLastUpdated());
            categoryOptionGroupFlow.setShortName(categoryOptionGroup.getShortName());
            categoryOptionGroupFlow.setDataDimensionType(
                    categoryOptionGroup.getDataDimensionType());
            categoryOptionGroupFlow.setPublicAccess(categoryOptionGroup.getPublicAccess());
            categoryOptionGroupFlow.setDisplayShortName(categoryOptionGroup.getDisplayShortName());
            categoryOptionGroupFlow.setExternalAccess(categoryOptionGroup.isExternalAccess());
            categoryOptionGroupFlow.setDimensionItem(categoryOptionGroup.getDimensionItem());
            categoryOptionGroupFlow.setDataDimensionType(
                    categoryOptionGroup.getDataDimensionType());
            categoryOptionGroupFlow.setAccess(categoryOptionGroup.getAccess());
            categoryOptionGroupFlow.setDimensionItemType(
                    categoryOptionGroup.getDimensionItemType());

            return categoryOptionGroupFlow;
        }

        @Override
        public CategoryOptionGroup mapToModel(CategoryOptionGroupFlow categoryOptionGroupFlow) {
            if (categoryOptionGroupFlow == null) {
                return null;
            }
            CategoryOptionGroup categoryOptionGroup = new CategoryOptionGroup();
            categoryOptionGroup.setUId(categoryOptionGroupFlow.getUId());
            categoryOptionGroup.setName(categoryOptionGroupFlow.getName());
            categoryOptionGroup.setLastUpdated(categoryOptionGroupFlow.getLastUpdated());
            categoryOptionGroup.setShortName(categoryOptionGroupFlow.getShortName());
            categoryOptionGroup.setDataDimensionType(
                    categoryOptionGroupFlow.getDataDimensionType());
            categoryOptionGroup.setPublicAccess(categoryOptionGroupFlow.getPublicAccess());
            categoryOptionGroup.setDisplayShortName(categoryOptionGroupFlow.getDisplayShortName());
            categoryOptionGroup.setExternalAccess(categoryOptionGroupFlow.isExternalAccess());
            categoryOptionGroup.setDimensionItem(categoryOptionGroupFlow.getDimensionItem());
            categoryOptionGroup.setDataDimensionType(
                    categoryOptionGroupFlow.getDataDimensionType());
            categoryOptionGroup.setAccess(categoryOptionGroupFlow.getAccess());
            categoryOptionGroup.setDimensionItemType(
                    categoryOptionGroupFlow.getDimensionItemType());

            return categoryOptionGroup;
        }

        @Override
        public Class<CategoryOptionGroup> getModelTypeClass() {
            return CategoryOptionGroup.class;
        }

        @Override
        public Class<CategoryOptionGroupFlow> getDatabaseEntityTypeClass() {
            return CategoryOptionGroupFlow.class;
        }
    }
}
