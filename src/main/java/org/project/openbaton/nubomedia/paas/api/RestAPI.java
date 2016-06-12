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

package org.project.openbaton.nubomedia.paas.api;

import org.openbaton.sdk.api.exception.SDKException;
import org.project.openbaton.nubomedia.paas.core.AppManager;
import org.project.openbaton.nubomedia.paas.core.OpenbatonManager;
import org.project.openbaton.nubomedia.paas.core.OpenshiftManager;
import org.project.openbaton.nubomedia.paas.exceptions.ApplicationNotFoundException;
import org.project.openbaton.nubomedia.paas.exceptions.openbaton.StunServerException;
import org.project.openbaton.nubomedia.paas.exceptions.openbaton.turnServerException;
import org.project.openbaton.nubomedia.paas.exceptions.openshift.DuplicatedException;
import org.project.openbaton.nubomedia.paas.exceptions.openshift.NameStructureException;
import org.project.openbaton.nubomedia.paas.exceptions.openshift.UnauthorizedException;
import org.project.openbaton.nubomedia.paas.messages.*;
import org.project.openbaton.nubomedia.paas.model.persistence.openbaton.MediaServerGroup;
import org.project.openbaton.nubomedia.paas.model.persistence.Application;
import org.project.openbaton.nubomedia.paas.repository.application.ApplicationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.ResourceAccessException;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by maa on 28.09.15.
 */

@RestController
@RequestMapping("/api/v1/nubomedia/paas")
public class RestAPI {
    private static final Logger logger = LoggerFactory.getLogger(RestAPI.class);

    @Autowired
    private OpenshiftManager osmanager;
    @Autowired
    private OpenbatonManager obmanager;
    @Autowired
    private ApplicationRepository appRepo;

    @Autowired
    private AppManager appManager;

    @Value("${openshift.token}")
    private String token;

    @Value("${paas.keystore}")
    private String paasKeystore;

    @Value("${paas.vnfmIP}")
    private String vnfmIP;

    @PostConstruct
    private void init() {
        System.setProperty("javax.net.ssl.trustStore", paasKeystore);
    }

    /**
     *
     * @param request
     * @param projectId
     * @return
     * @throws SDKException
     * @throws UnauthorizedException
     * @throws DuplicatedException
     * @throws NameStructureException
     * @throws turnServerException
     * @throws StunServerException
     */
    @RequestMapping(value = "/app", method = RequestMethod.POST)
    public
    @ResponseBody
    NubomediaCreateAppResponse createApp(@RequestBody NubomediaCreateAppRequest request, @RequestHeader(value = "project-id") String projectId) throws SDKException, UnauthorizedException, DuplicatedException, NameStructureException, turnServerException, StunServerException {
        if (token == null) {
            throw new UnauthorizedException("No auth-token header");
        }
        if (request.getAppName().length() > 18) {
            throw new NameStructureException("Name is too long");
        }
        if (request.getAppName().contains(".")) {
            throw new NameStructureException("Name can't contains dots");
        }

        if (request.getAppName().contains("_")) {
            throw new NameStructureException("Name can't contain underscore");
        }

        if (!request.getAppName().matches("[a-z0-9]+(?:[._-][a-z0-9]+)*")) {
            throw new NameStructureException("Name must match [a-z0-9]+(?:[._-][a-z0-9]+)*");
        }

        if (!appRepo.findByAppName(request.getAppName()).isEmpty()) {
            throw new DuplicatedException("Application with " + request.getAppName() + " already exist");
        }

        logger.debug("REQUEST" + request.toString());
        Application app = appManager.createApplication(request, projectId, token);
        return new NubomediaCreateAppResponse(app, 200);
    }

    /**
     *
     * @param id
     * @param projectId
     * @return
     * @throws ApplicationNotFoundException
     * @throws UnauthorizedException
     */
    @RequestMapping(value = "/app/{id}", method = RequestMethod.GET)
    public
    @ResponseBody
    Application getApp(@PathVariable("id") String id, @RequestHeader(value = "project-id") String projectId) throws ApplicationNotFoundException, UnauthorizedException {
        logger.info("Request status for " + id + " app");
        Application app = null;
        if ( (app = appRepo.findFirstByAppIdAndProjectId(id, projectId)) == null) {
            throw new ApplicationNotFoundException("Application with ID in Project " + projectId + " not found");
        }
        return app;

    }

