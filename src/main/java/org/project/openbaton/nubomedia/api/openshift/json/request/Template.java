package org.project.openbaton.nubomedia.api.openshift.json.request;

/**
 * Created by maa on 25/09/2015.
 */
public class Template {

    private MetadataDeploy metadata;
    private SpecDeploy spec;

    public Template(MetadataDeploy metadata, SpecDeploy spec) {
        this.metadata = metadata;
        this.spec = spec;
    }

    public Template() {
    }

    public MetadataDeploy getMetadata() {
        return metadata;
    }

    public void setMetadata(MetadataDeploy metadata) {
        this.metadata = metadata;
    }

    public SpecDeploy getSpec() {
        return spec;
    }

    public void setSpec(SpecDeploy spec) {
        this.spec = spec;
    }
}
