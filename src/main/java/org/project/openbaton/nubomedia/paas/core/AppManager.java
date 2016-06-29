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

import org.openbaton.catalogue.mano.common.LifecycleEvent;
import org.openbaton.catalogue.mano.record.VirtualNetworkFunctionRecord;
import org.openbaton.catalogue.nfvo.Action;
import org.openbaton.sdk.api.exception.SDKException;
import org.project.openbaton.nubomedia.paas.core.util.NSRUtil;
import org.project.openbaton.nubomedia.paas.exceptions.openbaton.StunServerException;
import org.project.openbaton.nubomedia.paas.exceptions.openbaton.turnServerException;
import org.project.openbaton.nubomedia.paas.exceptions.openshift.DuplicatedException;
import org.project.openbaton.nubomedia.paas.exceptions.openshift.UnauthorizedException;
import org.project.openbaton.nubomedia.paas.messages.AppStatus;
import org.project.openbaton.nubomedia.paas.messages.NubomediaCreateAppRequest;
import org.project.openbaton.nubomedia.paas.messages.NubomediaPort;
import org.project.openbaton.nubomedia.paas.model.persistence.Application;
import org.project.openbaton.nubomedia.paas.model.persistence.openbaton.MediaServerGroup;
import org.project.openbaton.nubomedia.paas.model.persistence.openbaton.OpenBatonEvent;
import org.project.openbaton.nubomedia.paas.repository.application.ApplicationRepository;
import org.project.openbaton.nubomedia.paas.utils.PaaSProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    private OpenShiftManager osmanager;
    @Autowired
    private OpenbatonManager obmanager;
    @Autowired
    private PaaSProperties paaSProperties;

    @Value("${openshift.project}")
    private String openshiftProject;
    @Value("${openshift.token}")
    private String token;


    private SecureRandom appIDGenerator = new SecureRandom();


    public Application createApplication(NubomediaCreateAppRequest request, String projectId, String token) throws turnServerException, StunServerException, SDKException {
        List<String> protocols = new ArrayList<>();
        List<Integer> targetPorts = new ArrayList<>();
        List<Integer> ports = new ArrayList<>();

        for (NubomediaPort port : request.getPorts()) {
            protocols.add(port.getProtocol());
            targetPorts.add(port.getTargetPort());
            ports.add(port.getPort());
        }

        logger.debug("request params " + request.getAppName() + " " + request.getGitURL() + " " + ports + " " + protocols + " " + request.getReplicasNumber());

        //Openbaton MediaServer Request
        logger.info("[PAAS]: EVENT_APP_CREATE " + new Date().getTime());
        MediaServerGroup mediaServerGroup = this.obmanager.createMediaServerGroup(request.getFlavor(), this.paaSProperties.getInternalURL(), request.isCloudRepository(), request.isCdnConnector(), request.getQualityOfService(), request.isTurnServerActivate(), request.getTurnServerUrl(), request.getTurnServerUsername(), request.getTurnServerPassword(), request.isStunServerActivate(), request.getStunServerIp(), request.getStunServerPort(), request.getScaleInOut(), request.getScale_out_threshold());

        // Creating the application
        Application app = new Application();
        app.setName(request.getAppName());
        app.setProjectName(openshiftProject);
        app.setProjectId(projectId);
        app.setMediaServerGroup(mediaServerGroup);
        app.setRoute("");
        app.setGitURL(request.getGitURL());
        app.setTargetPorts(targetPorts);
        app.setPorts(ports);
        app.setProtocols(protocols);
        app.setReplicasNumber(request.getReplicasNumber());
        app.setSecretName(request.getSecretName());
        app.setResourceOK(false);
        app.setFlavor(request.getFlavor());
        app.setStatus(AppStatus.CREATED);

        appRepo.save(app);
        return app;
    }


    /**
     * This method is triggered upon receival of an event that the Network Service Record is instantiated
     * The event contains in the payload the NSR instantiated by Open Baton
     * This method starts the build procedure for the application on top of openshift
     *
     * @param evt
     * @throws UnauthorizedException
     */
    public void startOpenshiftBuild(OpenBatonEvent evt) throws UnauthorizedException {
        String mediaServerGroupID = evt.getPayload().getId();
        logger.debug("starting callback for app with media server group ID " + mediaServerGroupID);
        logger.info("Received event " + evt);

        Application app = appRepo.findByMSGroupID(mediaServerGroupID);
        MediaServerGroup mediaServerGroup = app.getMediaServerGroup();
        if (evt.getAction().equals(Action.INSTANTIATE_FINISH) && mediaServerGroup.getNsrID().equals(evt.getPayload().getId())) {
            logger.info("[PAAS]: EVENT_FINISH " + new Date().getTime());
            app.setStatus(AppStatus.INITIALISED);
            app.setResourceOK(true);
            appRepo.save(app);

            String vnfrID = "";
            String cloudRepositoryIp = null;
            String cloudRepositoryPort = null;
            String cdnServerIp = null;

            for (VirtualNetworkFunctionRecord record : evt.getPayload().getVnfr()) {

                if (record.getEndpoint().equals("media-server")) {
                    logger.debug("found record media-server");
                    vnfrID = record.getId();
                    mediaServerGroup.setFloatingIPs(NSRUtil.getFloatingIPs(record));
                    mediaServerGroup.setHostnames(NSRUtil.getHostnames(record));


                }
                if (record.getName().contains("mongodb")) {
                    try {
                        cloudRepositoryIp = NSRUtil.getIP(record);
                    } catch (Exception e) {
                        // TODO: handle correctly this case
                        logger.error("IP of mondodb was not found, set to null");
                        e.printStackTrace();
                        cloudRepositoryIp = null;
                    }
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
                    route = osmanager.buildApplication(token, app.getId(), app.getName(), app.getGitURL(), ports, targetPorts, app.getProtocols().toArray(new String[0]), app.getReplicasNumber(), app.getSecretName(), vnfrID, paaSProperties.getVnfmIP(), paaSProperties.getVnfmPort(), cloudRepositoryIp, cloudRepositoryPort, cdnServerIp);

                } catch (ResourceAccessException e) {
                    obmanager.deleteDescriptor(mediaServerGroup.getNsdID());
                    app.setStatus(AppStatus.FAILED);
                    appRepo.save(app);
                }
                logger.info("[PAAS]: SCHEDULED_APP_OS " + new Date().getTime());
            } catch (DuplicatedException e) {
                app.setRoute(e.getMessage());
                app.setStatus(AppStatus.DUPLICATED);
                appRepo.save(app);
                return;
            }
            obmanager.deleteDescriptor(mediaServerGroup.getNsdID());
            app.setRoute(route);
            appRepo.save(app);
        } else if (evt.getAction().equals(Action.ERROR)) {
            obmanager.deleteDescriptor(mediaServerGroup.getNsdID());
            obmanager.deleteRecord(mediaServerGroup.getNsrID());
            app.setStatus(AppStatus.FAILED);
            appRepo.save(app);
        }

    }


}
