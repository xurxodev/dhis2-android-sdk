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

package org.hisp.dhis.android.core.trackedentity.internal;

import androidx.annotation.NonNull;

import org.hisp.dhis.android.core.arch.db.stores.internal.IdentifiableObjectStore;
import org.hisp.dhis.android.core.enrollment.Enrollment;
import org.hisp.dhis.android.core.enrollment.EnrollmentInternalAccessor;
import org.hisp.dhis.android.core.event.Event;
import org.hisp.dhis.android.core.organisationunit.OrganisationUnit;
import org.hisp.dhis.android.core.trackedentity.TrackedEntityInstance;
import org.hisp.dhis.android.core.trackedentity.TrackedEntityInstanceInternalAccessor;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import dagger.Reusable;

@Reusable
class TrackedEntityInstanceUidHelperImpl implements TrackedEntityInstanceUidHelper {

    private final IdentifiableObjectStore<OrganisationUnit> organisationUnitStore;

    @Inject
    TrackedEntityInstanceUidHelperImpl(@NonNull IdentifiableObjectStore<OrganisationUnit> organisationUnitStore) {
        this.organisationUnitStore = organisationUnitStore;
    }

    @Override
    public Set<String> getMissingOrganisationUnitUids(Collection<TrackedEntityInstance> trackedEntityInstances) {
        Set<String> uids = new HashSet<>();
        for (TrackedEntityInstance tei: trackedEntityInstances) {
            if (tei.organisationUnit() != null) {
                uids.add(tei.organisationUnit());
            }
            List<Enrollment> enrollments = TrackedEntityInstanceInternalAccessor.accessEnrollments(tei);
            addEnrollmentsUids(enrollments, uids);
        }
        uids.removeAll(organisationUnitStore.selectUids());
        return uids;
    }

    private void addEnrollmentsUids(List<Enrollment> enrollments, Set<String> uids) {
        if (enrollments != null) {
            for (Enrollment enrollment: enrollments) {
                if (enrollment.organisationUnit() != null) {
                    uids.add(enrollment.organisationUnit());
                }
                addEventsUids(EnrollmentInternalAccessor.accessEvents(enrollment), uids);
            }
        }
    }

    private void addEventsUids(List<Event> events, Set<String> uids) {
        if (events != null) {
            for (Event event: events) {
                if (event.organisationUnit() != null) {
                    uids.add(event.organisationUnit());
                }
            }
        }
    }
}
