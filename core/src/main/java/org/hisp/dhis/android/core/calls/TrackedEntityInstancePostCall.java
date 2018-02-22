package org.hisp.dhis.android.core.calls;

import android.support.annotation.NonNull;

import org.hisp.dhis.android.core.enrollment.Enrollment;
import org.hisp.dhis.android.core.enrollment.EnrollmentImportHandler;
import org.hisp.dhis.android.core.enrollment.EnrollmentStore;
import org.hisp.dhis.android.core.event.Event;
import org.hisp.dhis.android.core.event.EventImportHandler;
import org.hisp.dhis.android.core.event.EventStore;
import org.hisp.dhis.android.core.imports.WebResponse;
import org.hisp.dhis.android.core.imports.WebResponseHandler;
import org.hisp.dhis.android.core.relationship.Relationship;
import org.hisp.dhis.android.core.relationship.RelationshipStore;
import org.hisp.dhis.android.core.trackedentity.TrackedEntityAttributeValue;
import org.hisp.dhis.android.core.trackedentity.TrackedEntityAttributeValueStore;
import org.hisp.dhis.android.core.trackedentity.TrackedEntityDataValue;
import org.hisp.dhis.android.core.trackedentity.TrackedEntityDataValueStore;
import org.hisp.dhis.android.core.trackedentity.TrackedEntityInstance;
import org.hisp.dhis.android.core.trackedentity.TrackedEntityInstanceImportHandler;
import org.hisp.dhis.android.core.trackedentity.TrackedEntityInstancePayload;
import org.hisp.dhis.android.core.trackedentity.TrackedEntityInstanceService;
import org.hisp.dhis.android.core.trackedentity.TrackedEntityInstanceStore;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Response;
@SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
public class TrackedEntityInstancePostCall implements Call<Response<WebResponse>> {
    // service
    private final TrackedEntityInstanceService trackedEntityInstanceService;

    // stores

    private final RelationshipStore relationshipStore;
    private final TrackedEntityInstanceStore trackedEntityInstanceStore;
    private final EnrollmentStore enrollmentStore;
    private final EventStore eventStore;
    private final TrackedEntityDataValueStore trackedEntityDataValueStore;
    private final TrackedEntityAttributeValueStore trackedEntityAttributeValueStore;

    private boolean isExecuted;

    public TrackedEntityInstancePostCall(@NonNull RelationshipStore relationshipStore,
                                         @NonNull TrackedEntityInstanceService trackedEntityInstanceService,
                                         @NonNull TrackedEntityInstanceStore trackedEntityInstanceStore,
                                         @NonNull EnrollmentStore enrollmentStore,
                                         @NonNull EventStore eventStore,
                                         @NonNull TrackedEntityDataValueStore trackedEntityDataValueStore,
                                         @NonNull TrackedEntityAttributeValueStore trackedEntityAttributeValueStore) {
        this.relationshipStore = relationshipStore;
        this.trackedEntityInstanceService = trackedEntityInstanceService;
        this.trackedEntityInstanceStore = trackedEntityInstanceStore;
        this.enrollmentStore = enrollmentStore;
        this.eventStore = eventStore;
        this.trackedEntityDataValueStore = trackedEntityDataValueStore;
        this.trackedEntityAttributeValueStore = trackedEntityAttributeValueStore;
    }

    @Override
    public boolean isExecuted() {
        synchronized (this) {
            return isExecuted;
        }
    }

    @Override
    public Response<WebResponse> call() throws Exception {
        synchronized (this) {
            if (isExecuted) {
                throw new IllegalStateException("Call is already executed");
            }

            isExecuted = true;

        }


        //The tracked entity instances should be posted in two steps to avoid server 500 error.
        // In the first post the trackedEntityInstance is pushed with attributes
        // In the second post the trackedEntityInstance is pushed with enrollments and relationships and attributes.
        List<TrackedEntityInstance> trackedEntityInstanceWithAttributesOnly = querySimpleTeiToSync();
        if(trackedEntityInstanceWithAttributesOnly.isEmpty()){
            return null;
        }
        //Post tracked entity instance only with attributes.
        Response<WebResponse> response = postTrackedEntityInstances(trackedEntityInstanceWithAttributesOnly);

        if(response.isSuccessful()) {
            //Post tracked entity instance with attributes first, relationships and enrollments.
            List<TrackedEntityInstance> trackedEntityInstanceWithEnrollments =
                    queryTrackedEntityInstanceWithAllData();
            if(trackedEntityInstanceWithEnrollments.isEmpty()){
                handleWebResponse(response);
                return response;
            }

            response = postTrackedEntityInstances(trackedEntityInstanceWithEnrollments);
                if(response!=null && response.isSuccessful()){
                    handleWebResponse(response);
                }
        }
        return response;
    }

    @NonNull
    private Response<WebResponse> postTrackedEntityInstances(
            List<TrackedEntityInstance> trackedEntityInstancesToPost) throws IOException {

        TrackedEntityInstancePayload trackedEntityInstancePayload =
                new TrackedEntityInstancePayload();
        trackedEntityInstancePayload.trackedEntityInstances =
                trackedEntityInstancesToPost;

        return trackedEntityInstanceService.
                postTrackedEntityInstances(trackedEntityInstancePayload).execute();
    }

