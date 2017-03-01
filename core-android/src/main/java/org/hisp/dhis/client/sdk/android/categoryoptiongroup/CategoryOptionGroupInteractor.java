package org.hisp.dhis.client.sdk.android.categoryoptiongroup;

import org.hisp.dhis.client.sdk.models.category.CategoryOptionGroup;

import java.util.List;

import rx.Observable;

public interface CategoryOptionGroupInteractor {
    Observable<List<CategoryOptionGroup>> pull();

    Observable<List<CategoryOptionGroup>> list();
}
