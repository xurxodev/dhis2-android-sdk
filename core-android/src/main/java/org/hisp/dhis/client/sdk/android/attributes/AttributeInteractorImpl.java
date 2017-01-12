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

import org.hisp.dhis.client.sdk.android.api.utils.DefaultOnSubscribe;
import org.hisp.dhis.client.sdk.core.attribute.AttributeController;
import org.hisp.dhis.client.sdk.core.attribute.AttributeService;
import org.hisp.dhis.client.sdk.models.attribute.Attribute;

import java.util.List;

import rx.Observable;

/**
 * Created by idelcano on 12/01/2017.
 */

public class AttributeInteractorImpl implements AttributeInteractor {
    private final AttributeService attributeService;
    private final AttributeController attributeController;

    public AttributeInteractorImpl(
            AttributeService attributeService,
            AttributeController attributeController) {
        this.attributeService = attributeService;
        this.attributeController = attributeController;
    }

    @Override
    public Observable<List<Attribute>> pull() {
        return Observable.create(new DefaultOnSubscribe<List<Attribute>>() {
            @Override
            public List<Attribute> call() {
                attributeController.pull();
                return attributeService.list();
            }
        });
    }

    @Override
    public Observable<Attribute> get(final long id) {
        return Observable.create(new DefaultOnSubscribe<Attribute>() {
            @Override
            public Attribute call() {
                return attributeService.get(id);
            }
        });
    }

    @Override
    public Observable<List<Attribute>> list() {
        return Observable.create(new DefaultOnSubscribe<List<Attribute>>() {
            @Override
            public List<Attribute> call() {
                return attributeService.list();
            }
        });
    }
}
