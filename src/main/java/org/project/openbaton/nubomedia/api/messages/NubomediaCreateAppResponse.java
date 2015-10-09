package org.project.openbaton.nubomedia.api.messages;

import java.util.UUID;

/**
 * Created by maa on 28.09.15.
 */
public class NubomediaCreateAppResponse {

    private UUID id;
    private String route;

    public NubomediaCreateAppResponse() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }
}