    @NonNull
    private List<TrackedEntityInstance> querySimpleTeiToSync() {
        Map<String, List<TrackedEntityAttributeValue>> attributeValueMap = trackedEntityAttributeValueStore.query();
        Map<String, TrackedEntityInstance> trackedEntityInstances =
                trackedEntityInstanceStore.queryToPost();

        List<TrackedEntityInstance> trackedEntityInstancesRecreated = new ArrayList<>();

        // EMPTY LISTS TO REPLACE NULL VALUES SO THAT API DOESN'T BREAK.
        List<TrackedEntityAttributeValue> emptyAttributeValueList = new ArrayList<>();

        for (Map.Entry<String, TrackedEntityInstance> teiUid : trackedEntityInstances.entrySet()) {

            // Building TEI WITHOUT (new ArrayList) relationships
            List<TrackedEntityAttributeValue> attributeValues = attributeValueMap.get(teiUid.getKey());

            // if attributeValues is null, it means that they doesn't exist.
            // Then we need to set it to empty arrayList so that API doesn't break
            if (attributeValues == null) {
                attributeValues = emptyAttributeValueList;
            }
            TrackedEntityInstance trackedEntityInstance = trackedEntityInstances.get(teiUid.getKey());

            trackedEntityInstancesRecreated.add(
                    trackedEntityInstance.toBuilder()
                            .trackedEntityAttributeValues(attributeValues)
                            .relationships(new ArrayList<Relationship>())
                            .enrollments(new ArrayList<Enrollment>()).build());
        }

        return trackedEntityInstancesRecreated;
    }

    @NonNull
    private List<TrackedEntityInstance> queryTrackedEntityInstanceWithAllData() {
        Map<String, List<TrackedEntityDataValue>> dataValueMap =
                trackedEntityDataValueStore.queryTrackedEntityDataValues(Boolean.FALSE);
        Map<String, List<Event>> eventMap = eventStore.queryEventsAttachedToEnrollmentToPost();
        Map<String, List<Enrollment>> enrollmentMap = enrollmentStore.query();
        Map<String, List<TrackedEntityAttributeValue>> attributeValueMap = trackedEntityAttributeValueStore.query();
        Map<String, TrackedEntityInstance> trackedEntityInstances =
                trackedEntityInstanceStore.queryToPost();

        List<TrackedEntityInstance> trackedEntityInstancesRecreated = new ArrayList<>();

        // EMPTY LISTS TO REPLACE NULL VALUES SO THAT API DOESN'T BREAK.
        List<TrackedEntityAttributeValue> emptyAttributeValueList = new ArrayList<>();

        for (Map.Entry<String, TrackedEntityInstance> teiUid : trackedEntityInstances.entrySet()) {
            List<Enrollment> enrollmentsRecreated = new ArrayList<>();
            List<Enrollment> enrollments = enrollmentMap.get(teiUid.getKey());

            // if enrollments is not null, then they exist for this tracked entity instance
            if (enrollments != null) {
                List<Event> eventRecreated = new ArrayList<>();
                // building enrollment
                int enrollmentSize = enrollments.size();
                for (int i = 0; i < enrollmentSize; i++) {
                    Enrollment enrollment = enrollments.get(i);

                    // building events for enrollment
                    List<Event> eventsForEnrollment = eventMap.get(enrollment.uid());

                    // if eventsForEnrollment is not null, then they exist for this enrollment
                    if (eventsForEnrollment != null) {
                        int eventSize = eventsForEnrollment.size();
                        for (int j = 0; j < eventSize; j++) {
                            Event event = eventsForEnrollment.get(j);
                            List<TrackedEntityDataValue> dataValuesForEvent = dataValueMap.get(event.uid());

                            eventRecreated.add(event.toBuilder()
                                    .trackedEntityDataValues(dataValuesForEvent)
                                    .build());
                        }
                    }
                    enrollmentsRecreated.add(enrollment.toBuilder()
                            .events(eventRecreated).build());

                }
            }

            // Building TEI WITHOUT (new ArrayList) relationships
            List<TrackedEntityAttributeValue> attributeValues = attributeValueMap.get(teiUid.getKey());

            // if attributeValues is null, it means that they doesn't exist.
            // Then we need to set it to empty arrayList so that API doesn't break
            if (attributeValues == null) {
                attributeValues = emptyAttributeValueList;
            }
            TrackedEntityInstance trackedEntityInstance = trackedEntityInstances.get(teiUid.getKey());

            List<Relationship> relationshipRecreated = relationshipStore.queryByTrackedEntityInstanceUid(
                        trackedEntityInstance.uid());

            trackedEntityInstancesRecreated.add(
                    trackedEntityInstance.toBuilder()
                    .trackedEntityAttributeValues(attributeValues)
                    .relationships(relationshipRecreated)
                    .enrollments(enrollmentsRecreated).build());
        }

        return trackedEntityInstancesRecreated;

    }

    private void handleWebResponse(Response<WebResponse> response) {
        WebResponse webResponse = response.body();
        EventImportHandler eventImportHandler = new EventImportHandler(eventStore);

        EnrollmentImportHandler enrollmentImportHandler = new EnrollmentImportHandler(
                enrollmentStore, eventImportHandler
        );

        TrackedEntityInstanceImportHandler trackedEntityInstanceImportHandler =
                new TrackedEntityInstanceImportHandler(
                        trackedEntityInstanceStore, enrollmentImportHandler, eventImportHandler
                );
        WebResponseHandler webResponseHandler = new WebResponseHandler(trackedEntityInstanceImportHandler);

        webResponseHandler.handleWebResponse(webResponse);

    }
}
