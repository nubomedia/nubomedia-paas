package org.project.openbaton.nubomedia.paas.messages;

/**
 * Created by maa on 21.10.15.
 */
public class NubomediaAppNotFoundMessage {

    private String id;
    private String token;
    private final String message = "Application not found";

    public NubomediaAppNotFoundMessage() {
    }

    public NubomediaAppNotFoundMessage(String id, String token) {
        this.id = id;
        this.token = token;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getMessage() {
        return message;
    }
}
