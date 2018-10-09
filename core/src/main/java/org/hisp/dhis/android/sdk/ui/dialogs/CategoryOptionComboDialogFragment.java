/*
 *  Copyright (c) 2016, University of Oslo
 *  * All rights reserved.
 *  *
 *  * Redistribution and use in source and binary forms, with or without
 *  * modification, are permitted provided that the following conditions are met:
 *  * Redistributions of source code must retain the above copyright notice, this
 *  * list of conditions and the following disclaimer.
 *  *
 *  * Redistributions in binary form must reproduce the above copyright notice,
 *  * this list of conditions and the following disclaimer in the documentation
 *  * and/or other materials provided with the distribution.
 *  * Neither the name of the HISP project nor the names of its contributors may
 *  * be used to endorse or promote products derived from this software without
 *  * specific prior written permission.
 *  *
 *  * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 *  * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 *  * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 *  * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 *  * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 *  * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 *  * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 *  * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 *  * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 *  * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 */

package org.hisp.dhis.android.sdk.ui.dialogs;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.View;

import com.raizlabs.android.dbflow.structure.Model;

import org.hisp.dhis.android.sdk.R;
import org.hisp.dhis.android.sdk.controllers.metadata.MetaDataController;
import org.hisp.dhis.android.sdk.persistence.loaders.DbLoader;
import org.hisp.dhis.android.sdk.persistence.loaders.Query;
import org.hisp.dhis.android.sdk.persistence.models.CategoryCombo;
import org.hisp.dhis.android.sdk.persistence.models.CategoryOptionCombo;
import org.hisp.dhis.android.sdk.persistence.models.OrganisationUnitProgramRelationship;
import org.hisp.dhis.android.sdk.persistence.models.Program;
import org.hisp.dhis.android.sdk.ui.dialogs.AutoCompleteDialogAdapter.OptionAdapterValue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CategoryOptionComboDialogFragment extends AutoCompleteDialogFragment
        implements LoaderManager.LoaderCallbacks<List<OptionAdapterValue>> {
    public static final int ID = 921346;
    private static final int LOADER_ID = 1;
    private static final String PROGRAMID = "programid";
    private static final String CATEGORY_NAME = "categoryname";
    private static String mCategoryName="";


    public static CategoryOptionComboDialogFragment newInstance(OnOptionSelectedListener listener,
                                                                String programID, String categoryName) {
        CategoryOptionComboDialogFragment fragment = new CategoryOptionComboDialogFragment();
        Bundle args = new Bundle();
        args.putString(PROGRAMID, programID);
        args.putString(CATEGORY_NAME, categoryName);
        mCategoryName=categoryName;
        fragment.setArguments(args);
        fragment.setOnOptionSetListener(listener);
        return fragment;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setDialogLabel(mCategoryName);
        setDialogId(ID);
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(LOADER_ID, getArguments(), this);
    }

    @Override
    public Loader<List<OptionAdapterValue>> onCreateLoader(int id, Bundle args) {
        if (LOADER_ID == id && isAdded()) {
            String programid = args.getString(PROGRAMID);
            List<Class<? extends Model>> modelsToTrack = new ArrayList<>();
            modelsToTrack.add(Program.class);
            modelsToTrack.add(OrganisationUnitProgramRelationship.class);
            return new DbLoader<>(
                    getActivity().getBaseContext(), modelsToTrack, new CategoryQuery(programid)
            );
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<List<OptionAdapterValue>> loader,
                               List<OptionAdapterValue> data) {
        if (LOADER_ID == loader.getId()) {
            getAdapter().swapData(data);

            if (MetaDataController.isDataLoaded(getActivity()))
                mProgressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<OptionAdapterValue>> loader) {
        getAdapter().swapData(null);
    }

    static class CategoryQuery implements Query<List<OptionAdapterValue>> {
        private final String mProgramId;

        public CategoryQuery(String programID) {
            mProgramId = programID;
        }

        @Override
        public List<OptionAdapterValue> query(Context context) {
            List<CategoryOptionCombo> categoryOptionCombos = MetaDataController
                    .getCategoryOptionComboFromProgram(
                            mProgramId);
            List<OptionAdapterValue> values = new ArrayList<>();
            if (categoryOptionCombos != null && !categoryOptionCombos.isEmpty()) {
                for (CategoryOptionCombo categoryOptionCombo : categoryOptionCombos) {
                    values.add(new OptionAdapterValue(categoryOptionCombo.getUid(), categoryOptionCombo.getName()));
                }
            }
            Collections.sort(values);
            return values;
        }
    }
}
