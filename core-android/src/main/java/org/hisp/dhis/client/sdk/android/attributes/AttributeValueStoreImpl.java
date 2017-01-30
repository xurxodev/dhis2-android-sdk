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


import org.hisp.dhis.client.sdk.android.api.persistence.flow.AttributeValueFlow;
import org.hisp.dhis.client.sdk.android.common.AbsStore;
import org.hisp.dhis.client.sdk.core.attribute.AttributeValueStore;
import org.hisp.dhis.client.sdk.core.common.persistence.TransactionManager;
import org.hisp.dhis.client.sdk.models.attribute.AttributeValue;

public class AttributeValueStoreImpl extends AbsStore<AttributeValue, AttributeValueFlow> implements
        AttributeValueStore {

    private final TransactionManager transactionManager;

    public AttributeValueStoreImpl(TransactionManager transactionManager) {
        super(AttributeValueFlow.MAPPER);

        this.transactionManager = transactionManager;
    }
}