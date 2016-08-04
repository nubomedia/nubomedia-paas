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

import com.google.gson.Gson;
import org.project.openbaton.nubomedia.paas.core.openshift.builders.MessageBuilderFactory;
import org.project.openbaton.nubomedia.paas.exceptions.openshift.DuplicatedException;
import org.project.openbaton.nubomedia.paas.exceptions.openshift.UnauthorizedException;
import org.project.openbaton.nubomedia.paas.model.openshift.BuildConfig;
import org.project.openbaton.nubomedia.paas.model.openshift.RouteConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;

/**
 * Created by maa on 08.10.15.
 */
@Service
public class BuildConfigManager {

  @Autowired private RestTemplate template;
  @Autowired private Gson mapper;
  private Logger logger;
  private String suffix;

  @PostConstruct
  public void init() {
    this.logger = LoggerFactory.getLogger(this.getClass());
    this.suffix = "/buildconfigs/";
  }

  public ResponseEntity<String> createBuildConfig(
      String baseURL,
      String appName,
      String namespace,
      String dockerRepo,
      String gitURL,
      HttpHeaders authHeader,
      String secretName,
      String mediaServerGID,
      String mediaServerIP,
      String mediaServerPort,
      String cloudRepositoryIp,
      String cloudRepoPort,
      String cdnServerIp,
      RouteConfig routeConfig)
      throws DuplicatedException, UnauthorizedException {

    BuildConfig message =
        MessageBuilderFactory.getBuilderMessage(
            appName,
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
    logger.debug("writing message " + mapper.toJson(message, BuildConfig.class));
    String URL = baseURL + namespace + suffix;
    HttpEntity<String> buildEntity =
        new HttpEntity<>(mapper.toJson(message, BuildConfig.class), authHeader);
    ResponseEntity response = template.exchange(URL, HttpMethod.POST, buildEntity, String.class);
    logger.debug("Build response: " + response.toString());

    if (response.getStatusCode().equals(HttpStatus.CONFLICT)) {
      throw new DuplicatedException("Application with " + appName + " is already present");
    }
    if (response.getStatusCode().equals(HttpStatus.UNAUTHORIZED)) {

      throw new UnauthorizedException("Invalid or expired token");
    }

    return response;
  }

  public HttpStatus deleteBuildConfig(
      String baseURL, String appName, String namespace, HttpHeaders authHeader)
      throws UnauthorizedException {

    String URL = baseURL + namespace + suffix + appName + "-bc";
    HttpEntity<String> deleteEntity = new HttpEntity<>(authHeader);
    ResponseEntity<String> responseEntity =
        template.exchange(URL, HttpMethod.DELETE, deleteEntity, String.class);

    if (responseEntity.getStatusCode().equals(HttpStatus.UNAUTHORIZED)) {

      throw new UnauthorizedException("Invalid or expired token");
    }

    return responseEntity.getStatusCode();
  }
}
