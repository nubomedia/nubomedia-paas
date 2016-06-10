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

package org.project.openbaton.nubomedia.paas.core;

import org.openbaton.catalogue.mano.common.*;
import org.openbaton.catalogue.mano.descriptor.NetworkServiceDescriptor;
import org.openbaton.catalogue.mano.descriptor.VirtualNetworkFunctionDescriptor;
import org.openbaton.catalogue.mano.record.NetworkServiceRecord;
import org.openbaton.catalogue.mano.record.VirtualNetworkFunctionRecord;
import org.openbaton.catalogue.nfvo.*;
import org.openbaton.sdk.NFVORequestor;
import org.openbaton.sdk.api.exception.SDKException;
import org.project.openbaton.nubomedia.paas.core.util.NSDUtil;
import org.project.openbaton.nubomedia.paas.core.util.NSRUtil;
import org.project.openbaton.nubomedia.paas.model.persistence.Application;
import org.project.openbaton.nubomedia.paas.utils.NfvoProperties;
import org.project.openbaton.nubomedia.paas.messages.AppStatus;
import org.project.openbaton.nubomedia.paas.model.persistence.openbaton.Flavor;
import org.project.openbaton.nubomedia.paas.model.persistence.openbaton.MediaServerGroup;
import org.project.openbaton.nubomedia.paas.model.persistence.openbaton.QoS;
import org.project.openbaton.nubomedia.paas.exceptions.openbaton.StunServerException;
import org.project.openbaton.nubomedia.paas.exceptions.openbaton.turnServerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.*;

/**
 * Created by lto on 24/09/15.
 */
@Service
public class OpenbatonManager {

    @Autowired
    private VimInstance vimInstance;

    @Autowired
    private NfvoProperties nfvoProperties;

    @Autowired
    private VirtualNetworkFunctionDescriptor cloudRepository;

    @Autowired
    private NSDUtil nsdUtil;

    private EventEndpoint eventEndpointCreation, eventEndpointError;

    @Autowired
    @Qualifier("networkServiceDescriptorNubo")
    private NetworkServiceDescriptor networkServiceDescriptorNubo;
    private Logger logger;
    private NFVORequestor nfvoRequestor;
    private String apiPath ;

    @PostConstruct
    private void init() throws IOException {

        this.logger = LoggerFactory.getLogger(this.getClass());
        this.nfvoRequestor = new NFVORequestor(nfvoProperties.getOpenbatonUsername(), nfvoProperties.getOpenbatonPasswd(), nfvoProperties.getOpenbatonIP(), nfvoProperties.getOpenbatonPort(), "1");
        this.apiPath = "/api/v1/nubomedia/paas";
        this.logger.info("Starting the Open Baton Manager Bean");
        try {
            this.logger.debug("Trying to create the VIM Instance " + vimInstance.getName());
            vimInstance = this.nfvoRequestor.getVimInstanceAgent().create(vimInstance);
        } catch (SDKException e){
            try {
                this.logger.warn("The VIM Instance " + vimInstance.getName() + " already exists, updating it");
                List<VimInstance> instances = nfvoRequestor.getVimInstanceAgent().findAll();
                for (VimInstance instance : instances){
                    if (vimInstance.getName().equals(instance.getName())){
                        if (!vimInstance.getAuthUrl().equals(instance.getAuthUrl()) && !vimInstance.getUsername().equals(instance.getUsername()) && !vimInstance.getPassword().equals(instance.getPassword())){
                            nfvoRequestor.getVimInstanceAgent().update(vimInstance,instance.getId());
                        }
                        else{
                            vimInstance = instance;
                        }
                    }
                }
            } catch (SDKException | ClassNotFoundException e1) {
                e1.printStackTrace();
            }
        }
        logger.debug("VIM instance created");

    }


