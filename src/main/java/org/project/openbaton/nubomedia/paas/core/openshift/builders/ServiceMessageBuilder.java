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

package org.project.openbaton.nubomedia.paas.core.openshift.builders;

import org.project.openbaton.nubomedia.paas.model.openshift.Metadata;
import org.project.openbaton.nubomedia.paas.model.openshift.Selector;
import org.project.openbaton.nubomedia.paas.model.openshift.ServiceConfig;
import org.project.openbaton.nubomedia.paas.model.openshift.ServiceSpec;
import org.project.openbaton.nubomedia.paas.model.persistence.Port;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by maa on 25/09/2015.
 */
public class ServiceMessageBuilder {

  private String namespace;
  private String osName;
  private List<Port> ports;

  public ServiceMessageBuilder(String namespace, String osName, List<Port> ports) {
    this.namespace = namespace;
    this.osName = osName;
    this.ports = ports;
  }

  public ServiceConfig buildMessage() {
    List<ServiceSpec.ServicePort> sPorts = new ArrayList<>();

    //    if (ports == null) {
    for (Port port : ports) {
      sPorts.add(
          new ServiceSpec.ServicePort(
              port.getProtocol(),
              port.getPort(),
              port.getTargetPort(),
              port.getProtocol().toLowerCase() + "-" + port.getTargetPort()));
      //      for (int i = 0; i < targetPorts.size(); i++) {
      //        sPorts[i] =
      //            new ServiceSpec.ServicePort(
      //                protocols.get(i),
      //                targetPorts.get(i),
      //                targetPorts.get(i),
      //                protocols.get(i).toLowerCase() + "-" + targetPorts.get(i));
      //      }
      //    } else {
      //      for (int i = 0; i < targetPorts.size(); i++) {
      //
      //        if (ports.get(i) == 0) {
      //          sPorts[i] =
      //              new ServiceSpec.ServicePort(
      //                  protocols.get(i),
      //                  targetPorts.get(i),
      //                  targetPorts.get(i),
      //                  protocols.get(i).toLowerCase() + "-" + targetPorts.get(i));
      //        }
      //
      //        sPorts[i] =
      //            new ServiceSpec.ServicePort(
      //                protocols.get(i),
      //                ports.get(i),
      //                targetPorts.get(i),
      //                protocols.get(i).toLowerCase() + "-" + targetPorts.get(i));
      //      }
      //    }
    }
    Metadata metadata = new Metadata(osName + "-svc", "", "", namespace);
    Selector selector = new Selector(osName);
    ServiceSpec spec = new ServiceSpec(selector, sPorts);

    return new ServiceConfig(metadata, spec);
  }
}
