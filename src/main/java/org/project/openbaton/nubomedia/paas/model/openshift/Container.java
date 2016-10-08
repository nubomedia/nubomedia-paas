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

package org.project.openbaton.nubomedia.paas.model.openshift;

import java.util.List;

/**
 * Created by maa on 25/09/2015.
 */
public class Container {

  private String name;
  private String image;
  private List<Port> ports;
  private List<ContainerVolume> volumeMounts;

  public static class Port {
    String protocol;
    int containerPort;

    public Port(String protocol, int containerPort) {
      this.protocol = protocol;
      this.containerPort = containerPort;
    }

    public Port() {}

    public String getProtocol() {
      return protocol;
    }

    public void setProtocol(String protocol) {
      this.protocol = protocol;
    }

    public int getContainerPort() {
      return containerPort;
    }

    public void setContainerPort(int containerPort) {
      this.containerPort = containerPort;
    }
  }

  public Container(
      String name, String image, List<Port> ports, List<ContainerVolume> volumeMounts) {
    this.name = name;
    this.image = image;
    this.ports = ports;
    this.volumeMounts = volumeMounts;
  }

  public Container(String name, String image, List<Port> ports) {
    this.name = name;
    this.image = image;
    this.ports = ports;
  }

  public Container() {}

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getImage() {
    return image;
  }

  public void setImage(String image) {
    this.image = image;
  }

  public List<Port> getPorts() {
    return ports;
  }

  public void setPorts(List<Port> ports) {
    this.ports = ports;
  }

  public List<ContainerVolume> getVolumeMounts() {
    return volumeMounts;
  }

  public void setVolumeMounts(List<ContainerVolume> volumeMounts) {
    this.volumeMounts = volumeMounts;
  }
}
