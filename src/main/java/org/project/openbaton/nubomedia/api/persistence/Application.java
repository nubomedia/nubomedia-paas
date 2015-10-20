package org.project.openbaton.nubomedia.api.persistence;

import org.project.openbaton.nubomedia.api.messages.BuildingStatus;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created by maa on 28.09.15.
 */
@Entity
public class Application {

    @Id
    private String appID;
    private String appName;
    private String projectName;
    private String route;
    private String groupID;
    private String gitURL;
    private int[] targetPorts;
    private int[] ports;
    private String[] protocols;
    private int replicasNumber;
    private String secretName;
    private String flavor;
    private BuildingStatus status;

    public Application(String appID,String flavor, String appName, String projectName, String route, String groupID, String gitURL, int[] targetPorts, int[] ports, String[] protocols, int replicasNumber, String secretName) {
        this.appID = appID;
        this.flavor = flavor;
        this.appName = appName;
        this.projectName = projectName;
        this.route = route;
        this.groupID = groupID;
        this.gitURL = gitURL;
        this.targetPorts = targetPorts;

        if(ports == null){
            this.ports = targetPorts;
        }
        else{
            this.ports = ports;
        }

        this.protocols = protocols;
        this.replicasNumber = replicasNumber;
        this.secretName = secretName;
        this.status = BuildingStatus.CREATED;
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

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public String getGroupID() {
        return groupID;
    }

    public void setGroupID(String groupID) {
        this.groupID = groupID;
    }

    public String getGitURL() {
        return gitURL;
    }

    public void setGitURL(String gitURL) {
        this.gitURL = gitURL;
    }

    public int[] getTargetPorts() {
        return targetPorts;
    }

    public void setTargetPorts(int[] targetPorts) {
        this.targetPorts = targetPorts;
    }

    public int[] getPorts() {
        return ports;
    }

    public void setPorts(int[] ports) {
        this.ports = ports;
    }

    public String[] getProtocols() {
        return protocols;
    }

    public void setProtocols(String[] protocols) {
        this.protocols = protocols;
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

    public String getFlavor() {
        return flavor;
    }

    public void setFlavor(String flavor) {
        this.flavor = flavor;
    }

    public String getAppID() {
        return appID;
    }

    public void setAppID(String appID) {
        this.appID = appID;
    }

    public BuildingStatus getStatus() {
        return status;
    }

    public void setStatus(BuildingStatus status) {
        this.status = status;
    }
}
