package org.project.openbaton.nubomedia.api.messages;

import org.project.openbaton.nubomedia.api.openbaton.Flavor;
import org.project.openbaton.nubomedia.api.openbaton.QoS;

import java.util.Arrays;

/**
 * Created by maa on 28.09.15.
 */
public class NubomediaCreateAppRequest {

    private String gitURL;
    private String appName;
    private String projectName;  //Replacing
    private NubomediaPort[] ports;
    private Flavor flavor;
    private int replicasNumber;
    private String secretName;
    private QoS qualityOfService;
    private boolean cloudRepository;
    private String cloudRepoPort;
    private boolean cloudRepoSecurity;
    private boolean turnServerActivate;
    private String turnServerUrl;
    private String turnServerUsername;
    private String turnServerPassword;
    private boolean stunServerActivate;
    private String stunServerIp;
    private String stunServerPort;
    private int scaleInOut;
    private double scale_in_threshold;
    private double scale_out_threshold;

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

    public Flavor getFlavor() {
        return flavor;
    }

    public void setFlavor(Flavor flavor) {
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

    public QoS getQualityOfService() {
        return qualityOfService;
    }

    public void setQualityOfService(QoS qualityOfService) {
        this.qualityOfService = qualityOfService;
    }

    public boolean isCloudRepository() {
        return cloudRepository;
    }

    public void setCloudRepository(boolean cloudRepository) {
        this.cloudRepository = cloudRepository;
    }

    public String getCloudRepoPort() {
        return cloudRepoPort;
    }

    public void setCloudRepoPort(String cloudRepoPort) {
        this.cloudRepoPort = cloudRepoPort;
    }

    public boolean isCloudRepoSecurity() {
        return cloudRepoSecurity;
    }

    public void setCloudRepoSecurity(boolean cloudRepoSecurity) {
        this.cloudRepoSecurity = cloudRepoSecurity;
    }

    public boolean isTurnServerActivate() {
        return turnServerActivate;
    }

    public void setTurnServerActivate(boolean turnServerActivate) {
        this.turnServerActivate = turnServerActivate;
    }

    public String getTurnServerUrl() {
        return turnServerUrl;
    }

    public void setTurnServerUrl(String turnServerUrl) {
        this.turnServerUrl = turnServerUrl;
    }

    public String getTurnServerUsername() {
        return turnServerUsername;
    }

    public void setTurnServerUsername(String turnServerUsername) {
        this.turnServerUsername = turnServerUsername;
    }

    public String getTurnServerPassword() {
        return turnServerPassword;
    }

    public void setTurnServerPassword(String turnServerPassword) {
        this.turnServerPassword = turnServerPassword;
    }

    public boolean isStunServerActivate() {
        return stunServerActivate;
    }

    public void setStunServerActivate(boolean stunServerActivate) {
        this.stunServerActivate = stunServerActivate;
    }

    public String getStunServerIp() {
        return stunServerIp;
    }

    public void setStunServerIp(String stunServerIp) {
        this.stunServerIp = stunServerIp;
    }

    public String getStunServerPort() {
        return stunServerPort;
    }

    public void setStunServerPort(String stunServerPort) {
        this.stunServerPort = stunServerPort;
    }

    public int getScaleInOut() {
        return scaleInOut;
    }

    public void setScaleInOut(int scaleInOut) {
        this.scaleInOut = scaleInOut;
    }

    public double getScale_in_threshold() {
        return scale_in_threshold;
    }

    public void setScale_in_threshold(double scale_in_threshold) {
        this.scale_in_threshold = scale_in_threshold;
    }

    public double getScale_out_threshold() {
        return scale_out_threshold;
    }

    public void setScale_out_threshold(double scale_out_threshold) {
        this.scale_out_threshold = scale_out_threshold;
    }

    @Override
    public String toString() {
        return "NubomediaCreateAppRequest{" +
                "gitURL='" + gitURL + '\'' +
                ", appName='" + appName + '\'' +
                ", projectName='" + projectName + '\'' +
                ", ports=" + Arrays.toString(ports) +
                ", flavor=" + flavor +
                ", replicasNumber=" + replicasNumber +
                ", secretName='" + secretName + '\'' +
                ", qualityOfService=" + qualityOfService +
                ", cloudRepository=" + cloudRepository +
                ", cloudRepoPort='" + cloudRepoPort + '\'' +
                ", cloudRepoSecurity=" + cloudRepoSecurity +
                ", turnServerIp='" + turnServerUrl + '\'' +
                ", turnServerUsername='" + turnServerUsername + '\'' +
                ", turnServerPassword='" + turnServerPassword + '\'' +
                ", scaleInOut=" + scaleInOut +
                ", scale_in_threshold=" + scale_in_threshold +
                ", scale_out_threshold=" + scale_out_threshold +
                '}';
    }
}
