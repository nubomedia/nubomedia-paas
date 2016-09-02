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

package org.project.openbaton.nubomedia.paas.core;

import org.openbaton.catalogue.mano.common.LifecycleEvent;
import org.openbaton.catalogue.mano.record.VirtualNetworkFunctionRecord;
import org.openbaton.catalogue.nfvo.Action;
import org.openbaton.sdk.api.exception.SDKException;
import org.project.openbaton.nubomedia.paas.core.util.NSRUtil;
import org.project.openbaton.nubomedia.paas.exceptions.ApplicationNotFoundException;
import org.project.openbaton.nubomedia.paas.exceptions.BadRequestException;
import org.project.openbaton.nubomedia.paas.exceptions.NotFoundException;
import org.project.openbaton.nubomedia.paas.exceptions.openbaton.StunServerException;
import org.project.openbaton.nubomedia.paas.exceptions.openbaton.turnServerException;
import org.project.openbaton.nubomedia.paas.exceptions.openshift.DuplicatedException;
import org.project.openbaton.nubomedia.paas.exceptions.openshift.NameStructureException;
import org.project.openbaton.nubomedia.paas.exceptions.openshift.UnauthorizedException;
import org.project.openbaton.nubomedia.paas.messages.*;
import org.project.openbaton.nubomedia.paas.model.persistence.Application;
import org.project.openbaton.nubomedia.paas.model.persistence.openbaton.MediaServerGroup;
import org.project.openbaton.nubomedia.paas.model.persistence.openbaton.OpenBatonEvent;
import org.project.openbaton.nubomedia.paas.properties.PaaSProperties;
import org.project.openbaton.nubomedia.paas.properties.OpenShiftProperties;
import org.project.openbaton.nubomedia.paas.properties.VnfmProperties;
import org.project.openbaton.nubomedia.paas.repository.application.ApplicationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;

import java.util.*;
import java.util.concurrent.ExecutionException;

/**
 * Created by gca on 24/05/16.
 */
@Service
@Scope("prototype")
public class AppManager {
  Logger logger = LoggerFactory.getLogger(this.getClass());

  @Autowired private ApplicationRepository appRepo;
  @Autowired private OpenShiftManager osmanager;
  @Autowired private OpenbatonManager obmanager;
  @Autowired private PaaSProperties paaSProperties;
  @Autowired private OpenShiftProperties openShiftProperties;

  @Autowired private VnfmProperties vnfmProperties;

  @Value("${openshift.project}")
  private String openshiftProject;

  @Value("${openshift.token}")
  private String token;

