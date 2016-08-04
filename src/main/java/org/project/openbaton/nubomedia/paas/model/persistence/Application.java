/*
 *
 *  * Copyright (c) 2016 Open Baton
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *     http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package org.project.openbaton.nubomedia.paas.model.persistence;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.openbaton.catalogue.util.IdGenerator;
import org.project.openbaton.nubomedia.paas.messages.AppStatus;
import org.project.openbaton.nubomedia.paas.model.persistence.openbaton.Flavor;
import org.project.openbaton.nubomedia.paas.model.persistence.openbaton.MediaServerGroup;

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
  private String projectName;
  private String projectId;
  private String route;
  private String gitURL;
  private int replicasNumber;
  private String secretName;
  private Flavor flavor;
  private AppStatus status;

  private Date createdAt;

  @JsonIgnore private boolean resourceOK;

  @ElementCollection(fetch = FetchType.EAGER)
  private List<Integer> targetPorts;

  @ElementCollection(fetch = FetchType.EAGER)
  private List<Integer> ports;

  @ElementCollection(fetch = FetchType.EAGER)
  private List<String> protocols;

  @ElementCollection(fetch = FetchType.EAGER)
  private List<String> podList;

  @OneToOne(cascade = CascadeType.ALL)
  private MediaServerGroup mediaServerGroup;

  public Application(
      Flavor flavor,
      String name,
      String projectName,
      String projectId,
      String route,
      String nsrID,
      String gitURL,
      List<Integer> targetPorts,
      List<Integer> ports,
      List<String> protocols,
      List<String> podList,
      int replicasNumber,
      String secretName,
      boolean resourceOK) {
    this.flavor = flavor;
    this.name = name;
    this.projectName = projectName;
    this.projectId = projectId;
    this.route = route;
    this.gitURL = gitURL;
    this.targetPorts = targetPorts;

    if (ports == null) {
      this.ports = targetPorts;
    } else {
      this.ports = ports;
    }

    this.podList = podList;
    this.protocols = protocols;
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

  public List<Integer> getTargetPorts() {
    return targetPorts;
  }

  public void setTargetPorts(List<Integer> targetPorts) {
    this.targetPorts = targetPorts;
  }

  public List<Integer> getPorts() {
    return ports;
  }

  public void setPorts(List<Integer> ports) {
    this.ports = ports;
  }

  public List<String> getProtocols() {
    return protocols;
  }

  public void setProtocols(List<String> protocols) {
    this.protocols = protocols;
  }

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

  @Override
  public String toString() {
    return "Application{"
        + "id='"
        + id
        + '\''
        + ", name='"
        + name
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
        + ", createdAt="
        + createdAt
        + ", resourceOK="
        + resourceOK
        + ", targetPorts="
        + targetPorts
        + ", ports="
        + ports
        + ", protocols="
        + protocols
        + ", podList="
        + podList
        + ", mediaServerGroup="
        + mediaServerGroup
        + '}';
  }
}
