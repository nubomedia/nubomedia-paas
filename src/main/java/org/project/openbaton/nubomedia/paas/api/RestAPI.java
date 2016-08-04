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

package org.project.openbaton.nubomedia.paas.api;

import org.openbaton.sdk.api.exception.SDKException;
import org.project.openbaton.nubomedia.paas.core.AppManager;
import org.project.openbaton.nubomedia.paas.core.OpenShiftManager;
import org.project.openbaton.nubomedia.paas.core.OpenbatonManager;
import org.project.openbaton.nubomedia.paas.exceptions.ApplicationNotFoundException;
import org.project.openbaton.nubomedia.paas.exceptions.ForbiddenException;
import org.project.openbaton.nubomedia.paas.exceptions.NotFoundException;
import org.project.openbaton.nubomedia.paas.exceptions.openbaton.StunServerException;
import org.project.openbaton.nubomedia.paas.exceptions.openbaton.turnServerException;
import org.project.openbaton.nubomedia.paas.exceptions.openshift.DuplicatedException;
import org.project.openbaton.nubomedia.paas.exceptions.openshift.NameStructureException;
import org.project.openbaton.nubomedia.paas.exceptions.openshift.UnauthorizedException;
import org.project.openbaton.nubomedia.paas.messages.*;
import org.project.openbaton.nubomedia.paas.model.persistence.Application;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.validation.Valid;
import java.util.List;

/**
 * Created by maa on 28.09.15.
 */
@RestController
@RequestMapping("/api/v1/nubomedia/paas")
public class RestAPI {
  private static final Logger logger = LoggerFactory.getLogger(RestAPI.class);

  @Autowired private OpenShiftManager osmanager;
  @Autowired private OpenbatonManager obmanager;

  @Autowired private AppManager appManager;

  @Value("${openshift.token}")
  private String token;

  @Value("${vnfm.ip}")
  private String vnfmIP;

  @Value("${openshift.keystore}")
  private String openshiftKeystore;

  @PostConstruct
  private void init() {
    System.setProperty("javax.net.ssl.trustStore", openshiftKeystore);
  }

  /**
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
  @ResponseBody
  public NubomediaCreateAppResponse createApp(
      @RequestBody NubomediaCreateAppRequest request,
      @RequestHeader(value = "project-id") String projectId)
      throws SDKException, UnauthorizedException, DuplicatedException, NameStructureException,
          turnServerException, StunServerException {
    Application app = appManager.createApplication(request, projectId, token);
    return new NubomediaCreateAppResponse(app, 200);
  }

  /**
   * @param id
   * @param projectId
   * @return
   * @throws ApplicationNotFoundException
   * @throws UnauthorizedException
   */
  @RequestMapping(value = "/app/{id}", method = RequestMethod.GET)
  @ResponseBody
  public Application getApp(
      @PathVariable("id") String id, @RequestHeader(value = "project-id") String projectId)
      throws ApplicationNotFoundException, UnauthorizedException {
    logger.info("Request status for " + id + " app");
    Application app = appManager.getApp(projectId, id);
    return app;
  }

  /**
   * @param projectId
   * @return
   * @throws UnauthorizedException
   * @throws ApplicationNotFoundException
   */
  @RequestMapping(
    value = "/app",
    method = RequestMethod.GET,
    produces = MediaType.APPLICATION_JSON_VALUE
  )
  public Iterable<Application> getApps(@RequestHeader(value = "project-id") String projectId)
      throws UnauthorizedException, ApplicationNotFoundException {
    logger.debug("Received request GET Applications");
    Iterable<Application> applications = appManager.getApps(projectId);
    logger.debug("Returning from GET Applications " + applications);
    return applications;
  }

  /**
   * @param id
   * @param projectId
   * @return
   * @throws UnauthorizedException
   */
  @RequestMapping(value = "/app/{id}", method = RequestMethod.DELETE)
  @ResponseBody
  public NubomediaDeleteAppResponse deleteApp(
      @PathVariable("id") String id, @RequestHeader(value = "project-id") String projectId)
      throws UnauthorizedException {
    logger.debug("Received delete Application (id " + id + ")request");
    return appManager.deleteApp(projectId, id);
  }

