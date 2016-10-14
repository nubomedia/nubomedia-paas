/*
 *
 *  * (C) Copyright 2016 NUBOMEDIA (http://www.nubomedia.eu)
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *   http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *  *
 *
 */

package org.project.openbaton.nubomedia.paas.model.persistence;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.openbaton.catalogue.util.IdGenerator;
import org.project.openbaton.nubomedia.paas.messages.AppStatus;
import org.project.openbaton.nubomedia.paas.model.persistence.openbaton.Flavor;
import org.project.openbaton.nubomedia.paas.model.persistence.openbaton.MediaServerGroup;
import org.project.openbaton.nubomedia.paas.model.persistence.openbaton.QoS;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Created by maa on 28.09.15.
 */
@Entity
public class Application {

  @Id private String id;
  private String name;
  private String osName;
  private String projectName;
  private String projectId;
  private String route;
  private String gitURL;
  private int replicasNumber;
  private String secretName;
  private Flavor flavor;
  private AppStatus status;

  private QoS qualityOfService;
  private boolean turnServerActivate;
  private String turnServerUrl;
  private String turnServerUsername;
  private String turnServerPassword;
  private boolean stunServerActivate;
  private String stunServerIp;
  private String stunServerPort;

  private boolean cdnConnector;
  private boolean cloudRepository;

  private int scaleOutLimit;

  private double scaleOutThreshold;

  private String createdBy;

  private Date createdAt;

  @JsonIgnore private boolean resourceOK;

  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  private List<Port> ports;

  //  @ElementCollection(fetch = FetchType.EAGER)
  //  private List<Integer> targetPorts;
  //
  //  @ElementCollection(fetch = FetchType.EAGER)
  //  private List<Integer> ports;
  //
  //  @ElementCollection(fetch = FetchType.EAGER)
  //  private List<String> protocols;

  @ElementCollection(fetch = FetchType.EAGER)
  private List<String> podList;

  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  private List<SupportingService> services;

  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  private List<EnvironmentVariable> envVars;

  @OneToOne(cascade = CascadeType.ALL)
  private MediaServerGroup mediaServerGroup;

  public Application(
      Flavor flavor,
      String name,
      String osName,
      String projectName,
      String projectId,
      String route,
      String gitURL,
      boolean cdnConnector,
      boolean cloudRepository,
      int scaleOutLimit,
      double scaleOutThreshold,
      String createdBy,
      Date createdAt,
      List<String> podList,
      int replicasNumber,
      String secretName,
      boolean resourceOK) {
    this.flavor = flavor;
    this.name = name;
    this.osName = osName;
    this.projectName = projectName;
    this.projectId = projectId;
    this.route = route;
    this.gitURL = gitURL;
    this.cdnConnector = cdnConnector;
    this.cloudRepository = cloudRepository;
    this.scaleOutLimit = scaleOutLimit;
    this.scaleOutThreshold = scaleOutThreshold;
    this.createdBy = createdBy;
    this.createdAt = createdAt;
    //    this.targetPorts = targetPorts;

    //    if (ports == null) {
    //      this.ports = targetPorts;
    //    } else {
    //      this.ports = ports;
    //    }

    this.podList = podList;
    //    this.protocols = protocols;
    this.replicasNumber = replicasNumber;
    this.secretName = secretName;
    this.status = AppStatus.CREATED;
    this.resourceOK = resourceOK;
  }

  public Application() {}

  @PrePersist
  public void ensureId() {
    id = IdGenerator.createUUID();
  }

  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getOsName() {
    return osName;
  }

