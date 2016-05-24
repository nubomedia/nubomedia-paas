package org.project.openbaton.nubomedia.paas.messages;

/**
 * Created by maa on 03.11.15.
 */
public class NubomediaDupMessage {

    private String message;
    private String token;

    public NubomediaDupMessage(String message, String token) {
        this.message = message;
        this.token = token;
    }

    public NubomediaDupMessage() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
