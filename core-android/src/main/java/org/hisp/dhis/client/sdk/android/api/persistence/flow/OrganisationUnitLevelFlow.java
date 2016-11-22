package org.hisp.dhis.client.sdk.android.api.persistence.flow;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.Table;

import org.hisp.dhis.client.sdk.android.api.persistence.DbDhis;

/**
 * Created by idelcano on 14/11/2016.
 */

/**
 * This class will be disappeared soon.
 * This is a SDK Pojo and is created to fix the Queries,
 * is in the app side and with hardcoded methods.
 * It makes the project compile and centralized the necessary methods
 * and necessary sdk new Pojos..
 */
@Table(database = DbDhis.class)
public class OrganisationUnitLevelFlow extends BaseIdentifiableObjectFlow {
    @Column(name = "level")
    int level;

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public OrganisationUnitLevelFlow() {
    }


    public String getUid() {
        //return getUId();
        return null;
    }

}