  /**
   * Deletes all the Apps specified in the list of ids
   *
   * @param projectId
   * @param ids
   * @throws NotFoundException
   * @throws ForbiddenException
   * @throws UnauthorizedException
   */
  @RequestMapping(
    value = "/multipledelete",
    method = RequestMethod.POST,
    consumes = MediaType.APPLICATION_JSON_VALUE
  )
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void multipleDelete(
      @RequestHeader(value = "project-id") String projectId, @RequestBody @Valid List<String> ids)
      throws NotFoundException, ForbiddenException, UnauthorizedException {
    for (String id : ids) deleteApp(id, projectId);
  }

  /**
   * @param projectId
   * @return
   * @throws UnauthorizedException
   */
  @RequestMapping(value = "/app", method = RequestMethod.DELETE)
  @ResponseBody
  public NubomediaDeleteAppsProjectResponse deleteApps(
      @RequestHeader(value = "project-id") String projectId) throws UnauthorizedException {
    logger.info("Requested delete of all Apps of project " + projectId);
    return appManager.deleteApps(projectId);
  }

  /**
   * @param id
   * @param projectId
   * @return
   * @throws UnauthorizedException
   * @throws ApplicationNotFoundException
   */
  @RequestMapping(value = "/app/{id}/buildlogs", method = RequestMethod.GET)
  @ResponseBody
  public NubomediaBuildLogs getBuildLogs(
      @PathVariable("id") String id, @RequestHeader(value = "project-id") String projectId)
      throws UnauthorizedException, ApplicationNotFoundException {
    logger.debug("Requested build log of Application " + id + " of project " + projectId);
    if (token == null) {
      throw new UnauthorizedException("no auth-token header");
    }
    return appManager.getBuildLogs(projectId, id);
  }

  /**
   * s
   *
   * @param id
   * @param podName
   * @param projectId
   * @return
   * @throws UnauthorizedException
   * @throws ApplicationNotFoundException
   */
  @RequestMapping(value = "/app/{id}/logs/{podName}", method = RequestMethod.GET)
  @ResponseBody
  public String getApplicationLogs(
      @PathVariable("id") String id,
      @PathVariable("podName") String podName,
      @RequestHeader(value = "project-id") String projectId)
      throws UnauthorizedException, ApplicationNotFoundException {
    logger.debug(
        "Requested Application logs of Application "
            + id
            + " of pod "
            + podName
            + " of project "
            + projectId);
    if (token == null) {
      throw new UnauthorizedException("no auth-token header");
    }
    return appManager.getApplicationLogs(projectId, id, podName);
  }

  @RequestMapping(value = "/secret", method = RequestMethod.POST)
  @ResponseBody
  public String createSecret(
      @RequestBody NubomediaCreateSecretRequest ncsr,
      @RequestHeader(value = "project-id") String projectId)
      throws UnauthorizedException {
    logger.debug(
        "Requested creation of new secret for "
            + ncsr.getProjectName()
            + " with key "
            + ncsr.getPrivateKey());
    if (token == null) {
      throw new UnauthorizedException("no auth-token header");
    }
    return osmanager.createSecret(token, ncsr.getPrivateKey());
  }

  @RequestMapping(value = "/secret/{projectName}/{secretName}", method = RequestMethod.DELETE)
  @ResponseBody
  public NubomediaDeleteSecretResponse deleteSecret(
      @PathVariable("secretName") String secretName,
      @PathVariable("projectName") String projectName)
      throws UnauthorizedException {
    logger.debug("Requested deletion of secret " + secretName);
    if (token == null) {
      throw new UnauthorizedException("no auth-token header");
    }
    HttpStatus deleteStatus = osmanager.deleteSecret(token, secretName);
    return new NubomediaDeleteSecretResponse(secretName, projectName, deleteStatus.value());
  }

  @RequestMapping(value = "/auth", method = RequestMethod.POST)
  @ResponseBody
  public NubomediaAuthorizationResponse authorize(
      @RequestBody NubomediaAuthorizationRequest request) throws UnauthorizedException {
    String token = osmanager.authenticate(request.getUsername(), request.getPassword());
    switch (token) {
      case "Unauthorized":
        return new NubomediaAuthorizationResponse(token, 401);
      case "PaaS Missing":
        return new NubomediaAuthorizationResponse(token, 404);
      default:
        return new NubomediaAuthorizationResponse(token, 200);
    }
  }

  @RequestMapping(value = "/server-ip/", method = RequestMethod.GET)
  @ResponseStatus(HttpStatus.OK)
  public String getMediaServerIp() {
    return vnfmIP;
  }
}
