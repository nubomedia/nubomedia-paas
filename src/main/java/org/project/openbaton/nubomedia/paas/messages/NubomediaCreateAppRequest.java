/*
 * Copyright (c) 2015-2016 Fraunhofer FOKUS
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.project.openbaton.nubomedia.paas.messages;

import org.project.openbaton.nubomedia.paas.model.persistence.openbaton.Flavor;
import org.project.openbaton.nubomedia.paas.model.persistence.openbaton.QoS;

import java.util.Arrays;

/**
 * Created by maa on 28.09.15.
 */
public class NubomediaCreateAppRequest {

    private String gitURL;
    private String appName;
    private NubomediaPort[] ports;
    private Flavor flavor;
    private int replicasNumber;
    private String secretName;
    private QoS qualityOfService;
    private boolean cloudRepository;
    private boolean cdnConnector;
    private boolean turnServerActivate;
    private String turnServerUrl;
    private String turnServerUsername;
    private String turnServerPassword;
    private boolean stunServerActivate;
    private String stunServerIp;
    private String stunServerPort;
    private int scaleInOut;
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

    public double getScale_out_threshold() {
        return scale_out_threshold;
    }

    public void setScale_out_threshold(double scale_out_threshold) {
        this.scale_out_threshold = scale_out_threshold;
    }

    public boolean isCdnConnector() {
        return cdnConnector;
    }

    public void setCdnConnector(boolean cdnConnector) {
        this.cdnConnector = cdnConnector;
    }

    @Override
    public String toString() {
        return "NubomediaCreateAppRequest{" +
                "gitURL='" + gitURL + '\'' +
                ", appName='" + appName + '\'' +
                ", ports=" + Arrays.toString(ports) +
                ", flavor=" + flavor +
                ", replicasNumber=" + replicasNumber +
                ", secretName='" + secretName + '\'' +
                ", qualityOfService=" + qualityOfService +
                ", cloudRepository=" + cloudRepository +
                ", turnServerIp='" + turnServerUrl + '\'' +
                ", turnServerUsername='" + turnServerUsername + '\'' +
                ", turnServerPassword='" + turnServerPassword + '\'' +
                ", scaleInOut=" + scaleInOut +
                ", scale_out_threshold=" + scale_out_threshold +
                '}';
    }
}
