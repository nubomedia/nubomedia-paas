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

package org.project.openbaton.nubomedia.paas.core.openshift;

import com.openshift.internal.restclient.model.Build;
import com.openshift.internal.restclient.model.BuildConfig;
import com.openshift.restclient.ClientBuilder;
import com.openshift.restclient.IClient;
import com.openshift.restclient.ResourceKind;
import com.openshift.restclient.model.IList;
import com.openshift.restclient.model.IResource;
import com.openshift.restclient.model.build.IBuildStatus;
import org.project.openbaton.nubomedia.paas.exceptions.NotFoundException;
import org.project.openbaton.nubomedia.paas.exceptions.openshift.UnauthorizedException;
import org.project.openbaton.nubomedia.paas.messages.AppStatus;
import org.project.openbaton.nubomedia.paas.model.openshift.RouteConfig;
import org.project.openbaton.nubomedia.paas.properties.OpenShiftProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by maa on 07.10.15.
 */
@Service
public class BuildManager {

  @Autowired private BuildConfigManager buildConfigManager;

  @Autowired private RestTemplate template;

  private String openshiftBaseURL;
  private String suffix;
  private String logSuffix;

  private IClient client;

  @Autowired private OpenShiftProperties openShiftProperties;

  private Logger logger;

  @PostConstruct
  public void init() {
    this.logger = LoggerFactory.getLogger(this.getClass());
    this.suffix = "/builds/";
    this.logSuffix = "/log";
    this.openshiftBaseURL = openShiftProperties.getBaseURL() + "/oapi/v1/namespaces/";
    client =
        new ClientBuilder(openShiftProperties.getBaseURL())
            .usingToken(openShiftProperties.getToken())
            .build();
  }

  public BuildConfig createBuild(
      String osName,
      String namespace,
      String gitURL,
      String dockerRepo,
      String secretName,
      String mediaServerGID,
      String mediaServerIP,
      String mediaServerPort,
      String cloudRepositoryIp,
      String cloudRepoPort,
      String cdnServerIp,
      RouteConfig routeConfig) {
    logger.info(
        "Creating buildconfig for "
            + osName
            + " in project "
            + namespace
            + " from gitURL "
            + gitURL
            + " with secret "
            + secretName);
    return buildConfigManager.createBuildConfig(
        osName,
        openShiftProperties.getProject(),
        dockerRepo,
        gitURL,
        secretName,
        mediaServerGID,
        mediaServerIP,
        mediaServerPort,
        cloudRepositoryIp,
        cloudRepoPort,
        cdnServerIp,
        routeConfig);
  }

  public void deleteBuilds(String osName) throws UnauthorizedException {
    logger.info("Deleting all builds of " + osName + "-bc");
    Collection<Build> buildsToRemove = new ArrayList<>();
    Collection<Build> builds = getBuilds();
    for (Build build : builds) {
      if (build.getName().contains(osName + "-bc")) {
        buildsToRemove.add(build);
      }
    }
    logger.debug("Deleting builds: " + buildsToRemove);
    for (Build buildToRemove : buildsToRemove) {
      logger.debug("Deleting build: " + buildToRemove);
      client.delete(buildToRemove);
      logger.debug("Deleted build: " + buildToRemove);
    }
    logger.info("Deleted all builds of " + osName + "-bc");
  }

  public AppStatus getApplicationStatus(String osName) throws NotFoundException {
    AppStatus status = getBuildStatus(osName);
    logger.debug("Got status of " + osName + ":" + status);
    return status;
  }

  public String getBuildLogs(String osName) throws NotFoundException, UnauthorizedException {
    Build target = this.retrieveBuild(osName);
    HttpHeaders authHeader = new HttpHeaders();
    authHeader.add("Authorization", "Bearer " + openShiftProperties.getToken());
    String URL =
        openshiftBaseURL + openShiftProperties.getProject() + suffix + target.getName() + logSuffix;
    HttpEntity<String> logEntity = new HttpEntity<>(authHeader);
    ResponseEntity<String> res = null;

    try {
      res = template.exchange(URL, HttpMethod.GET, logEntity, String.class);
    } catch (HttpServerErrorException e) {
      return "Build logs not anymore available";
    } catch (HttpClientErrorException e) {
      logger.error(e.getMessage(), e);
      return "Problems on communication with PaaS";
    }

    if (res.getStatusCode() != HttpStatus.OK) {
      logger.debug("Error retrieving logs " + res.getStatusCode() + " response " + res.toString());
    }
    if (res.getStatusCode().equals(HttpStatus.UNAUTHORIZED)) {
      throw new UnauthorizedException("Invalid or expired token");
    }

    logger.debug("Build logs of " + osName + " are " + res.getBody());
    return res.getBody();
  }

  public Build retrieveBuild(String osName) throws NotFoundException {
    Build res = null;
    Collection<Build> buildList = this.getBuilds();

    for (Build bd : buildList) {
      if (bd.getName().startsWith(osName + "-bc")) {
        res = bd;
        logger.trace(bd.toJson());
        break;
      }
    }
    if (res == null) {
      throw new NotFoundException("Not found builds of " + osName + "-bc");
    }
    return res;
  }

  public Collection<Build> getBuilds() {
    IList list = client.get(ResourceKind.BUILD, openShiftProperties.getProject());
    Collection<Build> buildList = new ArrayList<>();
    logger.trace("Build list: " + buildList);
    for (IResource resource : list.getItems()) {
      logger.trace("Build; " + resource.toJson());
      buildList.add((Build) resource);
    }
    return buildList;
  }

  public AppStatus getBuildStatus(String osName)
      throws org.project.openbaton.nubomedia.paas.exceptions.NotFoundException {

    AppStatus res = null;
    IBuildStatus status = retrieveBuild(osName).getBuildStatus();
    if (status != null) {
      switch (status.getPhase()) {
        case "Running":
          res = AppStatus.BUILDING;
          break;
        case "Failed":
          res = AppStatus.FAILED;
          break;
        case "Complete":
          res = AppStatus.BUILD_OK;
          break;
      }
    } else {
      res = AppStatus.PAAS_RESOURCE_MISSING;
    }

    return res;
  }
}
