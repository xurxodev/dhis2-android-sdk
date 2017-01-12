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

package org.hisp.dhis.client.sdk.core.common.controllers;

import org.hisp.dhis.client.sdk.core.common.network.ApiException;
import org.hisp.dhis.client.sdk.core.common.preferences.ResourceType;
import org.hisp.dhis.client.sdk.models.common.base.Model;

/**
 * Created by idelcano on 12/01/2017.
 */

public class AbsSimpleStrategyController<T extends Model> {
    private static final int EXPIRATION_THRESHOLD = 64;

    protected final ResourceType resourceType;
    protected Model objectStore;

    protected AbsSimpleStrategyController(ResourceType resourceType,
            Model objectStore) {
        this.resourceType = resourceType;
        this.objectStore = objectStore;
    }

    public final void pull() throws ApiException {

        /* if we don't have objects with given uids in place, we have
        to force a pull even if strategy is set to be DEFAULT */
    }
}
