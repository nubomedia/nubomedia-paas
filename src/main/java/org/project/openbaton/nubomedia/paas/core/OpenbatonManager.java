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

package org.project.openbaton.nubomedia.paas.core;

import org.omg.CosNaming.NamingContextPackage.NotFound;
import org.openbaton.catalogue.mano.common.ConnectionPoint;
import org.openbaton.catalogue.mano.common.LifecycleEvent;
import org.openbaton.catalogue.mano.descriptor.*;
import org.openbaton.catalogue.mano.record.NetworkServiceRecord;
import org.openbaton.catalogue.mano.record.Status;
import org.openbaton.catalogue.mano.record.VNFCInstance;
import org.openbaton.catalogue.mano.record.VirtualNetworkFunctionRecord;
import org.openbaton.catalogue.nfvo.Configuration;
import org.openbaton.catalogue.nfvo.VimInstance;
import org.openbaton.sdk.NFVORequestor;
import org.openbaton.sdk.api.exception.SDKException;
import org.project.openbaton.nubomedia.paas.core.util.NSDUtil;
import org.project.openbaton.nubomedia.paas.core.util.NSRUtil;
import org.project.openbaton.nubomedia.paas.core.util.VIMUtil;
import org.project.openbaton.nubomedia.paas.exceptions.BadRequestException;
import org.project.openbaton.nubomedia.paas.exceptions.NotFoundException;
import org.project.openbaton.nubomedia.paas.exceptions.StateException;
import org.project.openbaton.nubomedia.paas.exceptions.openbaton.StunServerException;
import org.project.openbaton.nubomedia.paas.exceptions.openbaton.turnServerException;
import org.project.openbaton.nubomedia.paas.messages.AppStatus;
import org.project.openbaton.nubomedia.paas.model.persistence.Application;
import org.project.openbaton.nubomedia.paas.model.persistence.openbaton.Flavor;
import org.project.openbaton.nubomedia.paas.model.persistence.openbaton.Host;
import org.project.openbaton.nubomedia.paas.model.persistence.openbaton.MediaServerGroup;
import org.project.openbaton.nubomedia.paas.model.persistence.openbaton.QoS;
import org.project.openbaton.nubomedia.paas.properties.KmsProperties;
import org.project.openbaton.nubomedia.paas.properties.VimProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.*;

/**
 * Created by lto on 24/09/15.
 */
@Service
public class OpenbatonManager {

  @Autowired private NFVORequestor nfvoRequestor;

  @Autowired private VimProperties vimProperties;

  @Autowired private KmsProperties kmsProperties;

  private Logger logger = LoggerFactory.getLogger(this.getClass());

  @PostConstruct
  private void init() throws IOException, SDKException {

    VimInstance vimInstance = VIMUtil.getVimInstance(vimProperties);
    try {
      this.logger.warn("Check if VIM Instance " + vimInstance.getName() + " exists already");
      List<VimInstance> instances = nfvoRequestor.getVimInstanceAgent().findAll();
      boolean foundVIM = false;
      for (VimInstance instance : instances) {
        if (vimInstance.getName().equals(instance.getName())) {
          foundVIM = true;
          logger.info("Found VIM Instance " + vimInstance.getName() + ". Updating ...");
          if (!vimInstance.getAuthUrl().equals(instance.getAuthUrl())
              && !vimInstance.getUsername().equals(instance.getUsername())
              && !vimInstance.getPassword().equals(instance.getPassword())) {
            nfvoRequestor.getVimInstanceAgent().update(vimInstance, instance.getId());
            logger.info("VIM Instance " + vimInstance.getName() + " updated.");
          } else {
            vimInstance = instance;
            logger.info("VIM Instance already up-to-date");
          }
          break;
        }
      }
      if (foundVIM == false) {
        this.logger.debug("Not found VIM Instance. Adding VIM Instance " + vimInstance.getName());
        vimInstance = this.nfvoRequestor.getVimInstanceAgent().create(vimInstance);
        logger.info("VIM instance created");
      }
    } catch (SDKException | ClassNotFoundException e1) {
      e1.printStackTrace();
    }
  }

