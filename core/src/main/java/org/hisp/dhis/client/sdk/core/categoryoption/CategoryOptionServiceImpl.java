
package org.hisp.dhis.client.sdk.core.categoryoption;

import org.hisp.dhis.client.sdk.models.category.CategoryOption;
import org.hisp.dhis.client.sdk.utils.Preconditions;

import java.util.List;

public class CategoryOptionServiceImpl implements CategoryOptionService {
    private final CategoryOptionStore categoryOptionStore;

    public CategoryOptionServiceImpl(CategoryOptionStore categoryOptionStore) {
        this.categoryOptionStore = categoryOptionStore;
    }

    @Override
    public List<CategoryOption> list() {
        return categoryOptionStore.queryAll();
    }

    @Override
    public CategoryOption get(long id) {
        return categoryOptionStore.queryById(id);
    }

    @Override
    public boolean remove(CategoryOption object) {
        Preconditions.isNull(object, "Object must not be null");
        return categoryOptionStore.delete(object);
    }

    @Override
    public boolean save(CategoryOption object) {
        Preconditions.isNull(object, "Object must not be null");
        return categoryOptionStore.save(object);
    }
}