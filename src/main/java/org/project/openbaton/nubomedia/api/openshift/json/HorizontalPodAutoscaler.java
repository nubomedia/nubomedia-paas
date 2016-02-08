package org.project.openbaton.nubomedia.api.openshift.json;

/**
 * Created by maa on 07.02.16.
 */
public class HorizontalPodAutoscaler {

    private final String kind = "HorizontalPodAutoscaler";
    private final String apiVersion = "extensions/v1beta1"; //TODO Change when are official
    private Metadata metadata;
    private HPASpec spec;

    public HorizontalPodAutoscaler(Metadata metadata, HPASpec spec) {
        this.metadata = metadata;
        this.spec = spec;
    }

    public HorizontalPodAutoscaler() {
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

    public HPASpec getSpec() {
        return spec;
    }

    public void setSpec(HPASpec spec) {
        this.spec = spec;
    }
}
