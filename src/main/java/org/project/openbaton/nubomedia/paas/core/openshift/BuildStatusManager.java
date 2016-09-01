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

package org.project.openbaton.nubomedia.paas.core.openshift;

import com.google.gson.Gson;
import org.project.openbaton.nubomedia.paas.exceptions.openshift.UnauthorizedException;
import org.project.openbaton.nubomedia.paas.messages.AppStatus;
import org.project.openbaton.nubomedia.paas.model.openshift.Build;
import org.project.openbaton.nubomedia.paas.model.openshift.BuildList;
import org.project.openbaton.nubomedia.paas.model.openshift.BuildStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;

/**
 * Created by maa on 08.10.15.
 */
@Service
public class BuildStatusManager {

  @Autowired private RestTemplate template;
  @Autowired private Gson mapper;
  private Logger logger;
  private String suffix;
  private String logSuffix;

  @PostConstruct
  private void init() {
    this.logger = LoggerFactory.getLogger(this.getClass());
    this.suffix = "/builds/";
    this.logSuffix = "/log";
  }

  //Responses: Complete, Running, Failed
  public AppStatus getBuildStatus(
      String baseURL, String osName, String namespace, HttpHeaders authHeader)
      throws UnauthorizedException {

    AppStatus res = null;
    BuildStatus status = null;
    BuildList buildList = this.getBuilds(baseURL, namespace, authHeader);

    for (Build b : buildList.getItems()) {
      if (b.getStatus().getConfig().getName().equals(osName + "-bc")) {
        logger.trace("build is " + mapper.toJson(b, Build.class));
        status = b.getStatus();
      }
    }

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

  public HttpStatus deleteBuilds(
      String baseURL, String osName, String namespace, HttpHeaders authHeader)
      throws UnauthorizedException {

    Build target = this.retrieveBuild(baseURL, osName, namespace, authHeader);
    String URL = baseURL + namespace + suffix + target.getMetadata().getName();
    HttpEntity<String> deleteEntity = new HttpEntity<>(authHeader);
    logger.debug("Deleting " + osName + " builds of project " + namespace);
    ResponseEntity<String> responseEntity =
        template.exchange(URL, HttpMethod.DELETE, deleteEntity, String.class);

    if (responseEntity.getStatusCode() != HttpStatus.OK) logger.debug(responseEntity.toString());

    if (responseEntity.getStatusCode().equals(HttpStatus.UNAUTHORIZED)) {

      throw new UnauthorizedException("Invalid or expired token");
    }

    return responseEntity.getStatusCode();
  }

  public String retrieveLogs(
      String baseURL, String osName, String namespace, HttpHeaders authHeader)
      throws UnauthorizedException {

    Build target = this.retrieveBuild(baseURL, osName, namespace, authHeader);
    String URL = baseURL + namespace + suffix + target.getMetadata().getName() + logSuffix;
    HttpEntity<String> logEntity = new HttpEntity<>(authHeader);
    ResponseEntity<String> res = null;

    try {
      res = template.exchange(URL, HttpMethod.GET, logEntity, String.class);
    } catch (HttpServerErrorException e) {
      return "Build logs not anymore available";
    } catch (HttpClientErrorException e) {
      return "Problems on communication with PaaS";
    }

    if (res.getStatusCode() != HttpStatus.OK) {
      logger.debug("Error retrieving logs " + res.getStatusCode() + " response " + res.toString());
    }
    if (res.getStatusCode().equals(HttpStatus.UNAUTHORIZED)) {

      throw new UnauthorizedException("Invalid or expired token");
    }

    return res.getBody();
  }

  private Build retrieveBuild(
      String baseURL, String osName, String namespace, HttpHeaders authHeader)
      throws UnauthorizedException {

    Build res = null;
    BuildList buildList = this.getBuilds(baseURL, namespace, authHeader);

    for (Build bd : buildList.getItems()) {

      if (bd.getStatus().getConfig().getName().equals(osName + "-bc")) {
        logger.trace("build is " + mapper.toJson(bd, Build.class));
        res = bd;
      }
    }
    return res;
  }

  private BuildList getBuilds(String baseURL, String namespace, HttpHeaders authHeader)
      throws UnauthorizedException {
    String URL = baseURL + namespace + suffix;
    HttpEntity<String> buildEntity = new HttpEntity<>(authHeader);
    ResponseEntity<String> builds =
        template.exchange(URL, HttpMethod.GET, buildEntity, String.class);
    logger.trace("BuildsList " + builds.getStatusCode() + " response " + builds.toString());

    if (builds.getStatusCode() != HttpStatus.OK) {
      logger.debug(
          "Error retrieving builds " + builds.getStatusCode() + " response " + builds.toString());
    }

    if (builds.getStatusCode().equals(HttpStatus.UNAUTHORIZED)) {

      throw new UnauthorizedException("Invalid or expired token");
    }

    return mapper.fromJson(builds.getBody(), BuildList.class);
  }
}
