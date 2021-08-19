/*
 *  Copyright (c) 2004-2021, University of Oslo
 *  All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *  Redistributions of source code must retain the above copyright notice, this
 *  list of conditions and the following disclaimer.
 *
 *  Redistributions in binary form must reproduce the above copyright notice,
 *  this list of conditions and the following disclaimer in the documentation
 *  and/or other materials provided with the distribution.
 *  Neither the name of the HISP project nor the names of its contributors may
 *  be used to endorse or promote products derived from this software without
 *  specific prior written permission.
 *
 *  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 *  ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 *  WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 *  DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 *  ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 *  (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 *  LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 *  ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 *  (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 *  SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.hisp.dhis.android.core.trackedentity;

import org.hisp.dhis.android.core.arch.db.stores.internal.IdentifiableObjectStore;
import org.hisp.dhis.android.core.arch.repositories.children.internal.ChildrenAppender;
import org.hisp.dhis.android.core.arch.repositories.collection.internal.ReadOnlyIdentifiableCollectionRepositoryImpl;
import org.hisp.dhis.android.core.arch.repositories.filters.internal.BooleanFilterConnector;
import org.hisp.dhis.android.core.arch.repositories.filters.internal.EnumFilterConnector;
import org.hisp.dhis.android.core.arch.repositories.filters.internal.FilterConnectorFactory;
import org.hisp.dhis.android.core.arch.repositories.filters.internal.IntegerFilterConnector;
import org.hisp.dhis.android.core.arch.repositories.filters.internal.StringFilterConnector;
import org.hisp.dhis.android.core.arch.repositories.scope.RepositoryScope;
import org.hisp.dhis.android.core.enrollment.EnrollmentStatus;
import org.hisp.dhis.android.core.trackedentity.TrackedEntityInstanceFilterTableInfo.Columns;
import org.hisp.dhis.android.core.trackedentity.internal.TrackedEntityInstanceFilterFields;

import java.util.Map;

import javax.inject.Inject;

import dagger.Reusable;

@Reusable
public final class TrackedEntityInstanceFilterCollectionRepository
        extends ReadOnlyIdentifiableCollectionRepositoryImpl<TrackedEntityInstanceFilter,
        TrackedEntityInstanceFilterCollectionRepository> {

    @Inject
    TrackedEntityInstanceFilterCollectionRepository(
            final IdentifiableObjectStore<TrackedEntityInstanceFilter> store,
            final Map<String, ChildrenAppender<TrackedEntityInstanceFilter>> childrenAppenders,
            final RepositoryScope scope) {
        super(store, childrenAppenders, scope, new FilterConnectorFactory<>(scope,
                s -> new TrackedEntityInstanceFilterCollectionRepository(store, childrenAppenders, s)));
    }

    public StringFilterConnector<TrackedEntityInstanceFilterCollectionRepository> byProgram() {
        return cf.string(Columns.PROGRAM);
    }

    public StringFilterConnector<TrackedEntityInstanceFilterCollectionRepository> byDescription() {
        return cf.string(Columns.DESCRIPTION);
    }

    public IntegerFilterConnector<TrackedEntityInstanceFilterCollectionRepository> bySortOrder() {
        return cf.integer(Columns.SORT_ORDER);
    }

    public EnumFilterConnector<TrackedEntityInstanceFilterCollectionRepository, EnrollmentStatus> byEnrollmentStatus() {
        return cf.enumC(Columns.ENROLLMENT_STATUS);
    }

    public BooleanFilterConnector<TrackedEntityInstanceFilterCollectionRepository> byFollowUp() {
        return cf.bool(Columns.FOLLOW_UP);
    }

    public IntegerFilterConnector<TrackedEntityInstanceFilterCollectionRepository> byPeriodFrom() {
        return cf.integer(Columns.PERIOD_FROM);
    }

    public IntegerFilterConnector<TrackedEntityInstanceFilterCollectionRepository> byPeriodTo() {
        return cf.integer(Columns.PERIOD_TO);
    }

    public StringFilterConnector<TrackedEntityInstanceFilterCollectionRepository> byColor() {
        return cf.string(Columns.COLOR);
    }

    public StringFilterConnector<TrackedEntityInstanceFilterCollectionRepository> byIcon() {
        return cf.string(Columns.ICON);
    }

    public TrackedEntityInstanceFilterCollectionRepository withTrackedEntityInstanceEventFilters() {
        return cf.withChild(TrackedEntityInstanceFilterFields.EVENT_FILTERS);
    }
}