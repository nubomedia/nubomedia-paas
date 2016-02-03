package org.project.openbaton.nubomedia.api.openshift.json;

import java.util.List;

/**
 * Created by maa on 01.10.15.
 */
public class ServiceAccount {

    private final String kind = "ServiceAccount";
    private final String apiVersion = "v1";
    private Metadata metadata;
    private List<SecretID> secrets;
    private List<SecretID> imagePullSecrets;

    public ServiceAccount() {
    }

    public ServiceAccount(Metadata metadata, List<SecretID> secrets, List<SecretID> imagePullSecrets) {
        this.metadata = metadata;
        this.secrets = secrets;
        this.imagePullSecrets = imagePullSecrets;
    }

    public String getKind() {
        return kind;
    }

    public String getApiVersion() {
        return apiVersion;
    }

    public List<SecretID> getSecrets() {
        return secrets;
    }

    public void setSecrets(List<SecretID> secrets) {
        this.secrets = secrets;
    }

    public Metadata getMetadata() {
        return metadata;
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }

    public List<SecretID> getImagePullSecrets() {
        return imagePullSecrets;
    }

    public void setImagePullSecrets(List<SecretID> imagePullSecrets) {
        this.imagePullSecrets = imagePullSecrets;
    }

}
