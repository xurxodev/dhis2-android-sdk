package org.hisp.dhis.android.sdk.persistence.migrations.version8;

import com.raizlabs.android.dbflow.annotation.Migration;
import com.raizlabs.android.dbflow.sql.migration.AlterTableMigration;

import org.hisp.dhis.android.sdk.persistence.Dhis2Database;
import org.hisp.dhis.android.sdk.persistence.models.OrganisationUnit;

@Migration(version = 8, databaseName = Dhis2Database.NAME)
public class Version8AddOrganisationUnitColumns extends AlterTableMigration<OrganisationUnit> {

    public Version8AddOrganisationUnitColumns(Class<OrganisationUnit> table) {
        super(OrganisationUnit.class);
    }

    public Version8AddOrganisationUnitColumns() {
        super(OrganisationUnit.class);
    }

    @Override
    public void onPreMigrate() {
        addColumn(String.class, "uuid");
        addColumn(String.class, "name");
        addColumn(String.class, "shortName");
        addColumn(String.class, "displayName");
        addColumn(String.class, "displayShortName");
        addColumn(Boolean.class, "externalAccess");
        addColumn(String.class, "path");
        addColumn(String.class, "featureType");
        addColumn(String.class, "created");
        addColumn(String.class, "lastUpdated");
        addColumn(String.class, "openingDate");
        addColumn(String.class, "dimensionItem");
        addColumn(String.class, "user");
    }

}
