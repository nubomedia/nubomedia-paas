package org.project.openbaton.nubomedia.paas.model.openshift;

import java.util.List;

/**
 * Created by maa on 01.10.15.
 */
public class ServiceAccountList {

    private final String kind = "ServiceAccountList";
    private final String apiVersion = "v1";
    private Metadata metadata;
    private List<ServiceAccount> items;

    public ServiceAccountList() {
    }

    public ServiceAccountList(Metadata metadata, List<ServiceAccount> items) {
        this.metadata = metadata;
        this.items = items;
    }

    public String getKind() {
        return kind;
    }

    public String getApiVersion() {
        return apiVersion;
    }

    public List<ServiceAccount> getItems() {
        return items;
    }

    public void setItems(List<ServiceAccount> items) {
        this.items = items;
    }

    public Metadata getMetadata() {
        return metadata;
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }
}
