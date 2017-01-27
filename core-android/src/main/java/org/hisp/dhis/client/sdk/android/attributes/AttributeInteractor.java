
package org.hisp.dhis.client.sdk.android.attributes;


import org.hisp.dhis.client.sdk.models.attribute.Attribute;

import java.util.List;

import rx.Observable;


public interface AttributeInteractor {

        Observable<Attribute> get(long id);

        Observable<List<Attribute>> list();

        Observable<List<Attribute>> pull();

}
