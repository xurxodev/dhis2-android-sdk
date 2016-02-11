package org.hisp.dhis.android.sdk.utils.log;

public class LogMessage {
    String message;
    int type;

    public LogMessage(String message, int type){
        this.message=message;
        this.type=type;
    }

    public String getMessage() {
        return message;
    }

    public int getType() {
        return type;
    }
}
