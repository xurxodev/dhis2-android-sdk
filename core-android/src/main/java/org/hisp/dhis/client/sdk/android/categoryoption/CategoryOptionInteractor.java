package org.hisp.dhis.client.sdk.android.categoryoption;

import org.hisp.dhis.client.sdk.models.category.CategoryOption;

import java.util.List;

import rx.Observable;

public interface CategoryOptionInteractor {
    Observable<List<CategoryOption>> pull();
}
