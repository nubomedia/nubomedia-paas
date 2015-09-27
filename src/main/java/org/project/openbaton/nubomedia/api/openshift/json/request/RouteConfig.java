package org.project.openbaton.nubomedia.api.openshift.json.request;

/**
 * Created by maa on 25/09/2015.
 */
public class RouteConfig {

    private final String kind = "Route";
    private final String  apiVersion = "v1";

    private Metadata metadata;

    private RouteSpec spec;

    public RouteConfig(Metadata metadata, RouteSpec spec) {
        this.metadata = metadata;
        this.spec = spec;
    }

    public RouteConfig() {
    }

    public String getKind() {
        return kind;
    }

    public String getApiVersion() {
        return apiVersion;
    }

    public Metadata getMetadata() {
        return metadata;
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }

    public RouteSpec getSpec() {
        return spec;
    }

    public void setSpec(RouteSpec spec) {
        this.spec = spec;
    }
}
