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
import com.openshift.internal.restclient.model.properties.ResourcePropertiesRegistry;
import com.openshift.restclient.ClientBuilder;
import com.openshift.restclient.IClient;
import com.openshift.restclient.ResourceKind;
import com.openshift.restclient.model.IResource;
import com.openshift.restclient.model.IService;
import org.jboss.dmr.ModelNode;
import org.project.openbaton.nubomedia.paas.core.openshift.builders.MessageBuilderFactory;
import org.project.openbaton.nubomedia.paas.exceptions.BadRequestException;
import org.project.openbaton.nubomedia.paas.exceptions.openshift.DuplicatedException;
import org.project.openbaton.nubomedia.paas.exceptions.openshift.UnauthorizedException;
import org.project.openbaton.nubomedia.paas.model.openshift.ServiceConfig;
import org.project.openbaton.nubomedia.paas.properties.OpenShiftProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;

/**
 * Created by maa on 08.10.15.
 */
@Service
public class ServiceManager {

  @Autowired private RestTemplate template;
  @Autowired private Gson mapper;
  private Logger logger;
  //  private String suffix;

  private IClient client;

  @Autowired private OpenShiftProperties openShiftProperties;

  @PostConstruct
  private void init() {
    this.logger = LoggerFactory.getLogger(this.getClass());
    //    this.suffix = "/services/";
    client =
        new ClientBuilder(openShiftProperties.getBaseURL())
            .usingToken(openShiftProperties.getToken())
            .build();
  }

  public IService makeService(
      String osName, List<Integer> ports, List<Integer> targetPorts, List<String> protocols) {

    ModelNode serivceNode =
        ModelNode.fromJSONString(
            mapper.toJson(
                MessageBuilderFactory.getServiceMessage(
                    openShiftProperties.getProject(), osName, ports, targetPorts, protocols),
                ServiceConfig.class));
    Map propertyServiceKeys =
        ResourcePropertiesRegistry.getInstance().get("v1", ResourceKind.SERVICE);
    IService service =
        new com.openshift.internal.restclient.model.Service(
            serivceNode, client, propertyServiceKeys);
    logger.debug("Created SerivceConfig {}", service);
    service = client.create(service);
    logger.debug("Triggered Serivce creation {}", service);

    //    ServiceConfig message =
    //        MessageBuilderFactory.getServiceMessage(namespace, osName, ports, targetPorts, protocols);
    //
    //    logger.debug("Writing service creation " + mapper.toJson(message, ServiceConfig.class));
    //
    //    String URL = kubernetesBaseURL + namespace + suffix;
    //    ResponseEntity response = null;
    //    try {
    //      HttpEntity<String> serviceEntity =
    //          new HttpEntity<>(mapper.toJson(message, ServiceConfig.class), authHeader);
    //      response = template.exchange(URL, HttpMethod.POST, serviceEntity, String.class);
    //    } catch (Exception e) {
    //      logger.error(e.getMessage(), e);
    //    }
    //
    //    if (response == null) {
    //      throw new BadRequestException(
    //          "Bad request towards OpenShift: " + mapper.toJson(message, ServiceConfig.class));
    //    }
    //
    //    if (response.getStatusCode().equals(HttpStatus.CONFLICT)) {
    //      throw new DuplicatedException("Application with " + osName + " is already present");
    //    }
    //
    //    if (response.getStatusCode().equals(HttpStatus.UNAUTHORIZED)) {
    //
    //      throw new UnauthorizedException("Invalid or expired token");
    //    }

    return service;
  }

  public void deleteService(String osName) {
    logger.debug("Deleting Serivce {}", osName + "-svc");

    IResource build =
        client
            .getResourceFactory()
            .stub(ResourceKind.SERVICE, osName + "-svc", openShiftProperties.getProject());
    client.delete(build);
    logger.debug("Deleted Serivce {}", osName + "-svc");

    //      throws UnauthorizedException {
    //    String URL = kubernetesBaseURL + namespace + suffix + osName + "-svc";
    //    HttpEntity<String> deleteEntity = new HttpEntity<>(authHeader);
    //    ResponseEntity<String> deleteResponse =
    //        template.exchange(URL, HttpMethod.DELETE, deleteEntity, String.class);

    //    if (deleteResponse.getStatusCode() != HttpStatus.OK)
    //      logger.debug(
    //          "Error deleting service " + osName + "-svc response " + deleteResponse.toString());

    //    if (deleteResponse.getStatusCode().equals(HttpStatus.UNAUTHORIZED)) {

    //      throw new UnauthorizedException("Invalid or expired token");
    //    }

    //    return deleteResponse.getStatusCode();
  }
}
