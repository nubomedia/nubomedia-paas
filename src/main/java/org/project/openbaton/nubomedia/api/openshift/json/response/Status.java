package org.project.openbaton.nubomedia.api.openshift.json.response;

/**
 * Created by Carlo on 27/09/2015.
 */
public class Status {

    private String kind;
    private String status;
    private String message;
    private int code;

    public Status(String kind, String status, String message, int code) {
        this.kind = kind;
        this.status = status;
        this.message = message;
        this.code = code;
    }

    public Status() {
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
