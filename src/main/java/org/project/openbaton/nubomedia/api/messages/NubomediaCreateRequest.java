package org.project.openbaton.nubomedia.api.messages;

/**
 * Created by maa on 28.09.15.
 */
public class NubomediaCreateRequest {

    private String gitURL;
    private String appName;
    private String projectName;
    private String[] protocols;
    private int[] ports;
    private int[] targetPorts;
    private String flavor;
    private int replicasNumber;

    public NubomediaCreateRequest() {
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

    public String[] getProtocols() {
        return protocols;
    }

    public void setProtocols(String[] protocols) {
        this.protocols = protocols;
    }

    public int[] getPorts() {
        return ports;
    }

    public void setPorts(int[] ports) {
        this.ports = ports;
    }

    public int[] getTargetPorts() {
        return targetPorts;
    }

    public void setTargetPorts(int[] targetPorts) {
        this.targetPorts = targetPorts;
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
}
