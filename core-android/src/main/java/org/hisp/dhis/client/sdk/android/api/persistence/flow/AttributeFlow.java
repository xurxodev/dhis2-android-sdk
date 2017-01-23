package org.hisp.dhis.client.sdk.android.api.persistence.flow;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.Table;

import org.hisp.dhis.client.sdk.android.api.persistence.DbDhis;
import org.hisp.dhis.client.sdk.android.common.AbsMapper;
import org.hisp.dhis.client.sdk.android.common.Mapper;
import org.hisp.dhis.client.sdk.models.attribute.Attribute;


@Table(database = DbDhis.class)
public class AttributeFlow extends BaseModelFlow {
    public static final Mapper<Attribute, AttributeFlow> MAPPER = new AttributeMapper();

    public AttributeFlow() {
    }

    @Column(name = "code")
    String code;

    @Column(name = "valueType")
    String valueType;

    @Column(name = "uId")
    String UId;

    @Column(name = "lastUpdated")
    String lastUpdated;

    @Column(name = "created")
    String created;

    @Column(name = "displayName")
    String displayName;

    @Column(name = "name")
    String name;

    public String getCode() {
        //return code;
        return code;
    }

    public String getValueType() {
        return valueType;
    }

    public void setValueType(String valueType) {
        this.valueType = valueType;
    }

    public String getUId() {
        return UId;
    }

    public void setUId(String UId) {
        this.UId = UId;
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

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCode(String code) {
        this.code = code;
    }


    private static class AttributeMapper extends AbsMapper<Attribute, AttributeFlow> {

        @Override
        public AttributeFlow mapToDatabaseEntity(Attribute attribute) {
            if (attribute == null) {
                return null;
            }

            AttributeFlow attributeFlow = new AttributeFlow();
            attributeFlow.setCode(attribute.getCode());
            attributeFlow.setUId(attribute.getUId());
            attributeFlow.setName(attribute.getName());
            attributeFlow.setDisplayName(attribute.getDisplayName());
            attributeFlow.setCreated(attribute.getCreated());
            attributeFlow.setLastUpdated(attribute.getLastUpdated());
            attributeFlow.setValueType(attribute.getValueType());

            return attributeFlow;
        }

        @Override
        public Attribute mapToModel(AttributeFlow attributeFlow) {
            if (attributeFlow == null) {
                return null;
            }

            Attribute attribute = new Attribute();
            attribute.setCode(attributeFlow.getCode());
            attribute.setId(attributeFlow.getId());
            attribute.setUId(attributeFlow.getUId());
            attribute.setName(attributeFlow.getName());
            attribute.setDisplayName(attributeFlow.getDisplayName());
            attribute.setCreated(attributeFlow.getCreated());
            attribute.setLastUpdated(attributeFlow.getLastUpdated());
            attribute.setValueType(attributeFlow.getValueType());

            return attribute;
        }

        @Override
        public Class<Attribute> getModelTypeClass() {
            return Attribute.class;
        }

        @Override
        public Class<AttributeFlow> getDatabaseEntityTypeClass() {
            return AttributeFlow.class;
        }
    }
}
