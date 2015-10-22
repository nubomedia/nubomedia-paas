package org.project.openbaton.nubomedia.api.messages;

import org.project.openbaton.nubomedia.api.persistence.Application;

/**
 * Created by maa on 28.09.15.
 */
public class NubomediaCreateAppResponse {

    private String id;
    private Application app;
    private int code;

    public NubomediaCreateAppResponse() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Application getApp() {
        return app;
    }

    public void setApp(Application app) {
        this.app = app;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
