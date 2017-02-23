/*
 * Copyright (c) 2016, University of Oslo
 *
 * All rights reserved.
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 * Neither the name of the HISP project nor the names of its contributors may
 * be used to endorse or promote products derived from this software without
 * specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.hisp.dhis.client.sdk.android.api.persistence.flow;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.Table;

import org.hisp.dhis.client.sdk.android.api.persistence.DbDhis;
import org.hisp.dhis.client.sdk.android.common.AbsMapper;
import org.hisp.dhis.client.sdk.android.common.Mapper;
import org.hisp.dhis.client.sdk.models.attribute.Attribute;
import org.hisp.dhis.client.sdk.models.category.CategoryOption;

@Table(database = DbDhis.class)
public final class CategoryOptionFlow extends BaseModelFlow {

    public static final Mapper<CategoryOption, CategoryOptionFlow> MAPPER = new CategoryOptionMapper();

    public CategoryOptionFlow() {
    }

    @Column(name = "code")
    String code;

    @Column(name = "uId")
    String UId;

    @Column(name = "lastUpdated")
    String lastUpdated;

    @Column(name = "created")
    String created;

    @Column(name = "displayName")
    String displayName;

    @Column(name = "shortName")
    String shortName;

    public String getCode() {
        //return code;
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

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }


    public static class CategoryOptionMapper  extends AbsMapper<CategoryOption, CategoryOptionFlow> {
        @Override
        public CategoryOptionFlow mapToDatabaseEntity(CategoryOption categoryOption) {
            if (categoryOption == null) {
                return null;
            }

            CategoryOptionFlow categoryOptionFlow = new CategoryOptionFlow();
            categoryOptionFlow.setCode(categoryOption.getCode());
            categoryOptionFlow.setUId(categoryOption.getUId());
            categoryOptionFlow.setShortName(categoryOption.getShortName());
            categoryOptionFlow.setDisplayName(categoryOption.getDisplayName());
            categoryOptionFlow.setCreated(categoryOption.getCreated());
            categoryOptionFlow.setLastUpdated(categoryOption.getLastUpdated());

            return categoryOptionFlow;
        }

        @Override
        public CategoryOption mapToModel(CategoryOptionFlow categoryOptionFlow) {
            if (categoryOptionFlow == null) {
                return null;
            }

            CategoryOption categoryOption = new CategoryOption();
            categoryOption.setCode(categoryOptionFlow.getCode());
            categoryOption.setId(categoryOptionFlow.getId());
            categoryOption.setUId(categoryOptionFlow.getUId());
            categoryOption.setShortName(categoryOptionFlow.getShortName());
            categoryOption.setDisplayName(categoryOptionFlow.getDisplayName());
            categoryOption.setCreated(categoryOptionFlow.getCreated());
            categoryOption.setLastUpdated(categoryOptionFlow.getLastUpdated());

            return categoryOption;
        }

        @Override
        public Class<CategoryOption> getModelTypeClass() {
            return CategoryOption.class;
        }

        @Override
        public Class<CategoryOptionFlow> getDatabaseEntityTypeClass() {
            return CategoryOptionFlow.class;
        }
    }
}