  public void setOsName(String osName) {
    this.osName = osName;
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

  public MediaServerGroup getMediaServerGroup() {
    return mediaServerGroup;
  }

  public void setMediaServerGroup(MediaServerGroup mediaServerGroup) {
    this.mediaServerGroup = mediaServerGroup;
  }

  public String getGitURL() {
    return gitURL;
  }

  public void setGitURL(String gitURL) {
    this.gitURL = gitURL;
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

  public Flavor getFlavor() {
    return flavor;
  }

  public void setFlavor(Flavor flavor) {
    this.flavor = flavor;
  }

  public AppStatus getStatus() {
    return status;
  }

  public void setStatus(AppStatus status) {
    this.status = status;
  }

  public boolean isResourceOK() {
    return resourceOK;
  }

  public void setResourceOK(boolean resourceOK) {
    this.resourceOK = resourceOK;
  }

  //  public List<Integer> getTargetPorts() {
  //    return targetPorts;
  //  }
  //
  //  public void setTargetPorts(List<Integer> targetPorts) {
  //    this.targetPorts = targetPorts;
  //  }
  //
  //  public List<Integer> getPorts() {
  //    return ports;
  //  }
  //
  //  public void setPorts(List<Integer> ports) {
  //    this.ports = ports;
  //  }
  //
  //  public List<String> getProtocols() {
  //    return protocols;
  //  }
  //
  //  public void setProtocols(List<String> protocols) {
  //    this.protocols = protocols;
  //  }

  public List<String> getPodList() {
    return podList;
  }

  public void setPodList(List<String> podList) {
    this.podList = podList;
  }

  public String getProjectId() {
    return projectId;
  }

  public void setProjectId(String projectId) {
    this.projectId = projectId;
  }

  public Date getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Date createdAt) {
    this.createdAt = createdAt;
  }

  public boolean isCdnConnector() {
    return cdnConnector;
  }

  public void setCdnConnector(boolean cdnConnector) {
    this.cdnConnector = cdnConnector;
  }

  public boolean isCloudRepository() {
    return cloudRepository;
  }

  public void setCloudRepository(boolean cloudRepository) {
    this.cloudRepository = cloudRepository;
  }

  public int getScaleOutLimit() {
    return scaleOutLimit;
  }

  public void setScaleOutLimit(int scaleOutLimit) {
    this.scaleOutLimit = scaleOutLimit;
  }

  public double getScaleOutThreshold() {
    return scaleOutThreshold;
  }

  public void setScaleOutThreshold(double scaleOutThreshold) {
    this.scaleOutThreshold = scaleOutThreshold;
  }

  public QoS getQualityOfService() {
    return qualityOfService;
  }

  public void setQualityOfService(QoS qualityOfService) {
    this.qualityOfService = qualityOfService;
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

  public List<SupportingService> getServices() {
    return services;
  }

  public void setServices(List<SupportingService> services) {
    this.services = services;
  }

  public List<EnvironmentVariable> getEnvVars() {
    return envVars;
  }

  public void setEnvVars(List<EnvironmentVariable> envVars) {
    this.envVars = envVars;
  }

  public List<Port> getPorts() {
    return ports;
  }

  public void setPorts(List<Port> ports) {
    this.ports = ports;
  }

  @Override
  public String toString() {
    return "Application{"
        + "id='"
        + id
        + '\''
        + ", name='"
        + name
        + '\''
        + ", osName='"
        + osName
        + '\''
        + ", projectName='"
        + projectName
        + '\''
        + ", projectId='"
        + projectId
        + '\''
        + ", route='"
        + route
        + '\''
        + ", gitURL='"
        + gitURL
        + '\''
        + ", replicasNumber="
        + replicasNumber
        + ", secretName='"
        + secretName
        + '\''
        + ", flavor="
        + flavor
        + ", status="
        + status
        + ", qualityOfService="
        + qualityOfService
        + ", turnServerActivate="
        + turnServerActivate
        + ", turnServerUrl='"
        + turnServerUrl
        + '\''
        + ", turnServerUsername='"
        + turnServerUsername
        + '\''
        + ", turnServerPassword='"
        + turnServerPassword
        + '\''
        + ", stunServerActivate="
        + stunServerActivate
        + ", stunServerIp='"
        + stunServerIp
        + '\''
        + ", stunServerPort='"
        + stunServerPort
        + '\''
        + ", cdnConnector="
        + cdnConnector
        + ", cloudRepository="
        + cloudRepository
        + ", scaleOutLimit="
        + scaleOutLimit
        + ", scaleOutThreshold="
        + scaleOutThreshold
        + ", createdBy='"
        + createdBy
        + '\''
        + ", createdAt="
        + createdAt
        + ", resourceOK="
        + resourceOK
        + ", ports="
        + ports
        + ", podList="
        + podList
        + ", services="
        + services
        + ", envVars="
        + envVars
        + ", mediaServerGroup="
        + mediaServerGroup
        + '}';
  }

  public String getCreatedBy() {
    return createdBy;
  }

  public void setCreatedBy(String createdBy) {
    this.createdBy = createdBy;
  }
}
