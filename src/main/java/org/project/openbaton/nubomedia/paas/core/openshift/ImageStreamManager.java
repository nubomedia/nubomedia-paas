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
import com.openshift.restclient.ClientBuilder;
import com.openshift.restclient.IClient;
import com.openshift.restclient.ResourceKind;
import com.openshift.restclient.model.IImageStream;
import com.openshift.restclient.model.IResource;
import org.project.openbaton.nubomedia.paas.core.openshift.builders.MessageBuilderFactory;
import org.project.openbaton.nubomedia.paas.exceptions.openshift.DuplicatedException;
import org.project.openbaton.nubomedia.paas.exceptions.openshift.UnauthorizedException;
import org.project.openbaton.nubomedia.paas.model.openshift.ImageStreamConfig;
import org.project.openbaton.nubomedia.paas.properties.OpenShiftProperties;
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
public class ImageStreamManager {

  //  @Autowired private RestTemplate template;
  //  @Autowired private Gson mapper;
  private Logger logger;
  private String suffix;

  private IClient client;

  @Autowired private OpenShiftProperties openShiftProperties;

  @PostConstruct
  private void init() {
    this.logger = LoggerFactory.getLogger(this.getClass());
    this.suffix = "/imagestreams/";
    client =
        new ClientBuilder(openShiftProperties.getBaseURL())
            .usingToken(openShiftProperties.getToken())
            .build();
  }

  public IImageStream crateImageStream(String osName) {

    IImageStream is =
        client
            .getResourceFactory()
            .stub(ResourceKind.IMAGE_STREAM, osName, openShiftProperties.getProject());
    logger.debug("Creating imagestream {}", is);
    is = client.create(is);
    logger.debug("Generated imagestream {}", is);

    //    ImageStreamConfig message = MessageBuilderFactory.getImageStreamMessage(osName);
    //    logger.debug("Sending message " + mapper.toJson(message, ImageStreamConfig.class));
    //    String URL = baseURL + namespace + suffix;
    //    HttpEntity<String> imageStreamEntity =
    //        new HttpEntity<>(mapper.toJson(message, ImageStreamConfig.class), authHeader);
    //
    //    ResponseEntity<String> response =
    //        template.exchange(URL, HttpMethod.POST, imageStreamEntity, String.class);
    //    logger.debug("response " + response.getBody());
    //
    //    if (response.getStatusCode().equals(HttpStatus.CONFLICT)) {
    //      throw new DuplicatedException("Application with " + osName + " is already present");
    //    }
    //
    //    if (response.getStatusCode().equals(HttpStatus.UNAUTHORIZED)) {
    //
    //      throw new UnauthorizedException("Invalid or expired token");
    //    }

    return is;
  }

  public void deleteImageStream(String osName) throws UnauthorizedException {

    logger.debug("Deleting ImageStream for {}", osName);
    IResource imageStream =
        client
            .getResourceFactory()
            .stub(ResourceKind.IMAGE_STREAM, osName, openShiftProperties.getProject());
    client.delete(imageStream);
    logger.debug("Deleted ImageStream for {}", osName);
    //    String URL = baseURL + namespace + suffix + osName;
    //    HttpEntity<String> deleteEntity = new HttpEntity<>(authHeader);
    //    ResponseEntity<String> deleteResponse =
    //        template.exchange(URL, HttpMethod.DELETE, deleteEntity, String.class);
    //
    //    if (deleteResponse.getStatusCode() != HttpStatus.OK)
    //      logger.debug(
    //          "Error deleting imagestream for "
    //              + osName
    //              + " in project "
    //              + namespace
    //              + " response "
    //              + deleteEntity.toString());
    //
    //    if (deleteResponse.getStatusCode().equals(HttpStatus.UNAUTHORIZED)) {
    //
    //      throw new UnauthorizedException("Invalid or expired token");
    //    }
    //
    //    return deleteResponse.getStatusCode();
  }
}