  public MediaServerGroup createMediaServerGroup(
      String appName,
      String osName,
      int numberOfInstances,
      Flavor flavorID,
      String callbackUrl,
      boolean cloudRepositorySet,
      boolean cdnConnectorSet,
      QoS qos,
      boolean turnServerActivate,
      String serverTurnIp,
      String serverTurnUsername,
      String serverTurnPassword,
      boolean stunServerActivate,
      String stunServerIp,
      String stunServerPort,
      int scaleInOut,
      double scale_out_threshold)
      throws SDKException, turnServerException, StunServerException {
    logger.debug(
        "Creating Media Server Group with name: "
            + appName
            + " with: FlavorID "
            + flavorID
            + " callbackURL "
            + callbackUrl
            + " isCloudRepo "
            + cloudRepositorySet
            + " QOS "
            + qos
            + "turnServerIp "
            + serverTurnIp
            + " serverTurnName "
            + serverTurnUsername
            + " scaleInOut "
            + scaleInOut);
    MediaServerGroup mediaServerGroup = new MediaServerGroup();
    // building network service descriptor
    NetworkServiceDescriptor targetNSD =
        NSDUtil.getNSD(
            numberOfInstances,
            flavorID,
            qos,
            turnServerActivate,
            serverTurnIp,
            serverTurnUsername,
            serverTurnPassword,
            stunServerActivate,
            stunServerIp,
            stunServerPort,
            scaleInOut,
            scale_out_threshold,
            kmsProperties,
            vimProperties);
    if (cloudRepositorySet && !cdnConnectorSet) {
      Set<VirtualNetworkFunctionDescriptor> vnfds = targetNSD.getVnfd();
      vnfds.add(NSDUtil.getCloudRepoVnfd());
      logger.debug("VNFDS " + vnfds.toString());
      targetNSD.setVnfd(vnfds);
    } else if (cdnConnectorSet) {
      Set<VirtualNetworkFunctionDescriptor> vnfds = targetNSD.getVnfd();
      VirtualNetworkFunctionDescriptor cdnConnectorVnfd = NSDUtil.getCloudRepoVnfd();
      Set<LifecycleEvent> lifecycleEvents = new HashSet<>();
      for (LifecycleEvent lce : cdnConnectorVnfd.getLifecycle_event()) {
        if (lce.getEvent().name().equals("START")) {
          List<String> lces = lce.getLifecycle_events();
          lces.add("start-cdn.sh");
        }
        lifecycleEvents.add(lce);
      }
      cdnConnectorVnfd.setLifecycle_event(lifecycleEvents);

      vnfds.add(cdnConnectorVnfd);
      targetNSD.setVnfd(vnfds);
    }
    //Set names
    targetNSD.setName(appName + "-" + osName);
    for (VirtualNetworkFunctionDescriptor vnfd : targetNSD.getVnfd()) {
      vnfd.setName(vnfd.getName() + "-" + osName);
      for (VirtualDeploymentUnit vdu : vnfd.getVdu()) {
        vdu.setName("vdu-" + osName);
      }
    }
    targetNSD = nfvoRequestor.getNetworkServiceDescriptorAgent().create(targetNSD);
    mediaServerGroup.setNsdID(targetNSD.getId());
    HashMap<String, ArrayList<String>> vduVimInstances = new HashMap<>();
    for (VirtualNetworkFunctionDescriptor vnfd : targetNSD.getVnfd()) {
      for (VirtualDeploymentUnit vdu : vnfd.getVdu()) {
        vduVimInstances.put(vdu.getName(), (ArrayList<String>) vdu.getVimInstanceName());
      }
    }
    ArrayList<String> keys = new ArrayList<>();
    //    keys.add(vimProperties.getKeypair());
    HashMap<String, Configuration> configurations = new HashMap();

    NetworkServiceRecord nsr =
        nfvoRequestor
            .getNetworkServiceRecordAgent()
            .create(targetNSD.getId(), vduVimInstances, keys, configurations);
    logger.debug("NSR " + nsr.toString());
    mediaServerGroup.setNsrID(nsr.getId());
    logger.debug("Result " + mediaServerGroup.toString());
    return mediaServerGroup;
  }

