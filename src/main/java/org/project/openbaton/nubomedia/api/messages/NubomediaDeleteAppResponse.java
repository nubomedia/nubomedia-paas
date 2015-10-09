package org.project.openbaton.nubomedia.api.messages;


/**
 * Created by maa on 09.10.15.
 */
public class NubomediaDeleteAppResponse {

    private int appID;
    private String appName;
    private String nameSpace;
    private int code;

    public NubomediaDeleteAppResponse() {
    }

    public NubomediaDeleteAppResponse(int appID, String appName, String nameSpace, int deleteStatus) {
        this.appID = appID;
        this.appName = appName;
        this.nameSpace = nameSpace;
        this.code = deleteStatus;
    }

    public int getAppID() {
        return appID;
    }

    public void setAppID(int appID) {
        this.appID = appID;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getNameSpace() {
        return nameSpace;
    }

    public void setNameSpace(String nameSpace) {
        this.nameSpace = nameSpace;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
