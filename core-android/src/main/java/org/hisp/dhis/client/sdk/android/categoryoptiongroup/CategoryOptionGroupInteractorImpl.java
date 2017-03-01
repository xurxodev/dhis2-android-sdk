package org.hisp.dhis.client.sdk.android.categoryoptiongroup;

import org.hisp.dhis.client.sdk.android.api.utils.DefaultOnSubscribe;
import org.hisp.dhis.client.sdk.core.categoryoptiongroup.CategoryOptionGroupController;
import org.hisp.dhis.client.sdk.core.categoryoptiongroup.CategoryOptionGroupFilters;
import org.hisp.dhis.client.sdk.core.categoryoptiongroup.CategoryOptionGroupService;
import org.hisp.dhis.client.sdk.models.category.CategoryOptionGroup;

import java.util.List;

import rx.Observable;

public class CategoryOptionGroupInteractorImpl implements CategoryOptionGroupInteractor {
    private final CategoryOptionGroupService mCategoryOptionGroupService;
    private final CategoryOptionGroupController mCategoryOptionGroupController;

    public CategoryOptionGroupInteractorImpl(
            CategoryOptionGroupService categoryOptionGroupService,
            CategoryOptionGroupController categoryOptionGroupController) {
        mCategoryOptionGroupService = categoryOptionGroupService;
        mCategoryOptionGroupController = categoryOptionGroupController;
    }

    @Override
    public Observable<List<CategoryOptionGroup>> pull(final CategoryOptionGroupFilters categoryOptionGroupFilters) {
        return Observable.create(new DefaultOnSubscribe<List<CategoryOptionGroup>>() {
            @Override
            public List<CategoryOptionGroup> call() {
                mCategoryOptionGroupController.pull(categoryOptionGroupFilters);
                return mCategoryOptionGroupService.list();
            }
        });
    }

    @Override
    public Observable<List<CategoryOptionGroup>> list() {
        return Observable.create(new DefaultOnSubscribe<List<CategoryOptionGroup>>() {
            @Override
            public List<CategoryOptionGroup> call() {
                return mCategoryOptionGroupService.list();
            }
        });
    }
}