  public MediaServerGroup updateMediaServerGroup(Application application) throws Exception {
    NetworkServiceRecord nsr = null;
    try {
      nsr =
          nfvoRequestor
              .getNetworkServiceRecordAgent()
              .findById(application.getMediaServerGroup().getNsrID());
    } catch (SDKException | ClassNotFoundException e) {
      String message = "no NSR found, the media server group cannot be updated";
      logger.debug(message);
      throw new Exception(message);
    }
    application.getMediaServerGroup().setStatus(nsr.getStatus());
    for (VirtualNetworkFunctionRecord record : nsr.getVnfr()) {
      if (record.getEndpoint().equals("media-server")) {
        logger.debug("found record media-server");
        Set<Host> updatedHosts = new HashSet<>();
        Set<Host> receivedHosts = NSRUtil.getHosts(record);
        Set<Host> oldHosts = application.getMediaServerGroup().getHosts();
        for (Host receivedHost : receivedHosts) {
          boolean foundHost = false;
          for (Host oldHost : oldHosts) {
            if (receivedHost.getHostname().equals(oldHost.getHostname())) {
              oldHost.setStatus(receivedHost.getStatus());
              oldHost.setFloatingIp(receivedHost.getFloatingIp());
              updatedHosts.add(oldHost);
              foundHost = true;
              break;
            }
          }
          if (!foundHost) {
            updatedHosts.add(receivedHost);
          }
        }
        application.getMediaServerGroup().setHosts(updatedHosts);
        //        application.getMediaServerGroup().setFloatingIPs(NSRUtil.getFloatingIPs(record));
        //        application.getMediaServerGroup().setHostnames(NSRUtil.getHostnames(record));
      }
    }
    return application.getMediaServerGroup();
  }

  public AppStatus getStatus(String nsrID) {
    NetworkServiceRecord nsr = null;
    AppStatus res = AppStatus.CREATED;
    try {
      nsr = nfvoRequestor.getNetworkServiceRecordAgent().findById(nsrID);
    } catch (SDKException | ClassNotFoundException e) {
      logger.debug("no NSR found");
      res = AppStatus.FAILED;
    }

    if (nsr != null) {
      if (nsr.getStatus() != null) {
        switch (nsr.getStatus()) {
          case NULL:
            res = AppStatus.CREATED;
            break;
          case INITIALIZED:
            res = AppStatus.INITIALIZING;
            break;
          case ERROR:
            res = AppStatus.FAILED;
            break;
          case ACTIVE:
            res = AppStatus.INITIALISED;
            break;
        }
      } else {
        res = AppStatus.FAILED;
      }
    } else {
      res = AppStatus.FAILED;
    }

    return res;
  }

  public boolean existRecord(String nsrID) {

    try {
      return nfvoRequestor.getNetworkServiceRecordAgent().findById(nsrID) != null;

    } catch (SDKException | ClassNotFoundException e) {
      e.printStackTrace();
      return false;
    }
  }

  public void deleteRecord(String nsrID) {
    try {
      nfvoRequestor.getNetworkServiceRecordAgent().delete(nsrID);
    } catch (SDKException e) {
      e.printStackTrace();
    }
  }

  public void deleteDescriptor(String nsdID) {
    try {
      this.nfvoRequestor.getNetworkServiceDescriptorAgent().delete(nsdID);
    } catch (SDKException e) {
      e.printStackTrace();
    }
  }

