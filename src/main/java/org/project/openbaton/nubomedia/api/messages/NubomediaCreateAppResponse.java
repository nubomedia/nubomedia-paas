package org.project.openbaton.nubomedia.api.messages;

import java.util.UUID;

/**
 * Created by maa on 28.09.15.
 */
public class NubomediaCreateAppResponse {

    private String id;
    private String route;
    private String appName;
    private int code;

    public NubomediaCreateAppResponse() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
