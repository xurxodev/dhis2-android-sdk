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

package org.hisp.dhis.client.sdk.core.attribute;

import org.hisp.dhis.client.sdk.models.attribute.Attribute;
import org.hisp.dhis.client.sdk.utils.Preconditions;

import java.util.List;

/**
 * Created by idelcano on 12/01/2017.
 */

public class AttributeServiceImpl implements AttributeService {
    private final AttributeStore attributeStore;

    public AttributeServiceImpl(AttributeStore attributeStore) {
        this.attributeStore = attributeStore;
    }

    @Override
    public List<Attribute> list() {
        return attributeStore.queryAll();
    }

    @Override
    public Attribute get(long id) {
        return attributeStore.queryById(id);
    }

    @Override
    public boolean remove(Attribute object) {
        Preconditions.isNull(object, "Object must not be null");
        return attributeStore.delete(object);
    }

    @Override
    public boolean save(Attribute object) {
        Preconditions.isNull(object, "Object must not be null");
        return attributeStore.save(object);
    }
}