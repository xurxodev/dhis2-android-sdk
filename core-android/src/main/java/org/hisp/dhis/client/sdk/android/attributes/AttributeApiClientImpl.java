/*
 * Copyright (c) 2017.
 *
 * This file is part of QA App.
 *
 *  Health Network QIS App is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Health Network QIS App is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with Foobar.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.hisp.dhis.client.sdk.android.attributes;

import static org.hisp.dhis.client.sdk.android.api.network.NetworkUtils.getCollection;

import org.hisp.dhis.client.sdk.android.api.network.ApiResource;
import org.hisp.dhis.client.sdk.core.attribute.AttributeApiClient;
import org.hisp.dhis.client.sdk.core.common.Fields;
import org.hisp.dhis.client.sdk.core.common.network.ApiException;
import org.hisp.dhis.client.sdk.models.attribute.Attribute;
import org.joda.time.DateTime;

import java.util.List;
import java.util.Map;
import java.util.Set;

import retrofit2.Call;

/**
 * Created by idelcano on 12/01/2017.
 */

public class AttributeApiClientImpl implements AttributeApiClient {
    private final AttributeApiClientRetrofit attributeApiClientRetrofit;

    public AttributeApiClientImpl(AttributeApiClientRetrofit attributeApiClientRetrofit) {
        this.attributeApiClientRetrofit = attributeApiClientRetrofit;
    }

    @Override
    public List<Attribute> getAttributes(Fields fields, DateTime lastUpdated, Set<String> uids)
            throws ApiException {
        ApiResource<Attribute> apiResource = new ApiResource<Attribute>() {
            static final String IDENTIFIABLE_PROPERTIES =
                    "id,name,displayName,created,lastUpdated,code,valueType";

            @Override
            public String getResourceName() {
                return "attributes";
            }

            @Override
            public String getBasicProperties() {
                return "id";
            }

            @Override
            public String getAllProperties() {
                return IDENTIFIABLE_PROPERTIES;
            }

            @Override
            public String getDescendantProperties() {
                return IDENTIFIABLE_PROPERTIES; // end
            }

            @Override
            public Call<Map<String, List<Attribute>>> getEntities(
                    Map<String, String> queryMap, List<String> filters) throws ApiException {
                return attributeApiClientRetrofit.getAttributes(queryMap, filters);
            }
        };

        List<Attribute> attributes = getCollection(apiResource, fields, null, null);

        return attributes;
    }
}
