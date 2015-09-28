package org.project.openbaton.nubomedia.api.messages;

/**
 * Created by maa on 28.09.15.
 */
public class NubomediaCreateResponse {

    private int id;
    private String route;

    public NubomediaCreateResponse() {
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
