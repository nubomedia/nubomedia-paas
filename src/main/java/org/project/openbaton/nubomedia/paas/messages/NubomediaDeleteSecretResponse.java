package org.project.openbaton.nubomedia.paas.messages;

/**
 * Created by maa on 09.10.15.
 */
public class NubomediaDeleteSecretResponse {

    private String secretName;
    private String projectName;
    private int code;

    public NubomediaDeleteSecretResponse(String secretName, String projectName, int code) {
        this.secretName = secretName;
        this.projectName = projectName;
        this.code = code;
    }

    public NubomediaDeleteSecretResponse() {
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getSecretName() {
        return secretName;
    }

    public void setSecretName(String secretName) {
        this.secretName = secretName;
    }
}
