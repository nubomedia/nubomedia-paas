package org.project.openbaton.nubomedia.api.openshift.json;

/**
 * Created by Carlo on 25/09/2015.
 */
public class ServiceConfig implements MessageConfig{

    private final String kind = "Service";
    private final String apiVersion = "v1";

    private Metadata metadata;

    private ServiceSpec spec;

    public ServiceConfig(Metadata metadata, ServiceSpec spec) {
        this.metadata = metadata;
        this.spec = spec;
    }

    public ServiceConfig() {
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

    public ServiceSpec getSpec() {
        return spec;
    }

    public void setSpec(ServiceSpec spec) {
        this.spec = spec;
    }
}