    /**
     *
     * @param projectId
     * @return
     * @throws UnauthorizedException
     * @throws ApplicationNotFoundException
     */
    @RequestMapping(value = "/app", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Iterable<Application> getApps(@RequestHeader(value = "project-id") String projectId) throws UnauthorizedException, ApplicationNotFoundException {
        logger.debug("Received request GET Applications");
        Iterable<Application> applications = this.appRepo.findByProjectId(projectId);
        logger.debug("Returning from GET Applications " + applications);
        return applications;
    }


    /**
     *
     * @param id
     * @param projectId
     * @return
     * @throws UnauthorizedException
     */
    @RequestMapping(value = "/app/{id}", method = RequestMethod.DELETE)
    public
    @ResponseBody
    NubomediaDeleteAppResponse deleteApp(@PathVariable("id") String id, @RequestHeader(value = "project-id") String projectId) throws UnauthorizedException {
        logger.debug("Received delete Application (id " + id+")request");
        Application app = null;
        if ((app=appRepo.findFirstByAppIdAndProjectId(id, projectId)) == null) {
            return new NubomediaDeleteAppResponse(id, "Application not found in Project " + projectId + " not found", "null", 404);
        }

        logger.debug("Retrieved Application to be deleted " + app);

        // check that the app has been instantiated on openshift
        if (!app.isResourceOK()) {

            String name = app.getAppName();
            String projectName = app.getProjectName();

            if (app.getStatus().equals(AppStatus.CREATED) || app.getStatus().equals(AppStatus.INITIALIZING) || app.getStatus().equals(AppStatus.FAILED)) {
                logger.debug("media server group: " + app.getMediaServerGroup());
                obmanager.deleteDescriptor(app.getMediaServerGroup().getNsdID());
                if (!app.getStatus().equals(AppStatus.FAILED) && obmanager.existRecord(app.getMediaServerGroup().getId())) {
                    obmanager.deleteRecord(app.getMediaServerGroup().getId());
                }
            }

            appRepo.delete(app);
            return new NubomediaDeleteAppResponse(id, name, projectName, 200);

        }

        if (app.getStatus().equals(AppStatus.PAAS_RESOURCE_MISSING)) {
            obmanager.deleteDescriptor(app.getMediaServerGroup().getNsdID());
            obmanager.deleteRecord(app.getMediaServerGroup().getId());
            appRepo.delete(app);

            return new NubomediaDeleteAppResponse(id, app.getAppName(), app.getProjectName(), 200);
        }


        obmanager.deleteRecord(app.getMediaServerGroup().getId());
        HttpStatus resDelete = HttpStatus.BAD_REQUEST;
        try {
            resDelete = osmanager.deleteApplication(token, app.getAppName());
        } catch (ResourceAccessException e) {
            logger.info("Application does not exist on the PaaS");
        }

        appRepo.delete(app);

        return new NubomediaDeleteAppResponse(id, app.getAppName(), app.getProjectName(), resDelete.value());
    }


    /**
     *
     * @param projectId
     * @return
     * @throws UnauthorizedException
     */
    @RequestMapping(value = "/app", method = RequestMethod.DELETE)
    public
    @ResponseBody
    NubomediaDeleteAppsProjectResponse deleteApp(@RequestHeader(value = "project-id") String projectId) throws UnauthorizedException {

        if (appRepo.findByProjectId(projectId) == null || appRepo.findByProjectId(projectId).size() == 0) {
            return new NubomediaDeleteAppsProjectResponse(projectId, "Not Found any Applications in this project", null, 404);
        }

        NubomediaDeleteAppsProjectResponse response = new NubomediaDeleteAppsProjectResponse(projectId, "", new ArrayList<NubomediaDeleteAppResponse>(), 200);

        List<Application> apps = appRepo.findByProjectId(projectId);

        logger.debug("Deleting " + apps);
        for (Application app : apps) {
            if (!app.isResourceOK()) {
                if (app.getStatus().equals(AppStatus.CREATED) || app.getStatus().equals(AppStatus.INITIALIZING) || app.getStatus().equals(AppStatus.FAILED)) {
                    logger.debug("media server group: " + app.getMediaServerGroup());
                    obmanager.deleteDescriptor(app.getMediaServerGroup().getNsdID());

                    if (!app.getStatus().equals(AppStatus.FAILED) && obmanager.existRecord(app.getMediaServerGroup().getId())) {
                        obmanager.deleteRecord(app.getMediaServerGroup().getId());
                    }
                }

                appRepo.delete(app);
                response.getResponses().add(new NubomediaDeleteAppResponse(app.getAppID(), app.getAppName(), app.getProjectName(), 200));
                break;
            }

            if (app.getStatus().equals(AppStatus.PAAS_RESOURCE_MISSING)) {
                obmanager.deleteRecord(app.getMediaServerGroup().getId());
                appRepo.delete(app);
                response.getResponses().add(new NubomediaDeleteAppResponse(app.getAppID(), app.getAppName(), app.getProjectName(), 200));
                break;
            }
            obmanager.deleteRecord(app.getMediaServerGroup().getId());
            HttpStatus resDelete = HttpStatus.BAD_REQUEST;
            try {
                resDelete = osmanager.deleteApplication(token, app.getAppName());
            } catch (ResourceAccessException e) {
                logger.info("PaaS Missing");
            }

            appRepo.delete(app);
            response.getResponses().add(new NubomediaDeleteAppResponse(app.getAppID(), app.getAppName(), app.getProjectName(), resDelete.value()));
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

    /**
     *
     * @param id
     * @param projectId
     * @return
     * @throws UnauthorizedException
     * @throws ApplicationNotFoundException
     */
    @RequestMapping(value = "/app/{id}/buildlogs", method = RequestMethod.GET)
    public
    @ResponseBody
    NubomediaBuildLogs getBuildLogs(@PathVariable("id") String id, @RequestHeader(value = "project-id") String projectId) throws UnauthorizedException, ApplicationNotFoundException {

        if (token == null) {
            throw new UnauthorizedException("no auth-token header");
        }

        NubomediaBuildLogs res = new NubomediaBuildLogs();

        if (appRepo.findFirstByAppIdAndProjectId(id, projectId) == null) {
            throw new ApplicationNotFoundException("Application with ID in Project " + projectId + " not found");
        }

        Application app = appRepo.findFirstByAppIdAndProjectId(id, projectId);

        if (app.getStatus().equals(AppStatus.FAILED) && !app.isResourceOK()) {

            res.setId(id);
            res.setAppName(app.getAppName());
            res.setProjectName(app.getProjectName());
            res.setLog("Something wrong on retrieving resources");

        } else if (app.getStatus().equals(AppStatus.CREATED) || app.getStatus().equals(AppStatus.INITIALIZING)) {
            res.setId(id);
            res.setAppName(app.getAppName());
            res.setProjectName(app.getProjectName());
            res.setLog("The application is retrieving resources " + app.getStatus());

            return res;
        } else if (app.getStatus().equals(AppStatus.PAAS_RESOURCE_MISSING)) {
            res.setId(id);
            res.setAppName(app.getAppName());
            res.setProjectName(app.getProjectName());
            res.setLog("PaaS components are missing, send an email to the administrator to check the PaaS status");

            return res;
        } else {

            res.setId(id);
            res.setAppName(app.getAppName());
            res.setProjectName(app.getProjectName());
            try {
                res.setLog(osmanager.getBuildLogs(token, app.getAppName()));
            } catch (ResourceAccessException e) {
                app.setStatus(AppStatus.PAAS_RESOURCE_MISSING);
                appRepo.save(app);
                res.setLog("Openshift is not responding, app " + app.getAppName() + " is not anymore available");
            }
        }

        return res;
    }

    /**
     * s
     * @param id
     * @param podName
     * @param projectId
     * @return
     * @throws UnauthorizedException
     * @throws ApplicationNotFoundException
     */
    @RequestMapping(value = "/app/{id}/logs/{podName}", method = RequestMethod.GET)
    public
    @ResponseBody
    String getApplicationLogs(@PathVariable("id") String id, @PathVariable("podName") String podName, @RequestHeader(value = "project-id") String projectId) throws UnauthorizedException, ApplicationNotFoundException {

        if (token == null) {
            throw new UnauthorizedException("no auth-token header");
        }

        if (appRepo.findFirstByAppIdAndProjectId(id, projectId) == null) {
            throw new ApplicationNotFoundException("Application with ID in Project " + projectId + " not found");
        }

        Application app = appRepo.findFirstByAppIdAndProjectId(id, projectId);

        if (!app.getStatus().equals(AppStatus.RUNNING)) {
            return "Application Status " + app.getStatus() + ", logs are not available until the status is RUNNING";
        }

        return osmanager.getApplicationLog(token, app.getAppName(), podName);

    }


    @RequestMapping(value = "/secret", method = RequestMethod.POST)
    public
    @ResponseBody
    String createSecret(@RequestBody NubomediaCreateSecretRequest ncsr, @RequestHeader(value = "project-id") String projectId) throws UnauthorizedException {

        if (token == null) {
            throw new UnauthorizedException("no auth-token header");
        }

        logger.debug("Creating new secret for " + ncsr.getProjectName() + " with key " + ncsr.getPrivateKey());
        return osmanager.createSecret(token, ncsr.getPrivateKey());
    }

    @RequestMapping(value = "/secret/{projectName}/{secretName}", method = RequestMethod.DELETE)
    public
    @ResponseBody
    NubomediaDeleteSecretResponse deleteSecret(@PathVariable("secretName") String secretName, @PathVariable("projectName") String projectName) throws UnauthorizedException {

        if (token == null) {
            throw new UnauthorizedException("no auth-token header");
        }

        HttpStatus deleteStatus = osmanager.deleteSecret(token, secretName);
        return new NubomediaDeleteSecretResponse(secretName, projectName, deleteStatus.value());
    }

    @RequestMapping(value = "/auth", method = RequestMethod.POST)
    public
    @ResponseBody
    NubomediaAuthorizationResponse authorize(@RequestBody NubomediaAuthorizationRequest request) throws UnauthorizedException {

        String token = osmanager.authenticate(request.getUsername(), request.getPassword());
        if (token.equals("Unauthorized")) {
            return new NubomediaAuthorizationResponse(token, 401);
        } else if (token.equals("PaaS Missing")) {
            return new NubomediaAuthorizationResponse(token, 404);
        } else {
            return new NubomediaAuthorizationResponse(token, 200);
        }
    }

    @RequestMapping(value = "/server-ip/", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public String getMediaServerIp() {
        return vnfmIP;
    }


}

