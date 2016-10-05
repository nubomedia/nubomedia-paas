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
import com.openshift.internal.restclient.model.Secret;
import com.openshift.internal.restclient.model.ServiceAccount;
import com.openshift.restclient.ClientBuilder;
import com.openshift.restclient.IClient;
import com.openshift.restclient.ResourceKind;
import com.openshift.restclient.model.IList;
import com.openshift.restclient.model.IResource;
import org.project.openbaton.nubomedia.paas.core.openshift.builders.MessageBuilderFactory;
import org.project.openbaton.nubomedia.paas.exceptions.NotFoundException;
import org.project.openbaton.nubomedia.paas.exceptions.openshift.UnauthorizedException;
import org.project.openbaton.nubomedia.paas.model.openshift.SecretConfig;
import org.project.openbaton.nubomedia.paas.model.openshift.SecretID;
import org.project.openbaton.nubomedia.paas.properties.OpenShiftProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by maa on 06.10.15.
 */
@Service
public class SecretManager {

  @Autowired private Gson mapper;
  @Autowired private RestTemplate template;
  private Logger logger;
  private String secretSuffix;
  private String saSuffix;

  private IClient client;

  @Autowired private OpenShiftProperties openShiftProperties;

  @PostConstruct
  private void init() {
    this.logger = LoggerFactory.getLogger(this.getClass());
    this.secretSuffix = "/secrets";
    this.saSuffix = "/serviceaccounts/builder";
    client =
        new ClientBuilder(openShiftProperties.getBaseURL())
            .usingToken(openShiftProperties.getToken())
            //              .sslCertificate("nubomedia", null)
            //                              (X509Certificate) javax.security.cert.X509Certificate.getInstance(new FileInputStream(new File(openshiftKeystore))))
            .build();
  }

  public String createSecret(String privateKey) {
    //      throws UnauthorizedException {

    SecretConfig message =
        MessageBuilderFactory.getSecretMessage(openShiftProperties.getProject(), privateKey);
    String secretJson = mapper.toJson(message, Secret.class);
    Secret secretConfig = client.getResourceFactory().create(secretJson);

    logger.debug("Creating secret {}", secretConfig);
    secretConfig = client.create(secretConfig);
    logger.debug("Created secret {}", secretConfig);
    //    logger.debug("message " + mapper.toJson(message, SecretConfig.class));
    //    HttpEntity<String> secretEntity =
    //        new HttpEntity<>(mapper.toJson(message, SecretConfig.class), authHeader);
    //    ResponseEntity<String> responseEntity =
    //        template.exchange(
    //            baseURL + namespace + secretSuffix, HttpMethod.POST, secretEntity, String.class);

    //    logger.debug("response entity " + responseEntity.toString());
    //    SecretConfig response = mapper.fromJson(responseEntity.getBody(), SecretConfig.class);
    //    String secretName = response.getMetadata().getName();
    //
    //    responseEntity = this.updateBuilder(baseURL, secretName, authHeader, namespace, true);
    //    if (responseEntity.getStatusCode() != HttpStatus.OK)
    //      logger.debug("Error updating builder account " + response.toString());
    //
    //    if (responseEntity.getStatusCode().equals(HttpStatus.UNAUTHORIZED)) {
    //
    //      throw new UnauthorizedException("Invalid or expired token");
    //    }

    return secretConfig.getName();
  }

