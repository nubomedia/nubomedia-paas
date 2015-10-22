package org.project.openbaton.nubomedia.api.openbaton;

/**
 * Created by maa on 21.10.15.
 */
public class OpenbatonCreateServer {

    private String vnfrID;
    private String mediaServerID;
    private String eventID;
    private String token;

    public OpenbatonCreateServer() {
    }

    public String getMediaServerID() {
        return mediaServerID;
    }

    public void setMediaServerID(String mediaServerID) {
        this.mediaServerID = mediaServerID;
    }

    public String getEventID() {
        return eventID;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    public String getToken(){
        return this.token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getVnfrID() {
        return vnfrID;
    }

    public void setVnfrID(String vnfrID) {
        this.vnfrID = vnfrID;
    }
}
