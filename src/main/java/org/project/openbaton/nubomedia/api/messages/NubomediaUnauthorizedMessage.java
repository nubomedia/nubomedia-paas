package org.project.openbaton.nubomedia.api.messages;

/**
 * Created by maa on 21.10.15.
 */
public class NubomediaUnauthorizedMessage {

    private String error;
    private String message;

    public NubomediaUnauthorizedMessage(String error, String message) {
        this.error = error;
        this.message = message;
    }

    public NubomediaUnauthorizedMessage() {
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
