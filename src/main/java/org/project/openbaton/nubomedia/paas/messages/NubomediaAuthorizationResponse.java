package org.project.openbaton.nubomedia.paas.messages;

/**
 * Created by maa on 20.10.15.
 */
public class NubomediaAuthorizationResponse {

    private String token;
    private int code;

    public NubomediaAuthorizationResponse(String token, int code) {
        this.token = token;
        this.code = code;
    }

    public NubomediaAuthorizationResponse() {
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
