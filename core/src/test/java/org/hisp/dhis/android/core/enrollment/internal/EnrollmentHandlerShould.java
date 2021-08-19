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

import org.hisp.dhis.android.core.arch.cleaners.internal.OrphanCleaner;
import org.hisp.dhis.android.core.arch.handlers.internal.HandleAction;
import org.hisp.dhis.android.core.arch.handlers.internal.Handler;
import org.hisp.dhis.android.core.arch.handlers.internal.IdentifiableDataHandler;
import org.hisp.dhis.android.core.data.utils.FillPropertiesTestUtils;
import org.hisp.dhis.android.core.enrollment.Enrollment;
import org.hisp.dhis.android.core.enrollment.EnrollmentInternalAccessor;
import org.hisp.dhis.android.core.event.Event;
import org.hisp.dhis.android.core.note.Note;
import org.hisp.dhis.android.core.note.internal.NoteDHISVersionManager;
import org.hisp.dhis.android.core.note.internal.NoteUniquenessManager;
import org.hisp.dhis.android.core.relationship.Relationship;
import org.hisp.dhis.android.core.relationship.internal.RelationshipDHISVersionManager;
import org.hisp.dhis.android.core.relationship.internal.RelationshipHandler;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(JUnit4.class)
public class EnrollmentHandlerShould {
    @Mock
    private EnrollmentStore enrollmentStore;

    @Mock
    private IdentifiableDataHandler<Event> eventHandler;

    @Mock
    private Handler<Note> noteHandler;

    @Mock
    private NoteUniquenessManager noteUniquenessManager;

    @Mock
    private Enrollment enrollment;

    @Mock
    private Event event;

    @Mock
    private Note note;

    @Mock
    private NoteDHISVersionManager noteVersionManager;

    @Mock
    private RelationshipDHISVersionManager relationshipVersionManager;

    @Mock
    private RelationshipHandler relationshipHandler;

    @Mock
    private OrphanCleaner<Enrollment, Relationship> relationshipOrphanCleaner;

    @Mock
    private OrphanCleaner<Enrollment, Event> eventCleaner;

    // object to test
    private EnrollmentHandler enrollmentHandler;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        when(enrollment.uid()).thenReturn("test_enrollment_uid");
        when(EnrollmentInternalAccessor.accessEvents(enrollment)).thenReturn(Collections.singletonList(event));
        when(enrollment.notes()).thenReturn(Collections.singletonList(note));
        when(note.storedDate()).thenReturn(FillPropertiesTestUtils.LAST_UPDATED_STR);

        List<String> emptyList = Collections.emptyList();
        when(enrollmentStore.selectUidsWhere(anyString())).thenReturn(emptyList);

        enrollmentHandler = new EnrollmentHandler(relationshipVersionManager, relationshipHandler, noteVersionManager,
                enrollmentStore, eventHandler, eventCleaner, noteHandler, noteUniquenessManager,
                relationshipOrphanCleaner);
    }

    @Test
    public void do_nothing_when_passing_null_argument() {
        enrollmentHandler.handleMany(null, e -> e, false);

        // verify that store or event handler is never called
        verify(enrollmentStore, never()).deleteIfExists(anyString());
        verify(enrollmentStore, never()).updateOrInsert(any(Enrollment.class));

        verify(eventHandler, never()).handleMany(anyCollection(), any(), anyBoolean());
        verify(eventCleaner, never()).deleteOrphan(any(Enrollment.class), anyCollection());
        verify(noteHandler, never()).handleMany(anyCollection());
    }

    @Test
    public void invoke_only_delete_when_a_enrollment_is_set_as_deleted() {
        when(enrollment.deleted()).thenReturn(Boolean.TRUE);

        enrollmentHandler.handleMany(Collections.singletonList(enrollment), o -> o, false);

        // verify that enrollment store is only invoked with delete
        verify(enrollmentStore, times(1)).deleteIfExists(anyString());


        verify(enrollmentStore, never()).updateOrInsert(any(Enrollment.class));

        // event handler should not be invoked
        verify(eventHandler, never()).handleMany(anyCollection(), any(), anyBoolean());
        verify(eventCleaner, times(1)).deleteOrphan(any(Enrollment.class), anyCollection());
        verify(noteHandler, never()).handleMany(anyCollection());
    }

    @Test
    public void invoke_only_update_or_insert_when_handle_enrollment_is_valid() {
        when(enrollment.deleted()).thenReturn(Boolean.FALSE);
        when(enrollmentStore.updateOrInsert(any(Enrollment.class))).thenReturn(HandleAction.Update);

        enrollmentHandler.handleMany(Collections.singletonList(enrollment), o -> o, false);

        // verify that enrollment store is only invoked with update
        verify(enrollmentStore, times(1)).updateOrInsert(any(Enrollment.class));

        verify(enrollmentStore, never()).deleteIfExists(anyString());

        // event handler should be invoked once
        verify(eventHandler, times(1)).handleMany(anyList(), any(), eq(false));
        verify(eventCleaner, times(1)).deleteOrphan(any(Enrollment.class), anyCollection());
        verify(noteHandler, times(1)).handleMany(anyCollection());
    }
}
