package org.project.openbaton.nubomedia.api.openshift.json;

/**
 * Created by maa on 01.10.15.
 */
public class ServiceAccountList {

    private final String kind = "ServiceAccountList";
    private final String apiVersion = "v1";
    private Metadata metadata;
    private ServiceAccount[] items;

    public ServiceAccountList() {
    }

    public ServiceAccountList(Metadata metadata, ServiceAccount[] items) {
        this.metadata = metadata;
        this.items = items;
    }

    public String getKind() {
        return kind;
    }

    public String getApiVersion() {
        return apiVersion;
    }

    public ServiceAccount[] getItems() {
        return items;
    }

    public void setItems(ServiceAccount[] items) {
        this.items = items;
    }

    public Metadata getMetadata() {
        return metadata;
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }
}
