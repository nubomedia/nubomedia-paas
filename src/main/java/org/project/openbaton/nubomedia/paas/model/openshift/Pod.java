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

package org.project.openbaton.nubomedia.paas.model.openshift;

/**
 * Created by maa on 15.10.15.
 */
public class Pod {

  private final String kind = "Pod";
  private final String apiVersion = "v1";
  private Metadata metadata;
  private PodStatus status;

  public static class PodStatus {

    private String phase;
    private String hostIP;
    private String podIP;

    public PodStatus() {}

    public PodStatus(String phase, String hostIP, String podIP) {
      this.phase = phase;
      this.hostIP = hostIP;
      this.podIP = podIP;
    }

    public String getPhase() {
      return phase;
    }

    public void setPhase(String phase) {
      this.phase = phase;
    }

    public String getHostIP() {
      return hostIP;
    }

    public void setHostIP(String hostIP) {
      this.hostIP = hostIP;
    }

    public String getPodIP() {
      return podIP;
    }

    public void setPodIP(String podIP) {
      this.podIP = podIP;
    }
  }

  public Pod(Metadata metadata, PodStatus status) {
    this.metadata = metadata;
    this.status = status;
  }

  public Pod() {}

  public String getKind() {
    return kind;
  }

  public String getApiVersion() {
    return apiVersion;
  }

  public Metadata getMetadata() {
    return metadata;
  }

  public void setMetadata(Metadata metadata) {
    this.metadata = metadata;
  }

  public PodStatus getStatus() {
    return status;
  }

  public void setStatus(PodStatus status) {
    this.status = status;
  }
}
