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

import org.openbaton.catalogue.mano.common.Ip;
import org.openbaton.catalogue.mano.common.LifecycleEvent;
import org.openbaton.catalogue.mano.descriptor.VirtualDeploymentUnit;
import org.openbaton.catalogue.mano.record.VNFCInstance;
import org.openbaton.catalogue.mano.record.VirtualNetworkFunctionRecord;
import org.openbaton.catalogue.nfvo.Action;
import org.openbaton.sdk.api.exception.SDKException;
import org.project.openbaton.nubomedia.paas.core.openbaton.MediaServerGroup;
import org.project.openbaton.nubomedia.paas.exceptions.openbaton.StunServerException;
import org.project.openbaton.nubomedia.paas.exceptions.openbaton.turnServerException;
import org.project.openbaton.nubomedia.paas.exceptions.openshift.DuplicatedException;
import org.project.openbaton.nubomedia.paas.exceptions.openshift.UnauthorizedException;
import org.project.openbaton.nubomedia.paas.messages.BuildingStatus;
import org.project.openbaton.nubomedia.paas.messages.NubomediaCreateAppRequest;
import org.project.openbaton.nubomedia.paas.messages.NubomediaPort;
import org.project.openbaton.nubomedia.paas.model.openbaton.OpenbatonEvent;
import org.project.openbaton.nubomedia.paas.model.persistence.Application;
import org.project.openbaton.nubomedia.paas.model.persistence.ApplicationRepository;
import org.project.openbaton.nubomedia.paas.utils.PaaSProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.ResourceAccessException;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.*;

/**
 * Created by gca on 24/05/16.
 */
@Service
@Scope("prototype")
public class AppManager {
    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ApplicationRepository appRepo;
    @Autowired
    private OpenshiftManager osmanager;
    @Autowired
    private OpenbatonManager obmanager;
    @Autowired
    private PaaSProperties paaSProperties;

    private SecureRandom appIDGenerator = new SecureRandom();

    private static Map<String, MediaServerGroup> deploymentMap = new HashMap<>();

    public Application createApplication(NubomediaCreateAppRequest request, String token) throws turnServerException, StunServerException, SDKException {
        String appID = new BigInteger(130, appIDGenerator).toString(64);
        logger.debug("App ID " + appID + "\n");
        List<String> protocols = new ArrayList<>();
        List<Integer> targetPorts = new ArrayList<>();
        List<Integer> ports = new ArrayList<>();

        for (NubomediaPort port : request.getPorts()) {
            protocols.add(port.getProtocol());
            targetPorts.add(port.getTargetPort());
            ports.add(port.getPort());
        }

        logger.debug("request params " + request.getAppName() + " " + request.getGitURL() + " " + request.getProjectName() + " " + ports + " " + protocols + " " + request.getReplicasNumber());

        //Openbaton MediaServer Request
        logger.info("[PAAS]: EVENT_APP_CREATE " + new Date().getTime());
        MediaServerGroup mediaServerGroup = this.obmanager.createMediaServerGroup(request.getFlavor(), appID, this.paaSProperties.getInternalURL(), request.isCloudRepository(), request.isCdnConnector(), request.getQualityOfService(), request.isTurnServerActivate(), request.getTurnServerUrl(), request.getTurnServerUsername(), request.getTurnServerPassword(), request.isStunServerActivate(), request.getStunServerIp(), request.getStunServerPort(), request.getScaleInOut(), request.getScale_out_threshold());
        mediaServerGroup.setToken(token);

        deploymentMap.put(mediaServerGroup.getMediaServerGroupID(), mediaServerGroup);

        Application app = new Application(appID, request.getFlavor(), request.getAppName(), request.getProjectName(), "", mediaServerGroup.getMediaServerGroupID(), request.getGitURL(), targetPorts, ports, protocols, null, request.getReplicasNumber(), request.getSecretName(), false);
        appRepo.save(app);
        return app;
    }

    ;

