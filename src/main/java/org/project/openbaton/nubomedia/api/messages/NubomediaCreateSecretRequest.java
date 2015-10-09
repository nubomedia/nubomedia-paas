package org.project.openbaton.nubomedia.api.messages;

/**
 * Created by maa on 06.10.15.
 */
public class NubomediaCreateSecretRequest {

    private String projectName;
    private String privateKey;

    public NubomediaCreateSecretRequest() {
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }
}
