package org.project.openbaton.nubomedia.api.messages;

/**
 * Created by maa on 28.09.15.
 */
public class NubomediaCreateAppResponse {

    private int id;
    private String route;

    public NubomediaCreateAppResponse() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }
}
