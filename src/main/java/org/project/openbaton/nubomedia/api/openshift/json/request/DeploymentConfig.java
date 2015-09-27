package org.project.openbaton.nubomedia.api.openshift.json.request;

/**
 * Created by maa on 25/09/2015.
 */
public class DeploymentConfig {

    private final String kind = "DeploymentConfig";
    private final String apiVersion = "v1";

    private Metadata metadata;

    private DeploymentConfigSpec spec;

    public DeploymentConfig(){}

    public DeploymentConfig(Metadata metadata, DeploymentConfigSpec spec) {
        this.metadata = metadata;
        this.spec = spec;
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

    public DeploymentConfigSpec getSpec() {
        return spec;
    }

    public void setSpec(DeploymentConfigSpec spec) {
        this.spec = spec;
    }
}