  public Application createApplication(
      NubomediaCreateAppRequest request, String user, String projectId, String token)
      throws turnServerException, StunServerException, SDKException, DuplicatedException,
          NameStructureException, UnauthorizedException {
    if (token == null) {
      throw new UnauthorizedException("No auth-token header");
    }
    //    if (request.getName().length() > 18) {
    //      throw new NameStructureException("Name is too long");
    //    }
    if (request.getName().contains(".")) {
      throw new NameStructureException("Name can't contains dots");
    }

    if (request.getName().contains("_")) {
      throw new NameStructureException("Name can't contain underscore");
    }

    if (!request.getName().matches("[a-z0-9]+(?:[._-][a-z0-9]+)*")) {
      throw new NameStructureException("Name must match [a-z0-9]+(?:[._-][a-z0-9]+)*");
    }

    //    if (!appRepo.findByName(request.getName()).isEmpty()) {
    //      throw new DuplicatedException("Application with " + request.getName() + " already exist");
    //    }

    logger.debug("REQUEST" + request.toString());

    List<String> protocols = new ArrayList<>();
    List<Integer> targetPorts = new ArrayList<>();
    List<Integer> ports = new ArrayList<>();

    for (NubomediaPort port : request.getPorts()) {
      protocols.add(port.getProtocol());
      targetPorts.add(port.getTargetPort());
      ports.add(port.getPort());
    }

    logger.debug(
        "request params "
            + request.getName()
            + " "
            + request.getGitURL()
            + " "
            + ports
            + " "
            + protocols
            + " "
            + request.getReplicasNumber());
    Random r = new Random();
    char c = (char) (r.nextInt(26) + 'a');
    String osName = c + UUID.randomUUID().toString().substring(0, 8);
    //Openbaton MediaServer Request
    logger.info("[PAAS]: EVENT_APP_CREATE " + new Date().getTime());
    MediaServerGroup mediaServerGroup =
        this.obmanager.createMediaServerGroup(
            request.getName(),
            osName,
            request.getFlavor(),
            this.paaSProperties.getPort(),
            request.isCloudRepository(),
            request.isCdnConnector(),
            request.getQualityOfService(),
            request.isTurnServerActivate(),
            request.getTurnServerUrl(),
            request.getTurnServerUsername(),
            request.getTurnServerPassword(),
            request.isStunServerActivate(),
            request.getStunServerIp(),
            request.getStunServerPort(),
            request.getScaleInOut(),
            request.getScale_out_threshold());

    // Creating the application
    Application app = new Application();
    app.setName(request.getName());
    app.setOsName(osName);
    app.setProjectName(openshiftProject);
    app.setProjectId(projectId);
    app.setMediaServerGroup(mediaServerGroup);
    //app.setRoute("");
    app.setRoute(osName + "." + openShiftProperties.getDomainName());
    app.setGitURL(request.getGitURL());
    app.setTargetPorts(targetPorts);
    app.setPorts(ports);
    app.setProtocols(protocols);
    app.setReplicasNumber(request.getReplicasNumber());
    app.setSecretName(request.getSecretName());
    app.setResourceOK(false);
    app.setFlavor(request.getFlavor());
    app.setStatus(AppStatus.CREATED);
    app.setCdnConnector(request.isCdnConnector());
    app.setCloudRepository(request.isCloudRepository());
    app.setQualityOfService(request.getQualityOfService());
    app.setScaleInOut(request.getScaleInOut());
    app.setScaleOutThreshold(request.getScale_out_threshold());
    app.setCreatedBy(user);
    app.setCreatedAt(new Date());
    app.setStunServerActivate(request.isStunServerActivate());
    app.setStunServerIp(request.getStunServerIp());
    app.setStunServerPort(request.getStunServerPort());
    app.setTurnServerActivate(request.isTurnServerActivate());
    app.setTurnServerUrl(request.getTurnServerUrl());
    app.setTurnServerUsername(request.getTurnServerUsername());
    app.setTurnServerPassword(request.getTurnServerPassword());

    appRepo.save(app);
    return app;
  }

