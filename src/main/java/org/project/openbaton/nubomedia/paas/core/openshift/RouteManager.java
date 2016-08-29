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
import org.project.openbaton.nubomedia.paas.exceptions.openshift.DuplicatedException;
import org.project.openbaton.nubomedia.paas.exceptions.openshift.UnauthorizedException;
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
public class RouteManager {

  @Autowired private RestTemplate template;
  @Autowired private Gson mapper;
  private Logger logger;
  private String suffix;

  @PostConstruct
  private void init() {
    this.logger = LoggerFactory.getLogger(this.getClass());
    this.suffix = "/routes/";
  }

  public ResponseEntity<String> makeRoute(
      String baseURL,
      String osName,
      String namespace,
      HttpHeaders authHeader,
      RouteConfig routeConfig)
      throws DuplicatedException, UnauthorizedException {
    //RouteConfig message = MessageBuilderFactory.getRouteMessage(name, appID, domainName);

    //logger.debug("Route message " + mapper.toJson(message,RouteConfig.class));
    logger.debug("Route message " + mapper.toJson(routeConfig, RouteConfig.class));

    String URL = baseURL + namespace + suffix;
    //HttpEntity<String> routeEntity = new HttpEntity<String>(mapper.toJson(message, RouteConfig.class), authHeader);
    HttpEntity<String> routeEntity =
        new HttpEntity<>(mapper.toJson(routeConfig, RouteConfig.class), authHeader);
    ResponseEntity response = template.exchange(URL, HttpMethod.POST, routeEntity, String.class);

    if (response.getStatusCode().equals(HttpStatus.CONFLICT)) {
      throw new DuplicatedException("Application with " + osName + " is already present");
    }

    if (response.getStatusCode().equals(HttpStatus.UNAUTHORIZED)) {

      throw new UnauthorizedException("Invalid or expired token");
    }

    logger.debug("ROUTE BODY IS " + response.getBody());

    return response;
  }

  public HttpStatus deleteRoute(
      String baseURL, String osName, String namespace, HttpHeaders authHeader)
      throws UnauthorizedException {
    String URL = baseURL + namespace + suffix + osName + "-route";
    HttpEntity<String> deleteEntity = new HttpEntity<>(authHeader);
    ResponseEntity<String> deleteResponse =
        template.exchange(URL, HttpMethod.DELETE, deleteEntity, String.class);

    if (deleteResponse.getStatusCode() != HttpStatus.OK)
      logger.debug(
          "Error deleting route " + osName + "-route response " + deleteResponse.toString());

    if (deleteResponse.getStatusCode().equals(HttpStatus.UNAUTHORIZED)) {

      throw new UnauthorizedException("Invalid or expired token");
    }

    return deleteResponse.getStatusCode();
  }
}
