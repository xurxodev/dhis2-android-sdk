package org.hisp.dhis.android.sdk.persistence.models;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.Table;

import org.hisp.dhis.android.sdk.controllers.metadata.MetaDataController;
import org.hisp.dhis.android.sdk.persistence.Dhis2Database;

import java.util.Map;

@Table(databaseName = Dhis2Database.NAME)
public class ProgramAttributeValue extends AttributeValue{

    public ProgramAttributeValue(){}

    @Column(name = "program")
    String program;

    @JsonProperty("program")
    public void setProgram(Map<String, Object> program){
        this.program = (String) program.get("id");
    }

    public Program getProgram(){
        return MetaDataController.getProgram(program);
    }

    public void setProgram(String program){
        this.program = program;
    }
}
