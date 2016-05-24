package org.project.openbaton.nubomedia.paas.model.openshift;

import com.google.gson.JsonElement;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.codec.binary.Base64;

/**
 * Created by maa on 01.10.15.
 */
public class PrivateDockerSecret implements SecretType{

    @SerializedName(value = ".dockercfg") private String dockercfg;

    public PrivateDockerSecret() {
    }

    public PrivateDockerSecret(JsonElement jsondockercfg){
        this.dockercfg = Base64.encodeBase64String(jsondockercfg.getAsString().getBytes());
    }

    public String getDockercfg() {
        return dockercfg;
    }

    public void setDockercfg(String dockercfg) {
        this.dockercfg = dockercfg;
    }
}
