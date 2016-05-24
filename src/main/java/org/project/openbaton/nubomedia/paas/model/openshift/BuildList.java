package org.project.openbaton.nubomedia.paas.model.openshift;

/**
 * Created by maa on 08.10.15.
 */
public class BuildList {

    private final String kind = "BuildList";
    private final String apiVersion = "v1";
    private Metadata metadata;
    private Build[] items;

    public BuildList() {
    }

    public BuildList(Metadata metadata, Build[] items) {
        this.metadata = metadata;
        this.items = items;
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

    public Build[] getItems() {
        return items;
    }

    public void setItems(Build[] items) {
        this.items = items;
    }
}
