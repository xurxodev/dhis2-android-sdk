package org.hisp.dhis.android.sdk.utils.log;

import java.util.ArrayList;
import java.util.List;

public class SdkLogger {

    List<LogMessage> logMessages;
    public static final int INFO=0;
    public static final int WARNING=1;
    public static final int ERROR=2;
    public static SdkLogger sdkLogger;

    private SdkLogger(){}

    public static SdkLogger getInstance(){
        if(sdkLogger==null)
            sdkLogger= new SdkLogger();
        return sdkLogger;
    }

    public void initLog(){
        logMessages=new ArrayList<>();
    }

    public void addInfo(String message){
        logMessages.add(new LogMessage(message, INFO));
    }

    public void addWarning(String message, Exception e){
        logMessages.add(new LogMessage(message, e, WARNING));
    }

    public void addError(String message, Exception e){
        logMessages.add(new LogMessage(message, e, ERROR));
    }

    public List<LogMessage> finishLog(){
        List<LogMessage> logMessages=this.logMessages;
        this.logMessages=null;
        return logMessages;
    }

}
