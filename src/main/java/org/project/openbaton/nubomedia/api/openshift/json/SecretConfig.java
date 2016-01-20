package org.project.openbaton.nubomedia.api.openshift.json;

import com.google.gson.annotations.SerializedName;

/**
 * Created by maa on 01.10.15.
 */
public class SecretConfig {

    private final String kind = "Secret";
    private final String apiVersion = "v1";
    private Metadata metadata;
    @SerializedName(value = "data")private SecretType secretType;

    public SecretConfig(Metadata metadata, SecretType secretType) {
        this.metadata = metadata;
        this.secretType = secretType;
    }

    public SecretConfig() {
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

    public SecretType getSecretType() {
        return secretType;
    }

    public void setSecretType(SecretType secretType) {
        this.secretType = secretType;
    }
}
