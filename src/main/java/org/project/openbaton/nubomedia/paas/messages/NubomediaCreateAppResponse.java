package org.project.openbaton.nubomedia.paas.messages;

import org.project.openbaton.nubomedia.paas.model.persistence.Application;

/**
 * Created by maa on 28.09.15.
 */
public class NubomediaCreateAppResponse {

    private Application app;
    private int code;

    public NubomediaCreateAppResponse() {
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