  /**
   * This method is triggered upon receival of an event that the Network Service Record is
   * instantiated The event contains in the payload the NSR instantiated by Open Baton This method
   * starts the build procedure for the application on top of openshift
   *
   * @param evt
   * @throws UnauthorizedException
   */
  public void startOpenshiftBuild(OpenBatonEvent evt)
      throws UnauthorizedException, BadRequestException {
    String nsrId = evt.getPayload().getId();
    logger.debug("starting callback for app with media server group ID " + nsrId);
    logger.info("Received event " + evt);

    Application app = appRepo.findByNsrId(nsrId);
    MediaServerGroup mediaServerGroup = app.getMediaServerGroup();
    if (evt.getAction().equals(Action.INSTANTIATE_FINISH)
        && mediaServerGroup.getNsrID().equals(evt.getPayload().getId())) {
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

        for (int i = 0; i < ports.length; i++) {
          ports[i] = app.getPorts().get(i);
          targetPorts[i] = app.getTargetPorts().get(i);
        }

        logger.info("[PAAS]: CREATE_APP_OS " + new Date().getTime());
        logger.debug("cloudRepositoryPort " + cloudRepositoryPort + " IP " + cloudRepositoryIp);

        try {
          route =
              osmanager.buildApplication(
                  token,
                  app.getId(),
                  app.getName(),
                  app.getOsName(),
                  app.getGitURL(),
                  ports,
                  targetPorts,
                  app.getProtocols().toArray(new String[0]),
                  app.getReplicasNumber(),
                  app.getSecretName(),
                  vnfrID,
                  vnfmProperties.getIp(),
                  vnfmProperties.getPort(),
                  cloudRepositoryIp,
                  cloudRepositoryPort,
                  cdnServerIp);

        } catch (ResourceAccessException e) {
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
      //app.setRoute(route);
      appRepo.save(app);
    } else if (evt.getAction().equals(Action.ERROR)) {
      obmanager.deleteDescriptor(mediaServerGroup.getNsdID());
      obmanager.deleteRecord(mediaServerGroup.getNsrID());
      app.setStatus(AppStatus.FAILED);
      appRepo.save(app);
    }
  }

  public Application getApp(String id) throws ApplicationNotFoundException {
    Application app = null;
    if ((app = appRepo.findFirstById(id)) == null) {
      throw new ApplicationNotFoundException("Application with ID " + id + " not found");
    }
    return app;
  }

  public Application getApp(String projectId, String id) throws ApplicationNotFoundException {
    Application app = null;
    if ((app = appRepo.findFirstByAppIdAndProjectId(id, projectId)) == null) {
      throw new ApplicationNotFoundException(
          "Application with ID " + id + " in Project " + projectId + " not found");
    }
    return app;
  }

  public Iterable<Application> getApps() throws ApplicationNotFoundException {
    Iterable<Application> applications = this.appRepo.findAll();
    return applications;
  }

  public Iterable<Application> getApps(String projectId) throws ApplicationNotFoundException {
    Iterable<Application> applications = this.appRepo.findByProjectId(projectId);
    return applications;
  }

  public NubomediaDeleteAppResponse deleteApp(String id)
      throws UnauthorizedException, NotFoundException {
    Application app = appRepo.findFirstById(id);
    if (app == null) throw new NotFoundException("Not found Application with ID " + id);
    return deleteApp(app.getProjectId(), id);
  }

  public NubomediaDeleteAppResponse deleteApp(String projectId, String id)
      throws UnauthorizedException {
    Application app = null;
    if ((app = appRepo.findFirstByAppIdAndProjectId(id, projectId)) == null) {
      return new NubomediaDeleteAppResponse(
          id, "Application not found in Project " + projectId + " not found", "null", "null", 404);
    }

    logger.debug("Retrieved Application to be deleted " + app);

    // check that the app has been instantiated on openshift
    if (!app.isResourceOK() || app.getStatus().equals(AppStatus.PAAS_RESOURCE_MISSING)) {
      try {
        obmanager.deleteRecord(app.getMediaServerGroup().getNsrID());
        obmanager.deleteDescriptor(app.getMediaServerGroup().getNsdID());
      } catch (Exception e) {
        logger.error(e.getMessage());
      } finally {
        appRepo.delete(app);
      }
      return new NubomediaDeleteAppResponse(
          id, app.getName(), app.getOsName(), app.getProjectName(), 200);
    }

    HttpStatus resDelete = HttpStatus.BAD_REQUEST;
    try {
      resDelete = osmanager.deleteApplication(token, app.getOsName());
    } catch (ResourceAccessException e) {
      logger.info("Application does not exist on the PaaS");
    } catch (Exception e) {
      logger.warn(e.getMessage());
    }
    try {
      obmanager.deleteRecord(app.getMediaServerGroup().getNsrID());
      obmanager.deleteDescriptor(app.getMediaServerGroup().getNsdID());
    } catch (Exception e) {
      logger.error(e.getMessage());
    } finally {
      appRepo.delete(app);
    }
    return new NubomediaDeleteAppResponse(
        id, app.getName(), app.getOsName(), app.getProjectName(), resDelete.value());
  }

  public NubomediaDeleteAppsProjectResponse deleteApps(String projectId)
      throws UnauthorizedException {

    if (appRepo.findByProjectId(projectId) == null
        || appRepo.findByProjectId(projectId).size() == 0) {
      return new NubomediaDeleteAppsProjectResponse(
          projectId, "Not Found any Applications in this project", null, 404);
    }

    NubomediaDeleteAppsProjectResponse response =
        new NubomediaDeleteAppsProjectResponse(
            projectId, "", new ArrayList<NubomediaDeleteAppResponse>(), 200);

    List<Application> apps = appRepo.findByProjectId(projectId);

    logger.debug("Deleting " + apps);
    for (Application app : apps) {
      response.getResponses().add(deleteApp(app.getProjectId(), app.getId()));
    }
    for (NubomediaDeleteAppResponse singleRes : response.getResponses()) {
      if (singleRes.getCode() != 200) {
        response.setCode(singleRes.getCode());
        response.setMessage("Not all applications were deleted successfully");
        return response;
      }
    }
    response.setMessage("All applications of this project were removed successfully");
    return response;
  }

  public NubomediaBuildLogs getBuildLogs(String id)
      throws UnauthorizedException, ApplicationNotFoundException, NotFoundException {
    Application app = appRepo.findFirstById(id);
    if (app == null) throw new NotFoundException("Not found Application with ID " + id);
    return getBuildLogs(app.getProjectId(), id);
  }

  public NubomediaBuildLogs getBuildLogs(String projectId, String id)
      throws UnauthorizedException, ApplicationNotFoundException {
    NubomediaBuildLogs res = new NubomediaBuildLogs();

    if (appRepo.findFirstByAppIdAndProjectId(id, projectId) == null) {
      throw new ApplicationNotFoundException(
          "Application with ID in Project " + projectId + " not found");
    }

    Application app = appRepo.findFirstByAppIdAndProjectId(id, projectId);

    if (app.getStatus().equals(AppStatus.FAILED) && !app.isResourceOK()) {
      res.setId(id);
      res.setName(app.getName());
      res.setProjectName(app.getProjectName());
      res.setLog("Something wrong on retrieving resources");

    } else if (app.getStatus().equals(AppStatus.CREATED)
        || app.getStatus().equals(AppStatus.INITIALIZING)) {
      res.setId(id);
      res.setName(app.getName());
      res.setProjectName(app.getProjectName());
      res.setLog("The application is retrieving resources " + app.getStatus());

      return res;
    } else if (app.getStatus().equals(AppStatus.PAAS_RESOURCE_MISSING)) {
      res.setId(id);
      res.setName(app.getName());
      res.setProjectName(app.getProjectName());
      res.setLog(
          "PaaS components are missing, send an email to the administrator to check the PaaS status");

      return res;
    } else {
      res.setId(id);
      res.setName(app.getName());
      res.setProjectName(app.getProjectName());
      try {
        res.setLog(osmanager.getBuildLogs(token, app.getName(), app.getOsName()));
      } catch (ResourceAccessException e) {
        app.setStatus(AppStatus.PAAS_RESOURCE_MISSING);
        appRepo.save(app);
        res.setLog(
            "Openshift is not responding, app "
                + app.getName()
                + app.getOsName()
                + " is not anymore available");
      }
    }
    return res;
  }

  public String getApplicationLogs(String projectId, String id, String podName)
      throws UnauthorizedException, ApplicationNotFoundException {
    if (appRepo.findFirstByAppIdAndProjectId(id, projectId) == null) {
      throw new ApplicationNotFoundException(
          "Application with ID in Project " + projectId + " not found");
    }
    Application app = appRepo.findFirstByAppIdAndProjectId(id, projectId);

    if (!app.getStatus().equals(AppStatus.RUNNING)) {
      return "Application Status "
          + app.getStatus()
          + ", logs are not available until the status is RUNNING";
    }
    return osmanager.getApplicationLog(token, app.getOsName(), podName);
  }

  public String getApplicationLogs(String id, String podName)
      throws NotFoundException, UnauthorizedException, ApplicationNotFoundException {
    Application app = appRepo.findFirstById(id);
    if (app == null) throw new NotFoundException("Not found Application with ID " + id);
    return getApplicationLogs(app.getProjectId(), id, podName);
  }
}
