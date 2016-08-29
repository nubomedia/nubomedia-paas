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

package org.project.openbaton.nubomedia.paas.core.openshift.builders;

import org.project.openbaton.nubomedia.paas.model.openshift.Metadata;
import org.project.openbaton.nubomedia.paas.model.openshift.Selector;
import org.project.openbaton.nubomedia.paas.model.openshift.ServiceConfig;
import org.project.openbaton.nubomedia.paas.model.openshift.ServiceSpec;

/**
 * Created by maa on 25/09/2015.
 */
public class ServiceMessageBuilder {

  private String osName;
  private String[] protocols;
  private int[] targetPorts;
  private int[] ports;

  public ServiceMessageBuilder(String osName, String[] protocols, int[] ports, int[] targetPorts) {
    this.osName = osName;
    this.protocols = protocols;
    this.targetPorts = targetPorts;
    this.ports = ports;
  }

  public ServiceConfig buildMessage() {
    ServiceSpec.ServicePort[] sPorts = new ServiceSpec.ServicePort[targetPorts.length];

    if (ports == null) {
      for (int i = 0; i < targetPorts.length; i++) {
        sPorts[i] =
            new ServiceSpec.ServicePort(
                protocols[i],
                targetPorts[i],
                targetPorts[i],
                protocols[i].toLowerCase() + "-" + targetPorts[i]);
      }
    } else {
      for (int i = 0; i < targetPorts.length; i++) {

        if (ports[i] == 0) {
          sPorts[i] =
              new ServiceSpec.ServicePort(
                  protocols[i],
                  targetPorts[i],
                  targetPorts[i],
                  protocols[i].toLowerCase() + "-" + targetPorts[i]);
        }

        sPorts[i] =
            new ServiceSpec.ServicePort(
                protocols[i],
                ports[i],
                targetPorts[i],
                protocols[i].toLowerCase() + "-" + targetPorts[i]);
      }
    }
    Metadata metadata = new Metadata(osName + "-svc", "", "");
    Selector selector = new Selector(osName);
    ServiceSpec spec = new ServiceSpec(selector, sPorts);

    return new ServiceConfig(metadata, spec);
  }
}
