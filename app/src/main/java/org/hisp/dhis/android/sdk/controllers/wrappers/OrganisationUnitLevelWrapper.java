/*
 * Copyright (c) 2016.
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

package org.hisp.dhis.android.sdk.controllers.wrappers;

import com.fasterxml.jackson.databind.JsonNode;

import org.hisp.dhis.android.sdk.controllers.ApiEndpointContainer;
import org.hisp.dhis.android.sdk.controllers.DhisController;
import org.hisp.dhis.android.sdk.persistence.models.OrganisationUnitLevel;
import org.hisp.dhis.android.sdk.persistence.models.meta.DbOperation;
import org.hisp.dhis.android.sdk.utils.StringConverter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import retrofit.client.Response;
import retrofit.converter.ConversionException;

/**
 * Created by arrizabalaga on 9/02/16.
 */
public class OrganisationUnitLevelWrapper {

    public static List<OrganisationUnitLevel> deserialize(Response response) throws ConversionException, IOException {
        List<OrganisationUnitLevel> organisationUnitLevels = new ArrayList<>();
        String responseBodyString = new StringConverter().fromBody(response.getBody(), String.class);
        JsonNode node = DhisController.getInstance().getObjectMapper().
                readTree(responseBodyString);
        JsonNode organisationUnitsNode = node.get(ApiEndpointContainer.ORGANISATIONUNITLEVELS);

        if (organisationUnitsNode == null) { /* in case there are no items */
            return organisationUnitLevels;
        } else {
            Iterator<JsonNode> nodes = organisationUnitsNode.elements();
            while(nodes.hasNext()) {
                JsonNode indexNode = nodes.next();
                OrganisationUnitLevel item = DhisController.getInstance().getObjectMapper().
                        readValue(indexNode.toString(), OrganisationUnitLevel.class);
                organisationUnitLevels.add(item);
            }
        }
        return organisationUnitLevels;
    }

    public static List<DbOperation> getOperations(List<OrganisationUnitLevel> organisationUnitLevels) {
        List<DbOperation> operations = new ArrayList<>();

        for (OrganisationUnitLevel organisationUnitLevel : organisationUnitLevels) {
            operations.add(DbOperation.save(organisationUnitLevel));
        }
        return operations;
    }
}
