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

package org.hisp.dhis.client.sdk.android.api.persistence;

import com.raizlabs.android.dbflow.annotation.Database;
import com.raizlabs.android.dbflow.annotation.Migration;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.migration.BaseMigration;
import com.raizlabs.android.dbflow.structure.ModelAdapter;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;

import org.hisp.dhis.client.sdk.android.api.persistence.flow.AttributeFlow;

@Database(
        name = DbDhis.NAME, version = DbDhis.VERSION
)
public final class DbDhis {
    public static final String NAME = "dhis";
    public static final int VERSION = 3;

    @Migration(version = 3, database = DbDhis.class)
    public static class Migration3 extends BaseMigration {

        @Override
        public void migrate(DatabaseWrapper database) {
            ModelAdapter myAdapter = FlowManager.getModelAdapter(AttributeFlow.class);

            //Create temporal table
            String sql=myAdapter.getCreationQuery();
            sql=sql.replace("AttributeFlow", "AttributeFlow_temp");
            database.execSQL(sql);

            //Insert the data in temporal table
            String sqlCopy="INSERT INTO AttributeFlow_temp (id, code, valueType, attributeUId, lastUpdated, created, displayName, name) SELECT id, code, valueType, uId, lastUpdated, created, displayName, name FROM AttributeFlow";
            database.execSQL(sqlCopy);

            //Replace old table by new table with the new column name.
            database.execSQL("DROP TABLE IF EXISTS AttributeFlow");
            database.execSQL("ALTER TABLE AttributeFlow_temp RENAME TO AttributeFlow");
        }
    }
}
