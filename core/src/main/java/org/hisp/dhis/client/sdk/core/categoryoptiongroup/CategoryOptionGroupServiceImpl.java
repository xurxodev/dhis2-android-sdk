package org.hisp.dhis.client.sdk.core.categoryoptiongroup;

import org.hisp.dhis.client.sdk.models.category.CategoryOptionGroup;
import org.hisp.dhis.client.sdk.utils.Preconditions;

import java.util.List;

public class CategoryOptionGroupServiceImpl implements CategoryOptionGroupService {
    private final CategoryOptionGroupStore mCategoryOptionGroupStore;

    public CategoryOptionGroupServiceImpl(
            CategoryOptionGroupStore categoryOptionGroupStore) {
        mCategoryOptionGroupStore = categoryOptionGroupStore;
    }

    @Override
    public CategoryOptionGroup get(long id) {
        return mCategoryOptionGroupStore.queryById(id);
    }

    @Override
    public List<CategoryOptionGroup> list() {
        return mCategoryOptionGroupStore.queryAll();
    }

    @Override
    public boolean remove(CategoryOptionGroup object) {
        Preconditions.isNull(object, "Object must not be null");
        return mCategoryOptionGroupStore.delete(object);
    }

    @Override
    public boolean save(CategoryOptionGroup object) {
        Preconditions.isNull(object, "Object must not be null");
        return mCategoryOptionGroupStore.save(object);
    }
}
