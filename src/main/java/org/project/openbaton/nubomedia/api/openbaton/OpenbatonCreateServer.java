package org.project.openbaton.nubomedia.api.openbaton;

/**
 * Created by maa on 21.10.15.
 */
public class OpenbatonCreateServer {

    private String mediaServerID;
    private String eventAllocatedID;
    private String eventErrorID;
    private String nsdID;
    private String token;

    public OpenbatonCreateServer() {
    }

    public String getNsdID() {
        return nsdID;
    }

    public void setNsdID(String nsdID) {
        this.nsdID = nsdID;
    }

    public String getMediaServerID() {
        return mediaServerID;
    }

    public void setMediaServerID(String mediaServerID) {
        this.mediaServerID = mediaServerID;
    }

    public String getEventAllocatedID() {
        return eventAllocatedID;
    }

    public void setEventAllocatedID(String eventAllocatedID) {
        this.eventAllocatedID = eventAllocatedID;
    }

    public String getEventErrorID() {
        return eventErrorID;
    }

    public void setEventErrorID(String eventErrorID) {
        this.eventErrorID = eventErrorID;
    }

    public String getToken(){
        return this.token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "OpenbatonCreateServer{" +
                "mediaServerID='" + mediaServerID + '\'' +
                ", eventAllocatedID='" + eventAllocatedID + '\'' +
                ", eventErrorID='" + eventErrorID + '\'' +
                ", nsdID='" + nsdID + '\'' +
                ", token='" + token + '\'' +
                '}';
    }
}