  public void deleteSecret(String secretName) {

    logger.info("Deleting secret " + secretName);
    IResource build =
        client
            .getResourceFactory()
            .stub(ResourceKind.SECRET, secretName, openShiftProperties.getProject());
    client.delete(build);
    logger.info("Deleted secret " + secretName);

    //      throws UnauthorizedException {
    //    HttpEntity<String> deleteEntity = new HttpEntity<>("", authHeader);
    //    ResponseEntity<String> entity =
    //        template.exchange(
    //            baseURL + namespace + secretSuffix + "/" + secretName,
    //            HttpMethod.DELETE,
    //            deleteEntity,
    //            String.class);
    //
    //    if (entity.getStatusCode() != HttpStatus.OK)
    //      logger.debug("Error deleting secret " + secretName + " response " + entity.toString());
    //
    //    if (entity.getStatusCode().equals(HttpStatus.UNAUTHORIZED)) {
    //
    //      throw new UnauthorizedException("Invalid or expired token");
    //    }
    //
    //    entity = this.updateBuilder(baseURL, secretName, authHeader, namespace, false);
    //
    //    if (entity.getStatusCode() != HttpStatus.OK)
    //      logger.debug("Error updating builder account " + entity.toString());
    //
    //    if (entity.getStatusCode().equals(HttpStatus.UNAUTHORIZED)) {
    //
    //      throw new UnauthorizedException("Invalid or expired token");
    //    }
    //
    //    return entity.getStatusCode();
  }

  //BETA!!!!
  public List<String> getSecretList() throws NotFoundException {
    //      throws UnauthorizedException {
    List<String> res = new ArrayList<>();

    ServiceAccount serviceAccount = null;

    IList list = client.get(ResourceKind.SERVICE_ACCOUNT, openShiftProperties.getProject());
    for (IResource resource : list.getItems()) {
      logger.trace(resource.toJson());
      if (resource.getName().equals("builder")) {
        serviceAccount = (ServiceAccount) resource;
      }

      //    String url = baseURL + namespace + saSuffix;
      //    HttpEntity<String> builderEntity = new HttpEntity<>(authHeader);
      //    ResponseEntity<String> serviceAccountBuilder =
      //        template.exchange(url, HttpMethod.GET, builderEntity, String.class);

      //    if (serviceAccountBuilder.getStatusCode() != HttpStatus.OK)
      //      logger.debug("Error updating builder account " + serviceAccountBuilder.toString());

      //    if (serviceAccountBuilder.getStatusCode().equals(HttpStatus.UNAUTHORIZED)) {

      //      throw new UnauthorizedException("Invalid or expired token");
    }

    //    ServiceAccount serviceAccount =
    //        mapper.fromJson(serviceAccountBuilder.getBody(), ServiceAccount.class);
    if (serviceAccount == null) {
      throw new NotFoundException("Not found ServiceAccount builder");
    }
    for (String secret : serviceAccount.getSecrets()) {
      if (secret.contains(openShiftProperties.getProject())) {
        res.add(secret);
      }
    }

    return res;
  }

  private String updateBuilder(String secretName, boolean add) throws NotFoundException {
    //    String URL = baseURL + namespace + saSuffix;
    //    HttpEntity<String> builderEntity = new HttpEntity<>(authHeader);
    //    ResponseEntity<String> serviceAccount =
    //        template.exchange(URL, HttpMethod.GET, builderEntity, String.class);

    ServiceAccount serviceAccount = null;

    IList list = client.get(ResourceKind.SERVICE_ACCOUNT, openShiftProperties.getProject());
    for (IResource resource : list.getItems()) {
      logger.trace(resource.toJson());
      if (resource.getName().equals("builder")) {
        serviceAccount = (ServiceAccount) resource;
      }
    }
    if (serviceAccount == null) {
      throw new NotFoundException("Not found ServiceAccount builder");
    }

    logger.debug("Builder account " + serviceAccount.toJson());

    //    ServiceAccount builder = mapper.fromJson(serviceAccount.getBody(), ServiceAccount.class);
    Collection<String> secrets = serviceAccount.getSecrets();
    Collection<String> newSecrets = new ArrayList<>();

    if (add) {
      serviceAccount.addSecret(secretName);

    } else {
      serviceAccount.getSecrets().remove(secretName);
    }

    serviceAccount = client.update(serviceAccount);
    //    builderEntity = new HttpEntity<>(mapper.toJson(builder, ServiceAccount.class), authHeader);
    //    serviceAccount = template.exchange(URL, HttpMethod.PUT, builderEntity, String.class);

    logger.debug("New builder account " + serviceAccount.toJson());

    return serviceAccount.toJson();
  }
}
