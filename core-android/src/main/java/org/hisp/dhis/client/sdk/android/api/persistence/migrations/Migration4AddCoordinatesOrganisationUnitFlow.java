package org.hisp.dhis.client.sdk.android.api.persistence.migrations;

import com.raizlabs.android.dbflow.annotation.Migration;
import com.raizlabs.android.dbflow.sql.SQLiteType;
import com.raizlabs.android.dbflow.sql.migration.AlterTableMigration;

import org.hisp.dhis.client.sdk.android.api.persistence.DbDhis;
import org.hisp.dhis.client.sdk.android.api.persistence.flow.OrganisationUnitFlow;

@Migration(version = 4, database = DbDhis.class)
public class Migration4AddCoordinatesOrganisationUnitFlow extends
        AlterTableMigration<OrganisationUnitFlow> {
    public Migration4AddCoordinatesOrganisationUnitFlow(
            Class<OrganisationUnitFlow> table) {
        super(table);
        addColumn(SQLiteType.TEXT, "coordinates");
    }

}