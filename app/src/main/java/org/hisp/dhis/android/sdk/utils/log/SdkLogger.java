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

    public void addInfoMessage(String message){
        logMessages.add(new LogMessage(message,INFO));
    }

    public void addWarningMessage(String message){
        logMessages.add(new LogMessage(message,WARNING));
    }

    public void addErrorMessage(String message){
        logMessages.add(new LogMessage(message,ERROR));
    }

    public void addLogMessage(String message,int type){
        logMessages.add(new LogMessage(message,type));
    }

    public List<LogMessage> finishLog(){
        List<LogMessage> logMessages=this.logMessages;
        this.logMessages=null;
        return logMessages;
    }

}