    public MediaServerGroup createMediaServerGroup(Flavor flavorID, String appID, String callbackUrl, boolean cloudRepositorySet, boolean cdnConnectorSet, QoS qos, boolean turnServerActivate, String serverTurnIp, String serverTurnUsername, String serverTurnPassword, boolean stunServerActivate, String stunServerIp, String stunServerPort, int scaleInOut, double scale_out_threshold) throws SDKException, turnServerException, StunServerException {
        logger.debug("Creating Media Server Group with: FlavorID " + flavorID + " appID " + appID + " callbackURL " + callbackUrl + " isCloudRepo " + cloudRepositorySet + " QOS " + qos + "turnServerIp " + serverTurnIp + " serverTurnName " + serverTurnUsername + " scaleInOut " + scaleInOut);
        MediaServerGroup mediaServerGroup = new MediaServerGroup();
        NetworkServiceDescriptor targetNSD = nsdUtil.getNetworkServiceDescriptor(networkServiceDescriptorNubo,flavorID,qos,turnServerActivate, serverTurnIp,serverTurnUsername,serverTurnPassword,stunServerActivate, stunServerIp, stunServerPort, scaleInOut,scale_out_threshold);
        if (cloudRepositorySet && !cdnConnectorSet){
            Set<VirtualNetworkFunctionDescriptor> vnfds = targetNSD.getVnfd();
            vnfds.add(cloudRepository);
            logger.debug("VNFDS " + vnfds.toString());
            targetNSD.setVnfd(vnfds);
        } else if (cdnConnectorSet) {
            Set<VirtualNetworkFunctionDescriptor> vnfds = targetNSD.getVnfd();
            VirtualNetworkFunctionDescriptor cdnConnectorVnfd = cloudRepository;
            Set<LifecycleEvent> lifecycleEvents = new HashSet<>();
            for (LifecycleEvent lce : cdnConnectorVnfd.getLifecycle_event()){
                if (lce.getEvent().name().equals("START")){
                    List<String> lces = lce.getLifecycle_events();
                    lces.add("start-cdn.sh");
                }
                lifecycleEvents.add(lce);
            }
            cdnConnectorVnfd.setLifecycle_event(lifecycleEvents);

            vnfds.add(cdnConnectorVnfd);
            targetNSD.setVnfd(vnfds);
        }

        targetNSD = nfvoRequestor.getNetworkServiceDescriptorAgent().create(targetNSD);
        mediaServerGroup.setNsdID(targetNSD.getId());
        NetworkServiceRecord nsr = nfvoRequestor.getNetworkServiceRecordAgent().create(targetNSD.getId());
        logger.debug("NSR " + nsr.toString());
        mediaServerGroup.setId(nsr.getId());
        logger.debug("Result " + mediaServerGroup.toString());
        return mediaServerGroup;
    }

    public void updateMediaServerGroup(Application application) throws Exception {
        NetworkServiceRecord nsr = null;
        try {
            nsr = nfvoRequestor.getNetworkServiceRecordAgent().findById(application.getMediaServerGroup().getId());
        } catch (SDKException | ClassNotFoundException e) {
            String message = "no NSR found, the media server group cannot be updated";
            logger.debug(message);
            throw new Exception(message);
        }
        for (VirtualNetworkFunctionRecord record : nsr.getVnfr()) {

            if (record.getEndpoint().equals("media-server")) {
                logger.debug("found record media-server");
                application.getMediaServerGroup().setFloatingIPs(NSRUtil.getFloatingIPs(record));
                application.getMediaServerGroup().setHostnames(NSRUtil.getHostnames(record));
            }
        }
        return;
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
            if(nsr.getStatus() != null) {
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
            }
            else{
                res = AppStatus.FAILED;
            }
        }
        else{
            res = AppStatus.FAILED;
        }


        return res;
    }

    public boolean existRecord(String nsrID){

        try {
            if (nfvoRequestor.getNetworkServiceRecordAgent().findById(nsrID) != null){
                return true;
            }
            else{
                return false;
            }
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


    public void deleteDescriptor(String nsdID){
        try {
            this.nfvoRequestor.getNetworkServiceDescriptorAgent().delete(nsdID);
        } catch (SDKException e){
            e.printStackTrace();
        }
    }

}
