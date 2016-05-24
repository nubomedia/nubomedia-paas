package org.project.openbaton.nubomedia.paas.messages;

/**
 * Created by maa on 22.10.15.
 */
public class NubomediaOpenbatonMessage {

    public String info;
    public String message;

    public NubomediaOpenbatonMessage() {
    }

    public NubomediaOpenbatonMessage(String info, String message) {
        this.info = info;
        this.message = message;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
