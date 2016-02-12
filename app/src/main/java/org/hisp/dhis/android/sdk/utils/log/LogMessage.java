package org.hisp.dhis.android.sdk.utils.log;

public class LogMessage {
    Exception exception;
    String message;
    int type;

    public LogMessage(String message, Exception exception,  int type){
        this.exception=exception;
        this.message=message;
        this.type=type;
    }

    public LogMessage(String message, int type){
        this.exception=exception;
        this.message=message;
        this.type=type;
    }

    public LogMessage(Exception exception,  int type){
        this.exception=exception;
        this.type=type;
    }
    public Exception getException() {
        return exception;
    }

    public String getMessage() {
        return message;
    }

    public int getType() {
        return type;
    }
}
