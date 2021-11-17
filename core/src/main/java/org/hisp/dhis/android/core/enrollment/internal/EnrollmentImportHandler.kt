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
package org.hisp.dhis.android.core.enrollment.internal

import dagger.Reusable
import java.util.*
import javax.inject.Inject
import org.hisp.dhis.android.core.arch.db.stores.internal.StoreUtils.getSyncState
import org.hisp.dhis.android.core.arch.handlers.internal.HandleAction
import org.hisp.dhis.android.core.common.State
import org.hisp.dhis.android.core.common.internal.DataStatePropagator
import org.hisp.dhis.android.core.enrollment.Enrollment
import org.hisp.dhis.android.core.enrollment.EnrollmentInternalAccessor
import org.hisp.dhis.android.core.enrollment.EnrollmentTableInfo
import org.hisp.dhis.android.core.event.Event
import org.hisp.dhis.android.core.event.internal.EventImportHandler
import org.hisp.dhis.android.core.imports.TrackerImportConflict
import org.hisp.dhis.android.core.imports.internal.BaseImportSummaryHelper.getReferences
import org.hisp.dhis.android.core.imports.internal.EnrollmentImportSummary
import org.hisp.dhis.android.core.imports.internal.TrackerImportConflictParser
import org.hisp.dhis.android.core.imports.internal.TrackerImportConflictStore
import org.hisp.dhis.android.core.tracker.importer.internal.JobReportEnrollmentHandler

@Reusable
internal class EnrollmentImportHandler @Inject constructor(
    private val enrollmentStore: EnrollmentStore,
    private val eventImportHandler: EventImportHandler,
    private val trackerImportConflictStore: TrackerImportConflictStore,
    private val trackerImportConflictParser: TrackerImportConflictParser,
    private val jobReportEnrollmentHandler: JobReportEnrollmentHandler,
    private val dataStatePropagator: DataStatePropagator
) {

    fun handleEnrollmentImportSummary(
        enrollmentImportSummaries: List<EnrollmentImportSummary?>?,
        enrollments: List<Enrollment>,
        teiUid: String
    ) {

        enrollmentImportSummaries?.filterNotNull()?.forEach { enrollmentImportSummary ->
            enrollmentImportSummary.reference()?.let { enrollmentUid ->

                val syncState = getSyncState(enrollmentImportSummary.status())
                trackerImportConflictStore.deleteEnrollmentConflicts(enrollmentUid)

                val handleAction = enrollmentStore.setSyncStateOrDelete(enrollmentUid, syncState)

                if (syncState == State.ERROR || syncState == State.WARNING) {
                    dataStatePropagator.resetUploadingEventStates(enrollmentUid)
                }

                if (handleAction !== HandleAction.Delete) {
                    jobReportEnrollmentHandler.handleEnrollmentNotes(enrollmentUid, syncState)
                    storeEnrollmentImportConflicts(enrollmentImportSummary, teiUid)

                    handleEventImportSummaries(enrollmentImportSummary, enrollments, teiUid)
                }
            }
        }

        val processedEnrollments = getReferences(enrollmentImportSummaries)

        enrollments.filterNot { processedEnrollments.contains(it.uid()) }.forEach { enrollment ->
            val state = State.TO_UPDATE
            trackerImportConflictStore.deleteEnrollmentConflicts(enrollment.uid())
            enrollmentStore.setSyncStateOrDelete(enrollment.uid(), state)
            dataStatePropagator.resetUploadingEventStates(enrollment.uid())
        }

        dataStatePropagator.refreshTrackedEntityInstanceAggregatedSyncState(teiUid)
    }

    private fun handleEventImportSummaries(
        enrollmentImportSummary: EnrollmentImportSummary,
        enrollments: List<Enrollment>,
        teiUid: String
    ) {
        enrollmentImportSummary.events()?.importSummaries()?.let { importSummaries ->
            val enrollmentUid = enrollmentImportSummary.reference()!!
            eventImportHandler.handleEventImportSummaries(
                importSummaries,
                getEvents(enrollmentUid, enrollments),
                enrollmentUid,
                teiUid
            )
        }
    }

    private fun storeEnrollmentImportConflicts(
        enrollmentImportSummary: EnrollmentImportSummary,
        teiUid: String
    ) {
        val trackerImportConflicts: MutableList<TrackerImportConflict> = ArrayList()

        if (enrollmentImportSummary.description() != null) {
            trackerImportConflicts.add(
                getConflictBuilder(teiUid, enrollmentImportSummary)
                    .conflict(enrollmentImportSummary.description())
                    .displayDescription(enrollmentImportSummary.description())
                    .value(enrollmentImportSummary.reference())
                    .build()
            )
        }

        enrollmentImportSummary.conflicts()?.forEach { importConflict ->
            trackerImportConflicts.add(
                trackerImportConflictParser
                    .getEnrollmentConflict(importConflict, getConflictBuilder(teiUid, enrollmentImportSummary))
            )
        }

        trackerImportConflicts.forEach { trackerImportConflictStore.insert(it) }
    }

    private fun getEvents(
        enrollmentUid: String,
        enrollments: List<Enrollment>
    ): List<Event> {
        return enrollments.find { it.uid() == enrollmentUid }?.let {
            EnrollmentInternalAccessor.accessEvents(it)
        } ?: listOf()
    }

    private fun getConflictBuilder(
        trackedEntityInstanceUid: String,
        enrollmentImportSummary: EnrollmentImportSummary
    ): TrackerImportConflict.Builder {
        return TrackerImportConflict.builder()
            .trackedEntityInstance(trackedEntityInstanceUid)
            .enrollment(enrollmentImportSummary.reference())
            .tableReference(EnrollmentTableInfo.TABLE_INFO.name())
            .status(enrollmentImportSummary.status())
            .created(Date())
    }
}
