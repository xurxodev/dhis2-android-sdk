package org.hisp.dhis.android.sdk.persistence.migrations.version9;

import com.raizlabs.android.dbflow.annotation.Migration;
import com.raizlabs.android.dbflow.sql.migration.AlterTableMigration;

import org.hisp.dhis.android.sdk.persistence.Dhis2Database;
import org.hisp.dhis.android.sdk.persistence.models.UserAccount;

@Migration(version = 9, databaseName = Dhis2Database.NAME)
public class Version9AddUsernameColumn extends AlterTableMigration<UserAccount> {

    public Version9AddUsernameColumn(Class<UserAccount> table) {
        super(UserAccount.class);
    }

    public Version9AddUsernameColumn() {
        super(UserAccount.class);
    }

    @Override
    public void onPreMigrate() {
        addColumn(String.class, "username");
    }

}
