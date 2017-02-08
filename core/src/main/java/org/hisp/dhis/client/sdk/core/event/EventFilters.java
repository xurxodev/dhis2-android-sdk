package org.hisp.dhis.client.sdk.core.event;

import java.text.SimpleDateFormat;
import java.util.Date;

public class EventFilters {
    public static final String AMERICAN_DATE_FORMAT = "yyyy-MM-dd";

    private String organisationUnitUId = null;
    private String programUId = null;
    private int maxEvents = 0;
    private String startDate = null;
    private String endDate = null;
    private String categoryCombinationAttribute = null;
    private String categoryOptionAttribute = null;

    public String getOrganisationUnitUId() {
        return organisationUnitUId;
    }

    public void setOrganisationUnitUId(String organisationUnitUId) {
        this.organisationUnitUId = organisationUnitUId;
    }

    public String getProgramUId() {
        return programUId;
    }

    public void setProgramUId(String programUId) {
        this.programUId = programUId;
    }

    public int getMaxEvents() {
        return maxEvents;
    }

    public void setMaxEvents(int maxEvents) {
        this.maxEvents = maxEvents;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = convertDateToString(startDate);
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = convertDateToString(endDate);
    }

    public String getCategoryCombinationAttribute() {
        return categoryCombinationAttribute;
    }

    public void setCategoryCombinationAttribute(String categoryCombinationAttribute) {
        this.categoryCombinationAttribute = categoryCombinationAttribute;
    }

    public String getCategoryOptionAttribute() {
        return categoryOptionAttribute;
    }

    public void setCategoryOptionAttribute(String categoryOptionAttribute) {
        this.categoryOptionAttribute = categoryOptionAttribute;
    }

    private String convertDateToString (Date date){
        SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(AMERICAN_DATE_FORMAT);

        return DATE_FORMAT.format(startDate);
    }

}