    public void startOpenshiftBuild(OpenbatonEvent evt, String mediaServerGroupID) throws UnauthorizedException {
        logger.debug("starting callback for appId" + mediaServerGroupID);
        logger.info("Received event " + evt);
        logger.debug(deploymentMap.toString());
        MediaServerGroup server = deploymentMap.get(mediaServerGroupID);
        if (server == null) {
            // handle error
            logger.error("there was an error retrieving the application stored in the deployment Map ");
            return;
        }
        Application app = appRepo.findFirstByAppID(server.getAppId());

        if (evt.getAction().equals(Action.INSTANTIATE_FINISH) && server.getMediaServerGroupID().equals(evt.getPayload().getId())) {
            logger.info("[PAAS]: EVENT_FINISH " + new Date().getTime());
            app.setStatus(BuildingStatus.INITIALISED);
            app.setResourceOK(true);
            appRepo.save(app);

            String vnfrID = "";
            String cloudRepositoryIp = null;
            String cloudRepositoryPort = null;
            String cdnServerIp = null;


            for (VirtualNetworkFunctionRecord record : evt.getPayload().getVnfr()) {

                if (record.getEndpoint().equals("media-server"))
                    vnfrID = record.getId();

                if (record.getName().contains("mongodb")) {
                    cloudRepositoryIp = this.getCloudRepoIP(record);
                    cloudRepositoryPort = "7676";

                    for (LifecycleEvent lce : record.getLifecycle_event()) {
                        if (lce.getEvent().name().equals("START")) {
                            for (String scriptName : lce.getLifecycle_events()) {
                                if (scriptName.equals("start-cdn.sh")) {
                                    cdnServerIp = cloudRepositoryIp;
                                    break;
                                }
                            }
                        }
                    }
                }

            }

            logger.debug("retrieved session for " + server.getToken());
            String route = null;
            try {

                int[] ports = new int[app.getPorts().size()];
                int[] targetPorts = new int[app.getTargetPorts().size()];

                for(int i = 0; i < ports.length; i++){
                    ports[i] = app.getPorts().get(i);
                    targetPorts[i] = app.getTargetPorts().get(i);
                }


                logger.info("[PAAS]: CREATE_APP_OS " + new Date().getTime());
                logger.debug("cloudRepositoryPort " + cloudRepositoryPort + " IP " + cloudRepositoryIp);

                try {
                    route = osmanager.buildApplication(server.getToken(), app.getAppID(), app.getAppName(), app.getProjectName(), app.getGitURL(), ports, targetPorts, app.getProtocols().toArray(new String[0]), app.getReplicasNumber(), app.getSecretName(), vnfrID, paaSProperties.getVnfmIP(), paaSProperties.getVnfmPort(), cloudRepositoryIp, cloudRepositoryPort, cdnServerIp);

                } catch (ResourceAccessException e) {
                    obmanager.deleteDescriptor(server.getNsdID());
                    //obmanager.deleteEvent(server.getEventAllocatedID());
                    //obmanager.deleteEvent(server.getEventErrorID());
                    app.setStatus(BuildingStatus.FAILED);
                    appRepo.save(app);
                    deploymentMap.remove(app.getAppID());
                }
                logger.info("[PAAS]: SCHEDULED_APP_OS " + new Date().getTime());
            } catch (DuplicatedException e) {
                app.setRoute(e.getMessage());
                app.setStatus(BuildingStatus.DUPLICATED);
                appRepo.save(app);
                return;
            }
            obmanager.deleteDescriptor(server.getNsdID());

            //obmanager.deleteEvent(server.getEventAllocatedID());
            //obmanager.deleteEvent(server.getEventErrorID());
            app.setRoute(route);
            appRepo.save(app);
            deploymentMap.remove(app.getAppID());
        } else if (evt.getAction().equals(Action.ERROR)) {

            obmanager.deleteDescriptor(server.getNsdID());
            //obmanager.deleteEvent(server.getEventErrorID());
            //obmanager.deleteEvent(server.getEventAllocatedID());
            obmanager.deleteRecord(server.getMediaServerGroupID());
            app.setStatus(BuildingStatus.FAILED);
            appRepo.save(app);
            deploymentMap.remove(app.getAppID());
        }

    }

    private String getCloudRepoIP(VirtualNetworkFunctionRecord record) {

        for (VirtualDeploymentUnit vdu : record.getVdu()) {
            for (VNFCInstance instance : vdu.getVnfc_instance()) {
                for (Ip ip : instance.getFloatingIps()) {
                    if (ip != null) {
                        return ip.getIp();
                    }
                }
            }
        }

        return null;
    }

}
