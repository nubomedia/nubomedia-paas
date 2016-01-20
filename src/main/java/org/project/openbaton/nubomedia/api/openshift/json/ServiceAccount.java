package org.project.openbaton.nubomedia.api.openshift.json;

/**
 * Created by maa on 01.10.15.
 */
public class ServiceAccount {

    private final String kind = "ServiceAccount";
    private final String apiVersion = "v1";
    private Metadata metadata;
    private SecretID[] secrets;
    private SecretID[] imagePullSecrets;

    public ServiceAccount() {
    }

    public ServiceAccount(Metadata metadata, SecretID[] secrets, SecretID[] imagePullSecrets) {
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

    public SecretID[] getSecrets() {
        return secrets;
    }

    public void setSecrets(SecretID[] secrets) {
        this.secrets = secrets;
    }

    public Metadata getMetadata() {
        return metadata;
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }

    public SecretID[] getImagePullSecrets() {
        return imagePullSecrets;
    }

    public void setImagePullSecrets(SecretID[] imagePullSecrets) {
        this.imagePullSecrets = imagePullSecrets;
    }

}
