
package org.hisp.dhis.client.sdk.android.attributes;

import org.hisp.dhis.client.sdk.android.api.utils.DefaultOnSubscribe;
import org.hisp.dhis.client.sdk.core.attribute.AttributeController;
import org.hisp.dhis.client.sdk.core.attribute.AttributeService;
import org.hisp.dhis.client.sdk.models.attribute.Attribute;

import java.util.List;

import rx.Observable;


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
