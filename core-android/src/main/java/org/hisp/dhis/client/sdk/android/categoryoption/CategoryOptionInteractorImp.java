package org.hisp.dhis.client.sdk.android.categoryoption;

import org.hisp.dhis.client.sdk.android.api.utils.DefaultOnSubscribe;
import org.hisp.dhis.client.sdk.core.categoryoption.CategoryOptionController;
import org.hisp.dhis.client.sdk.core.categoryoption.CategoryOptionService;
import org.hisp.dhis.client.sdk.models.category.CategoryOption;

import java.util.List;

import rx.Observable;

public class CategoryOptionInteractorImp implements CategoryOptionInteractor {
    private final CategoryOptionService categoryOptionService;
    private final CategoryOptionController categoryOptionController;

    public CategoryOptionInteractorImp(CategoryOptionService categoryOptionService,
            CategoryOptionController categoryOptionController) {
        this.categoryOptionController = categoryOptionController;
        this.categoryOptionService = categoryOptionService;
    }

    @Override
    public Observable<List<CategoryOption>> pull() {
        return Observable.create(new DefaultOnSubscribe<List<CategoryOption>>() {
            @Override
            public List<CategoryOption> call() {
                categoryOptionController.pull();
                return categoryOptionService.list();
            }
        });
    }
}
