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

package org.hisp.dhis.android.core.event.internal;

import org.hisp.dhis.android.core.common.State;
import org.hisp.dhis.android.core.common.internal.DataStatePropagator;
import org.hisp.dhis.android.core.enrollment.internal.EnrollmentStore;
import org.hisp.dhis.android.core.event.Event;
import org.hisp.dhis.android.core.imports.ImportStatus;
import org.hisp.dhis.android.core.imports.internal.EventImportSummary;
import org.hisp.dhis.android.core.imports.internal.TrackerImportConflictParser;
import org.hisp.dhis.android.core.imports.internal.TrackerImportConflictStore;
import org.hisp.dhis.android.core.trackedentity.internal.TrackedEntityInstanceStore;
import org.hisp.dhis.android.core.tracker.importer.internal.JobReportEventHandler;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(JUnit4.class)
public class EventImportHandlerShould {
    @Mock
    private EventImportSummary importSummary;

    @Mock
    private EventStore eventStore;

    @Mock
    private TrackerImportConflictStore trackerImportConflictStore;

    @Mock
    private JobReportEventHandler jobReportEventHandler;

    @Mock
    private DataStatePropagator dataStatePropagator;

    @Mock
    private TrackerImportConflictParser trackerImportConflictParser;

    private final List<Event> events = new ArrayList<>();

    @Mock
    private Event event;

    // object to test
    private EventImportHandler eventImportHandler;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        when(importSummary.status()).thenReturn(ImportStatus.SUCCESS);

        eventImportHandler = new EventImportHandler(eventStore, trackerImportConflictStore,
                trackerImportConflictParser, jobReportEventHandler, dataStatePropagator);
    }

    @Test
    public void do_nothing_when_passing_null_argument() {
        eventImportHandler.handleEventImportSummaries(null, events, null, null);

        verify(eventStore, never()).setSyncStateOrDelete(anyString(), any(State.class));
    }

    @Test
    public void invoke_set_state_after_handle_event_import_summaries_with_success_status_and_reference() {
        when(importSummary.status()).thenReturn(ImportStatus.SUCCESS);
        when(importSummary.reference()).thenReturn("test_event_uid");

        eventImportHandler.handleEventImportSummaries(Collections.singletonList(importSummary), events,
                "test_enrollment_uid", "test_tei_uid");

        verify(eventStore, times(1)).setSyncStateOrDelete("test_event_uid", State.SYNCED);
    }

    @Test
    public void invoke_set_state_after_handle_event_import_summaries_with_error_status_and_reference() {
        when(importSummary.status()).thenReturn(ImportStatus.ERROR);
        when(importSummary.reference()).thenReturn("test_event_uid");

        eventImportHandler.handleEventImportSummaries(Collections.singletonList(importSummary), events,
                "test_enrollment_uid", "test_tei_uid");

        verify(eventStore, times(1)).setSyncStateOrDelete("test_event_uid", State.ERROR);
    }

    @Test
    public void mark_as_to_update_events_not_present_in_the_response() {
        when(importSummary.status()).thenReturn(ImportStatus.SUCCESS);
        when(importSummary.reference()).thenReturn("test_event_uid");

        List<Event> events = new ArrayList<>();
        events.add(event);
        when(event.uid()).thenReturn("missing_event_uid");

        eventImportHandler.handleEventImportSummaries(Collections.singletonList(importSummary), events,
                "test_enrollment_uid", "test_tei_uid");

        verify(eventStore, times(1)).setSyncStateOrDelete("test_event_uid", State.SYNCED);
        verify(eventStore, times(1)).setSyncStateOrDelete("missing_event_uid", State.TO_UPDATE);
    }
}