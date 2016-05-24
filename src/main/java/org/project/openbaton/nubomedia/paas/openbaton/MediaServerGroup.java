package org.project.openbaton.nubomedia.paas.openbaton;

/**
 * Created by maa on 21.10.15.
 */
public class MediaServerGroup {

    private String mediaServerGroupID;
    private String eventAllocatedID;
    private String eventErrorID;
    private String nsdID;
    private String token;

    public MediaServerGroup() {
    }

    public String getNsdID() {
        return nsdID;
    }

    public void setNsdID(String nsdID) {
        this.nsdID = nsdID;
    }

    public String getMediaServerGroupID() {
        return mediaServerGroupID;
    }

    public void setMediaServerGroupID(String mediaServerGroupID) {
        this.mediaServerGroupID = mediaServerGroupID;
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
                "mediaServerGroupID='" + mediaServerGroupID + '\'' +
                ", eventAllocatedID='" + eventAllocatedID + '\'' +
                ", eventErrorID='" + eventErrorID + '\'' +
                ", nsdID='" + nsdID + '\'' +
                ", token='" + token + '\'' +
                '}';
    }
}
