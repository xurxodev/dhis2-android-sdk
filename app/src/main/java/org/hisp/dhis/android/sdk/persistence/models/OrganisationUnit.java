/*
 *  Copyright (c) 2016, University of Oslo
 *  * All rights reserved.
 *  *
 *  * Redistribution and use in source and binary forms, with or without
 *  * modification, are permitted provided that the following conditions are met:
 *  * Redistributions of source code must retain the above copyright notice, this
 *  * list of conditions and the following disclaimer.
 *  *
 *  * Redistributions in binary form must reproduce the above copyright notice,
 *  * this list of conditions and the following disclaimer in the documentation
 *  * and/or other materials provided with the distribution.
 *  * Neither the name of the HISP project nor the names of its contributors may
 *  * be used to endorse or promote products derived from this software without
 *  * specific prior written permission.
 *  *
 *  * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 *  * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 *  * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 *  * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 *  * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 *  * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 *  * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 *  * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 *  * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 *  * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 */

package org.hisp.dhis.android.sdk.persistence.models;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import org.hisp.dhis.android.sdk.controllers.metadata.MetaDataController;
import org.hisp.dhis.android.sdk.persistence.Dhis2Database;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Simen Skogly Russnes on 17.02.15.
 */
@Table(databaseName = Dhis2Database.NAME)
public class OrganisationUnit extends BaseModel {

    @JsonProperty("id")
    @Column(name = "id")
    @PrimaryKey
    String id;

    @JsonProperty("label")
    @Column(name = "label")
    String label;

    @JsonProperty("level")
    @Column(name = "level")
    int level;

    @JsonProperty("parent")
    @Column(name = "parent")
    String parent;

    @JsonProperty("uuid")
    @Column(name = "uuid")
    String uuid;

    @JsonProperty("lastUpdated")
    @Column(name = "lastUpdated")
    String lastUpdated;

    @JsonProperty("created")
    @Column(name = "created")
    String created;

    @JsonProperty("name")
    @Column(name = "name")
    String name;

    @JsonProperty("user")
    @Column(name = "user")
    String user;

    @JsonProperty("shortName")
    @Column(name = "shortName")
    String shortName;

    @JsonProperty("displayName")
    @Column(name = "displayName")
    String displayName;

    @JsonProperty("displayShortName")
    @Column(name = "displayShortName")
    String displayShortName;

    @JsonProperty("externalAccess")
    @Column(name = "externalAccess")
    Boolean externalAccess;

    @JsonProperty("path")
    @Column(name = "path")
    String path;

    @JsonProperty("featureType")
    @Column(name = "featureType")
    String featureType;

    @JsonProperty("openingDate")
    @Column(name = "openingDate")
    String openingDate;

    @JsonProperty("dimensionItem")
    @Column(name = "dimensionItem")
    String dimensionItem;

    @JsonProperty("attributeValues")
    List<OrganisationUnitAttributeValue> attributeValues;

    @JsonProperty("programs")
    List<String> programs;

    @JsonProperty("ancestors")
    List<String> ancestors;

    @JsonProperty("children")
    List<String> children;

    public OrganisationUnit() {
    }

    @JsonAnySetter
    public void handleUnknown(String key, Object value) {
        // do something: put to a Map; log a warning, whatever
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public String getUser() {
        return user;
    }

    @JsonProperty("user")
    public void setUser(Map<String,Object> user) {
        this.user = ((String) user.get("id"));
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public List<String> getPrograms() {
        return programs;
    }

    @JsonProperty("programs")
    public void setPrograms(List<Map<String, Object>> programs) {
        List<String> tempPrograms = new ArrayList<>();
        for (Map<String, Object> program : programs) {
            tempPrograms.add((String) program.get("id"));
        }
        this.programs = tempPrograms;
    }

    public List<String> getChildren() {
        return children;
    }

    @JsonProperty("children")
    public void setChildren(List<Map<String, Object>> childs) {
        List<String> tempChilds = new ArrayList<>();
        for (Map<String, Object> child : childs) {
            tempChilds.add((String) child.get("id"));
        }
        this.children = tempChilds;
    }
    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public List<String> getAncestors() {
        return ancestors;
    }

    @JsonProperty("ancestors")
    public void setAncestors(List<Map<String, Object>> organisationUnits) {
        List<String> tempAncestors = new ArrayList<>();
        for (Map<String, Object> organisationUnit : organisationUnits) {
            tempAncestors.add((String) organisationUnit.get("id"));
        }
        this.ancestors = tempAncestors;
    }


    public List<OrganisationUnitAttributeValue> getAttributeValues() {
        if (attributeValues == null) {
            attributeValues = MetaDataController.getOrganisationUnitAttributeValues(this);
        }
        return attributeValues;
    }

    public OrganisationUnitAttributeValue getAttributeValue(String attributeId){
        if (getAttributeValues() == null) return null;
        for (OrganisationUnitAttributeValue attributeValue: getAttributeValues()){
            if (attributeValue.getAttribute().equals(attributeId))
                return attributeValue;
        }
        return null;
    }

    public OrganisationUnitAttributeValue getAttributeValue(long id){
        return MetaDataController.getOrganisationUnitAttributeValue(id);
    }
}