  public void startVnfcInstance(Application app, String hostname)
      throws SDKException, NotFoundException, BadRequestException {
    NetworkServiceRecord nsr = null;
    try {
      nsr =
          nfvoRequestor
              .getNetworkServiceRecordAgent()
              .findById(app.getMediaServerGroup().getNsrID());
    } catch (ClassNotFoundException e) {
      throw new NotFoundException("Not found NSR " + app.getMediaServerGroup().getNsrID());
    }
    boolean foundKMS = false;
    for (VirtualNetworkFunctionRecord vnfr : nsr.getVnfr()) {
      if (vnfr.getType().equals("media-server")) {
        for (VirtualDeploymentUnit msVdu : vnfr.getVdu()) {
          for (VNFCInstance mediaServer : msVdu.getVnfc_instance()) {
            if (mediaServer.getHostname().equals(hostname)) {
              foundKMS = true;
              if (mediaServer.getState().equals("INACTIVE")) {
                nfvoRequestor
                    .getNetworkServiceRecordAgent()
                    .startVNFCInstance(
                        nsr.getId(), vnfr.getId(), msVdu.getId(), mediaServer.getId());
              } else {
                throw new BadRequestException(
                    "Bad request: media server "
                        + hostname
                        + " is in wrong state. Must be INACTIVE to start KMS again");
              }
            }
          }
        }
      }
    }
    if (foundKMS == false) {
      throw new NotFoundException("Not found media server " + hostname);
    }
  }

  public void stopVnfcInstance(Application app, String hostname)
      throws SDKException, NotFoundException, BadRequestException {
    NetworkServiceRecord nsr = null;
    try {
      nsr =
          nfvoRequestor
              .getNetworkServiceRecordAgent()
              .findById(app.getMediaServerGroup().getNsrID());
    } catch (ClassNotFoundException e) {
      throw new NotFoundException("Not found NSR " + app.getMediaServerGroup().getNsrID());
    }
    boolean foundKMS = false;
    for (VirtualNetworkFunctionRecord vnfr : nsr.getVnfr()) {
      if (vnfr.getType().equals("media-server")) {
        for (VirtualDeploymentUnit msVdu : vnfr.getVdu()) {
          for (VNFCInstance mediaServer : msVdu.getVnfc_instance()) {
            if (mediaServer.getHostname().equals(hostname)) {
              foundKMS = true;
              if (mediaServer.getState().equals("ACTIVE")) {
                nfvoRequestor
                    .getNetworkServiceRecordAgent()
                    .stopVNFCInstance(
                        nsr.getId(), vnfr.getId(), msVdu.getId(), mediaServer.getId());
              } else {
                throw new BadRequestException(
                    "Bad request: media server "
                        + hostname
                        + " is in wrong state. Must be ACTIVE to stop the KMS");
              }
            }
          }
        }
      }
    }
    if (foundKMS == false) {
      throw new NotFoundException("Not found media server " + hostname);
    }
  }

  public void scaleInVnfcInstance(Application app, String hostname)
      throws SDKException, ClassNotFoundException, NotFoundException, BadRequestException {
    NetworkServiceRecord nsr =
        nfvoRequestor.getNetworkServiceRecordAgent().findById(app.getMediaServerGroup().getNsrID());
    if (nsr == null) {
      throw new NotFoundException("Not found NSR " + app.getMediaServerGroup().getNsrID());
    }
    checkScalability(nsr);
    VirtualNetworkFunctionRecord msVnfr = null;
    for (VirtualNetworkFunctionRecord vnfr : nsr.getVnfr()) {
      if (vnfr.getType().equals("media-server")) {
        msVnfr = vnfr;
      }
    }
    if (msVnfr == null) {
      throw new NotFoundException("Not found VNFR of type media-server");
    }
    VirtualDeploymentUnit msVdu = null;
    VNFCInstance vnfcInstanceToRemove = null;
    boolean scalingAllowed = false;
    for (VirtualDeploymentUnit vdu : msVnfr.getVdu()) {
      if (vdu.getVnfc_instance().size() > 1) {
        scalingAllowed = true;
        for (VNFCInstance vnfcInstance : vdu.getVnfc_instance()) {
          if (vnfcInstance.getHostname().equals(hostname)) {
            msVdu = vdu;
            vnfcInstanceToRemove = vnfcInstance;
          }
        }
      } else {
        logger.warn("Number of Media Servers reached already minimum of " + app.getName());
      }
    }
    if (scalingAllowed == false) {
      throw new BadRequestException(
          "Scaling in not permitted. Minimum number (1) of Media Servers of "
              + app.getName()
              + " reached");
    }
    if (vnfcInstanceToRemove == null) {
      throw new NotFoundException("Not found media server " + hostname + " to scale-in");
    }
    nfvoRequestor
        .getNetworkServiceRecordAgent()
        .deleteVNFCInstance(
            nsr.getId(), msVnfr.getId(), msVdu.getId(), vnfcInstanceToRemove.getId());
  }

