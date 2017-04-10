package org.hisp.dhis.client.sdk.models.event;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.hisp.dhis.client.sdk.models.pager.Pager;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EventWrapper {


    @JsonProperty("pager")
    private Pager pager;
    @JsonProperty("events")
    private List<Event> events;

    public EventWrapper() {
    }

    public Pager getPager() {
        return pager;
    }

    public void setPager(Pager pager) {
        this.pager = pager;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(
            List<Event> events) {
        this.events = events;
    }
}
