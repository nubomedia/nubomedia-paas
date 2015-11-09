package org.project.openbaton.nubomedia.api.messages;

/**
 * Created by maa on 28.09.15.
 */
public class NubomediaCreateAppRequest {

    private String gitURL;
    private String appName;
    private String projectName;
    private NubomediaPort[] ports;
    private String flavor;
    private int replicasNumber;
    private String secretName;

    public NubomediaCreateAppRequest() {
    }

    public String getGitURL() {
        return gitURL;
    }

    public void setGitURL(String gitURL) {
        this.gitURL = gitURL;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public NubomediaPort[] getPorts() {
        return ports;
    }

    public void setPorts(NubomediaPort[] ports) {
        this.ports = ports;
    }

    public String getFlavor() {
        return flavor;
    }

    public void setFlavor(String flavor) {
        this.flavor = flavor;
    }

    public int getReplicasNumber() {
        return replicasNumber;
    }

    public void setReplicasNumber(int replicasNumber) {
        this.replicasNumber = replicasNumber;
    }

    public String getSecretName() {
        return secretName;
    }

    public void setSecretName(String secretName) {
        this.secretName = secretName;
    }
}
