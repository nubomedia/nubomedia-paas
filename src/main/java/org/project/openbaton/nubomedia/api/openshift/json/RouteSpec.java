package org.project.openbaton.nubomedia.api.openshift.json;

/**
 * Created by maa on 25/09/2015.
 */
public class RouteSpec {

    public static class Status{
        //TODO: add status information in case of status request
    }

    private String host;
    private BuildElements to;
    private RouteTls tls;
    private Status status;

    public RouteSpec(){}

    public RouteSpec(String host, BuildElements to, RouteTls tls, Status status) {
        this.host = host;
        this.to = to;
        this.tls = tls;
        this.status = status;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public BuildElements getTo() {
        return to;
    }

    public void setTo(BuildElements to) {
        this.to = to;
    }

    public RouteTls getTls() {
        return tls;
    }

    public void setTls(RouteTls tls) {
        this.tls = tls;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
