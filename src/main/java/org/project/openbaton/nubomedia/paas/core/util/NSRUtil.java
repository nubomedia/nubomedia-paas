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

package org.project.openbaton.nubomedia.paas.core.util;

import org.openbaton.catalogue.mano.common.Ip;
import org.openbaton.catalogue.mano.descriptor.VirtualDeploymentUnit;
import org.openbaton.catalogue.mano.record.VNFCInstance;
import org.openbaton.catalogue.mano.record.VirtualNetworkFunctionRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Iterator;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by gca on 08/06/16.
 */
public class NSRUtil {
  private static final Logger logger = LoggerFactory.getLogger(NSRUtil.class);

  public static Set<String> getFloatingIPs(VirtualNetworkFunctionRecord vnfr) {
    Set<String> floatingIPs = new HashSet<>();
    for (VirtualDeploymentUnit vdu : vnfr.getVdu()) {
      for (VNFCInstance instance : vdu.getVnfc_instance()) {
        logger.debug("found instance " + instance.getHostname() + " getting IPs");
        Iterator<Ip> ipIterator = instance.getFloatingIps().iterator();
        if (ipIterator.hasNext()) {
          floatingIPs.add(ipIterator.next().getIp());
        } else {
          logger.warn("Not found any floating ip for " + instance.getHostname());
        }
      }
    }
    return floatingIPs;
  }

  public static Set<String> getHostnames(VirtualNetworkFunctionRecord vnfr) {
    Set<String> hostnames = new HashSet<>();
    for (VirtualDeploymentUnit vdu : vnfr.getVdu()) {
      for (VNFCInstance instance : vdu.getVnfc_instance()) {
        logger.debug("found instance " + instance.getHostname());
        hostnames.add(instance.getHostname());
      }
    }
    return hostnames;
  }

  public static String getIP(VirtualNetworkFunctionRecord record) throws Exception {
    for (VirtualDeploymentUnit vdu : record.getVdu()) {
      for (VNFCInstance instance : vdu.getVnfc_instance()) {
        for (Ip ip : instance.getFloatingIps()) {
          if (ip != null) return ip.getIp();
        }
      }
    }
    throw new Exception("IP was not found");
  }
}
