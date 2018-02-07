package org.hisp.dhis.android.core.event;

import android.database.sqlite.SQLiteConstraintException;
import android.support.annotation.NonNull;
import android.util.Log;

import org.hisp.dhis.android.core.common.State;
import org.hisp.dhis.android.core.data.database.DatabaseAdapter;
import org.hisp.dhis.android.core.data.database.Transaction;
import org.hisp.dhis.android.core.trackedentity.TrackedEntityDataValueHandler;

import static org.hisp.dhis.android.core.utils.Utils.isDeleted;

import java.util.List;

public class EventHandler {
    private final DatabaseAdapter databaseAdapter;
    private final EventStore eventStore;
    private final TrackedEntityDataValueHandler trackedEntityDataValueHandler;

    public EventHandler(EventStore eventStore,
            TrackedEntityDataValueHandler trackedEntityDataValueHandler,
            DatabaseAdapter databaseAdapter) {
        this.eventStore = eventStore;
        this.trackedEntityDataValueHandler = trackedEntityDataValueHandler;
        this.databaseAdapter = databaseAdapter;
    }

    public void handle(@NonNull List<Event> events) {

        if (events != null && !events.isEmpty()) {
            int size = events.size();

            for (int i = 0; i < size; i++) {
                Event event = events.get(i);
                handle(event);
            }
        }
    }

    public void handle(@NonNull Event event) {
        if (event == null) {
            return;
        }

        Transaction transaction = databaseAdapter.beginNewTransaction();
        try {
            if (isDeleted(event)) {
                eventStore.delete(event.uid());
            } else {
                String latitude = null;
                String longitude = null;

                if (event.coordinates() != null) {
                    latitude = event.coordinates().latitude();
                    longitude = event.coordinates().longitude();
                }


                int updatedRow = eventStore.update(event.uid(), event.enrollmentUid(),
                        event.created(), event.lastUpdated(),
                        event.createdAtClient(), event.lastUpdatedAtClient(),
                        event.status(), latitude, longitude, event.program(), event.programStage(),
                        event.organisationUnit(), event.eventDate(), event.completedDate(),
                        event.dueDate(), State.SYNCED, event.attributeCategoryOptions(),
                        event.attributeOptionCombo(),
                        event.trackedEntityInstance(), event.uid());

                if (updatedRow <= 0) {
                    eventStore.insert(event.uid(), event.enrollmentUid(), event.created(),
                            event.lastUpdated(),
                            event.createdAtClient(), event.lastUpdatedAtClient(),
                            event.status(), latitude, longitude, event.program(),
                            event.programStage(),
                            event.organisationUnit(), event.eventDate(), event.completedDate(),
                            event.dueDate(), State.SYNCED, event.attributeCategoryOptions(),
                            event.attributeOptionCombo(),
                            event.trackedEntityInstance());
                }

                trackedEntityDataValueHandler.handle(event.uid(),
                        event.trackedEntityDataValues());
            }
            transaction.setSuccessful();
        }catch (SQLiteConstraintException sql){
            // This catch is necessary to ignore events with bad foreign keys exception
            // More info: If the foreign key have the flag
            // DEFERRABLE INITIALLY DEFERRED this exception will be throw in transaction.end()
            // And the rollback will be executed only when the database is closed.
            // It is a reported as unfixed bug: https://issuetracker.google.com/issues/37001653
            Log.d(this.getClass().getSimpleName(), sql.getMessage());
        }finally {
            transaction.end();
        }
    }


}
