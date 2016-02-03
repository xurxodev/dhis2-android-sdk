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

package org.hisp.dhis.android.sdk.persistence.models;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import org.hisp.dhis.android.sdk.persistence.Dhis2Database;


@Table(databaseName = Dhis2Database.NAME)
public class OrganisationUnitGroup extends BaseModel {

    @Column(name = "organisationUnitId")
    @PrimaryKey
    String organisationUnitId;

    @Column(name = "organisationUnitGroupId")
    @PrimaryKey
    String organisationUnitGroupId;

    public String getOrganisationUnitId() {
        return organisationUnitId;
    }

    public void setOrganisationUnitId(String organisationUnitId) {
        this.organisationUnitId = organisationUnitId;
    }

    public String getOrganisationUnitGroupId() {
        return organisationUnitGroupId;
    }

    public void setOrganisationUnitGroupId(String organisationUnitGroupId) {
        this.organisationUnitGroupId = organisationUnitGroupId;
    }
}
