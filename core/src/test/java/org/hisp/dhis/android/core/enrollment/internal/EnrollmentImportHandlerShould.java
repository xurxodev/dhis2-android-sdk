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

package org.hisp.dhis.android.core.enrollment.internal;

import org.hisp.dhis.android.core.arch.db.stores.internal.IdentifiableObjectStore;
import org.hisp.dhis.android.core.arch.db.stores.internal.ObjectStore;
import org.hisp.dhis.android.core.common.State;
import org.hisp.dhis.android.core.common.internal.DataStatePropagator;
import org.hisp.dhis.android.core.event.internal.EventImportHandler;
import org.hisp.dhis.android.core.imports.ImportStatus;
import org.hisp.dhis.android.core.imports.TrackerImportConflict;
import org.hisp.dhis.android.core.imports.internal.EnrollmentImportSummary;
import org.hisp.dhis.android.core.imports.internal.EventImportSummaries;
import org.hisp.dhis.android.core.imports.internal.EventImportSummary;
import org.hisp.dhis.android.core.imports.internal.TrackerImportConflictParser;
import org.hisp.dhis.android.core.note.Note;
import org.hisp.dhis.android.core.trackedentity.internal.TrackedEntityInstanceStore;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(JUnit4.class)
public class EnrollmentImportHandlerShould {

    @Mock
    private EnrollmentStore enrollmentStore;

    @Mock
    private TrackedEntityInstanceStore trackedEntityInstanceStore;

    @Mock
    private IdentifiableObjectStore<Note> noteStore;

    @Mock
    private EventImportHandler eventImportHandler;

    @Mock
    private EventImportSummaries importEvent;

    @Mock
    private EventImportSummary eventSummary;

    @Mock
    private EnrollmentImportSummary importSummary;

    @Mock
    private ObjectStore<TrackerImportConflict> trackerImportConflictStore;

    @Mock
    private TrackerImportConflictParser trackerImportConflictParser;

    @Mock
    private DataStatePropagator dataStatePropagator;

    // object to test
    private EnrollmentImportHandler enrollmentImportHandler;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        enrollmentImportHandler = new EnrollmentImportHandler(enrollmentStore, trackedEntityInstanceStore, noteStore,
                eventImportHandler, trackerImportConflictStore, trackerImportConflictParser, dataStatePropagator);
    }

    @Test
    public void do_nothing_when_passing_null_arguments() throws Exception {
        enrollmentImportHandler.handleEnrollmentImportSummary(null, null);

        verify(enrollmentStore, never()).setStateOrDelete(anyString(), any(State.class));
    }

    @Test
    public void invoke_set_state_when_enrollment_import_summary_is_success_with_reference() throws Exception {
        when(importSummary.status()).thenReturn(ImportStatus.SUCCESS);
        when(importSummary.reference()).thenReturn("test_enrollment_uid");

        enrollmentImportHandler.handleEnrollmentImportSummary(Collections.singletonList(importSummary), "test_tei_uid");

        verify(enrollmentStore, times(1)).setStateOrDelete("test_enrollment_uid", State.SYNCED);
    }

    @Test
    public void  invoke_set_state_when_enrollment_import_summary_is_error_with_reference() throws Exception {
        when(importSummary.status()).thenReturn(ImportStatus.ERROR);
        when(importSummary.reference()).thenReturn("test_enrollment_uid");

        enrollmentImportHandler.handleEnrollmentImportSummary(Collections.singletonList(importSummary), "test_tei_uid");

        verify(enrollmentStore, times(1)).setStateOrDelete("test_enrollment_uid", State.ERROR);
    }

    @Test
    public void invoke_set_state_and_handle_event_import_summaries_when_enrollment_is_success_and_event_is_imported() throws Exception {
        when(importSummary.status()).thenReturn(ImportStatus.SUCCESS);
        when(importSummary.reference()).thenReturn("test_enrollment_uid");
        when(importSummary.events()).thenReturn(importEvent);

        List<EventImportSummary> eventSummaries = Collections.singletonList(eventSummary);
        when(importEvent.importSummaries()).thenReturn(eventSummaries);


        enrollmentImportHandler.handleEnrollmentImportSummary(Collections.singletonList(importSummary), "test_tei_uid");

        verify(enrollmentStore, times(1)).setStateOrDelete("test_enrollment_uid", State.SYNCED);
        verify(eventImportHandler, times(1)).handleEventImportSummaries(
                eq(eventSummaries), anyString(), anyString()
        );
    }
}