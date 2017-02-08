package org.hisp.dhis.client.sdk.android.event;

import static org.hisp.dhis.client.sdk.android.api.network.NetworkUtils.call;
import static org.hisp.dhis.client.sdk.android.api.network.NetworkUtils.unwrap;

import android.support.annotation.NonNull;

import org.hisp.dhis.client.sdk.core.common.Fields;
import org.hisp.dhis.client.sdk.core.common.network.ApiException;
import org.hisp.dhis.client.sdk.core.common.network.ApiMessage;
import org.hisp.dhis.client.sdk.core.common.utils.CollectionUtils;
import org.hisp.dhis.client.sdk.core.event.EventApiClient;
import org.hisp.dhis.client.sdk.core.event.EventFilters;
import org.hisp.dhis.client.sdk.models.event.Event;
import org.hisp.dhis.client.sdk.models.event.EventWrapper;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class EventApiClientImpl implements EventApiClient {
    private final EventApiClientRetrofit eventApiclientRetrofit;

    public EventApiClientImpl(EventApiClientRetrofit eventApiclientRetrofit) {
        this.eventApiclientRetrofit = eventApiclientRetrofit;
    }

    @Override
    public List<Event> getEvents(Fields fields, EventFilters eventFilters)
            throws ApiException {

        Map<String, String> queryMap = new HashMap<>();

        addBasicFilters(queryMap, eventFilters);

        addCommonFields(fields, queryMap);

        return callEvents(queryMap);
    }

    @Override
    public List<Event> getEvents(
            Fields fields, DateTime lastUpdated, Set<String> uids) throws ApiException {

        Map<String, String> queryMap = new HashMap<>();

        /* filter programs by lastUpdated field */
        if (lastUpdated != null) {
            queryMap.put("lastUpdated", lastUpdated.toString());
        }


        /* disable paging */
        queryMap.put("skipPaging", "true");

        addCommonFields(fields, queryMap);

        List<Event> allEvents = new ArrayList<>();
        if (uids != null && !uids.isEmpty()) {

            // splitting up request into chunks
            List<String> idFilters = buildIdFilter(uids);
            for (String idFilter : idFilters) {
                // List<String> combinedFilters = new ArrayList<>(filters);
                Map<String, String> combinedFilters = new HashMap<>(queryMap);
                combinedFilters.put("event", idFilter);

                // downloading subset of programs
                allEvents.addAll(unwrap(call(
                        eventApiclientRetrofit.getEvents(queryMap)), "events"));
            }
        } else {
            allEvents.addAll(unwrap(call(
                    eventApiclientRetrofit.getEvents(queryMap)), "events"));
        }

        return allEvents;
    }

    @Override
    public ApiMessage postEvents(List<Event> events) throws ApiException {
        Map<String, List<Event>> eventMap = new HashMap<>();
        eventMap.put("events", events);

        return call(eventApiclientRetrofit.postEvents(eventMap));
    }

    @Override
    public ApiMessage deleteEvent(Event event) throws ApiException {
        return call(eventApiclientRetrofit.deleteEvent(event.getUId()));
    }

    private static List<String> buildIdFilter(Set<String> ids) {
        List<String> idFilters = new ArrayList<>();

        if (ids != null && !ids.isEmpty()) {
            List<List<String>> splittedIds = CollectionUtils.slice(new ArrayList<>(ids), 64);
            for (List<String> listOfIds : splittedIds) {
                idFilters.add(CollectionUtils.join(listOfIds, ";"));
            }
        }

        return idFilters;
    }


    private void addCommonFields(Fields fields, Map<String, String> queryMap) {

        switch (fields) {
            case BASIC: {
                queryMap.put("fields", "event");
                break;
            }
            case ALL: {
                queryMap.put("fields", "event,name,displayName,created,lastUpdated,access," +
                        "program,programStage,status,orgUnit,eventDate,dueDate," +
                        "coordinate,dataValues");
                break;
            }
        }
    }


    @NonNull
    private List<Event> callEvents(Map<String, String> queryMap) {
        EventWrapper response = call(
                eventApiclientRetrofit.getEventsAndPager(queryMap));
        List<Event> allEvents = new ArrayList<>();
        allEvents.addAll(response.getEvents());
        return allEvents;
    }

    private void addBasicFilters(Map<String, String> queryMap, EventFilters eventFilters) {

        if (eventFilters.getOrganisationUnitUId() != null
                && !eventFilters.getOrganisationUnitUId().isEmpty()) {
            queryMap.put("orgUnit", eventFilters.getOrganisationUnitUId());
        }

        if (eventFilters.getProgramUId() != null
                && !eventFilters.getProgramUId().isEmpty()) {
            queryMap.put("program", eventFilters.getProgramUId());
        }

        if (eventFilters.getStartDate() != null
                && !eventFilters.getStartDate().isEmpty()) {
            queryMap.put("startDate", eventFilters.getStartDate());
        }

        if (eventFilters.getStartDate() != null
                && !eventFilters.getStartDate().isEmpty()) {
            queryMap.put("endDate", eventFilters.getEndDate());
        }

        if (eventFilters.getCategoryCombinationAttribute() != null
                && !eventFilters.getCategoryCombinationAttribute().isEmpty()) {
            queryMap.put("attributeCc", eventFilters.getCategoryCombinationAttribute());
        }

        if (eventFilters.getCategoryOptionAttribute() != null
                && !eventFilters.getCategoryOptionAttribute().isEmpty()) {
            queryMap.put("attributeCos", eventFilters.getCategoryOptionAttribute());
        }

        if (eventFilters.getMaxEvents() > 0) {
            queryMap.put("maxEvents", String.valueOf(eventFilters.getMaxEvents()));
        } else {
            queryMap.put("skipPaging", "true");
        }
    }
}