  private void checkScalability(NetworkServiceRecord nsr) throws BadRequestException {
    if (nsr.getStatus().ordinal() == Status.ACTIVE.ordinal()) {
      logger.debug("Scaling allowed since NSR is in ACTIVE state");
    } else {
      logger.debug(
          "Scaling not allowed for since NSR is in "
              + nsr.getStatus()
              + " state. Have to be in ACTIVE state");
      throw new BadRequestException(
          "Scaling not allowed since Application is in "
              + nsr.getStatus()
              + " state. Have to be in ACTIVE state");
    }
  }

  public void scaleOutVnfcInstance(Application app)
      throws SDKException, NotFoundException, ClassNotFoundException, BadRequestException {
    NetworkServiceRecord nsr =
        nfvoRequestor.getNetworkServiceRecordAgent().findById(app.getMediaServerGroup().getNsrID());
    if (nsr == null) {
      throw new NotFoundException("Not found NSR " + app.getMediaServerGroup().getNsrID());
    }
    checkScalability(nsr);
    VirtualNetworkFunctionRecord msVnfr = null;
    for (VirtualNetworkFunctionRecord vnfr : nsr.getVnfr()) {
      if (vnfr.getType().equals("media-server")) {
        msVnfr = vnfr;
      }
    }
    if (msVnfr == null) {
      throw new NotFoundException("Not found VNFR of type media-server");
    }
    VirtualDeploymentUnit msVdu = null;
    VNFComponent vnfComponentToAdd = null;
    boolean scalingAllowed = false;
    int maximumNumber = 0;
    for (VirtualDeploymentUnit vdu : msVnfr.getVdu()) {
      maximumNumber = vdu.getScale_in_out();
      if (vdu.getScale_in_out() > vdu.getVnfc_instance().size()) {
        scalingAllowed = true;
        for (VNFComponent vnfComponent : vdu.getVnfc()) {
          msVdu = vdu;
          vnfComponentToAdd = new VNFComponent();
          Set<VNFDConnectionPoint> connectionPoints = new HashSet<>();
          for (VNFDConnectionPoint cpCopy : vnfComponent.getConnection_point()) {
            VNFDConnectionPoint newCP = new VNFDConnectionPoint();
            newCP.setVirtual_link_reference(cpCopy.getVirtual_link_reference());
            newCP.setFloatingIp(cpCopy.getFloatingIp());
            connectionPoints.add(newCP);
          }
          vnfComponentToAdd.setConnection_point(connectionPoints);
        }
      } else {
        logger.warn("Number of Media Servers reached already maximum of " + app.getName());
      }
    }
    if (scalingAllowed == false) {
      throw new BadRequestException(
          "Scaling out not permitted. Maxmimum number ("
              + maximumNumber
              + ") of Media Servers of "
              + app.getName()
              + " reached");
    }
    if (vnfComponentToAdd == null) {
      throw new NotFoundException("Not found existing VNFComponent to create a new one.");
    }
    nfvoRequestor
        .getNetworkServiceRecordAgent()
        .createVNFCInstance(nsr.getId(), msVnfr.getId(), msVdu.getId(), vnfComponentToAdd);
  }
}
