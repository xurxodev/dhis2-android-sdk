
package org.hisp.dhis.client.sdk.core.attribute;

import org.hisp.dhis.client.sdk.core.common.Fields;
import org.hisp.dhis.client.sdk.core.common.network.ApiException;
import org.hisp.dhis.client.sdk.core.common.persistence.DbOperation;
import org.hisp.dhis.client.sdk.core.common.persistence.DbOperationImpl;
import org.hisp.dhis.client.sdk.core.common.persistence.TransactionManager;
import org.hisp.dhis.client.sdk.core.common.preferences.DateType;
import org.hisp.dhis.client.sdk.core.common.preferences.LastUpdatedPreferences;
import org.hisp.dhis.client.sdk.core.common.preferences.ResourceType;
import org.hisp.dhis.client.sdk.core.systeminfo.SystemInfoController;
import org.hisp.dhis.client.sdk.core.user.UserApiClient;
import org.hisp.dhis.client.sdk.models.attribute.Attribute;
import org.hisp.dhis.client.sdk.utils.Logger;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by idelcano on 12/01/2017.
 */

public class AttributeControllerImp implements AttributeController {

    private final SystemInfoController systemInfoController;
    private final AttributeApiClient attributeApiClient;
    private final AttributeStore attributeStore;

    /* Api clients */
    private final UserApiClient userApiClient;

    /* Preferences */
    private final LastUpdatedPreferences lastUpdatedPreferences;

    /* Utilities */
    private final TransactionManager transactionManager;
    private final Logger logger;


    public AttributeControllerImp(SystemInfoController systemInfoController,
            AttributeStore attributeStore, UserApiClient userApiClient,
            AttributeApiClient attributeApiClient, LastUpdatedPreferences lastUpdatedPreferences,
            TransactionManager transactionManager, Logger logger) {
        this.systemInfoController = systemInfoController;
        this.attributeStore = attributeStore;
        this.userApiClient = userApiClient;
        this.attributeApiClient = attributeApiClient;
        this.lastUpdatedPreferences = lastUpdatedPreferences;
        this.transactionManager = transactionManager;
        this.logger = logger;
    }

    @Override
    public void pull() throws ApiException {

        DateTime serverTime = systemInfoController.getSystemInfo().getServerDate();
        List<Attribute> allExistingOptionSets = attributeApiClient
                .getAttributes(Fields.ALL, null, null);

        List<DbOperation> dbOperations = new ArrayList<>();
        for (Attribute attribute : allExistingOptionSets) {
            dbOperations.add(DbOperationImpl.with(attributeStore)
                    .save(attribute));
        }
        transactionManager.transact(dbOperations);

        lastUpdatedPreferences.save(ResourceType.EVENTS, DateType.SERVER, serverTime);
    }
}