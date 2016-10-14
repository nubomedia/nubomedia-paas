/*
 *
 *  * (C) Copyright 2016 NUBOMEDIA (http://www.nubomedia.eu)
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *   http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *  *
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
import org.project.openbaton.nubomedia.paas.model.openshift.SecretConfig;
import org.project.openbaton.nubomedia.paas.properties.OpenShiftProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
  private Logger logger;

  private IClient client;

  @Autowired private OpenShiftProperties openShiftProperties;

  @PostConstruct
  private void init() {
    this.logger = LoggerFactory.getLogger(this.getClass());
    client =
        new ClientBuilder(openShiftProperties.getBaseURL())
            .usingToken(openShiftProperties.getToken())
            .build();
  }

  public String createSecret(String privateKey) {
    SecretConfig message =
        MessageBuilderFactory.getSecretMessage(openShiftProperties.getProject(), privateKey);
    String secretJson = mapper.toJson(message, Secret.class);
    Secret secretConfig = client.getResourceFactory().create(secretJson);

    logger.debug("Creating secret {}", secretConfig);
    secretConfig = client.create(secretConfig);
    logger.debug("Created secret {}", secretConfig);
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
  }

  public List<String> getSecretList() throws NotFoundException {
    List<String> res = new ArrayList<>();

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
    for (String secret : serviceAccount.getSecrets()) {
      if (secret.contains(openShiftProperties.getProject())) {
        res.add(secret);
      }
    }

    return res;
  }

  private String updateBuilder(String secretName, boolean add) throws NotFoundException {
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

    Collection<String> secrets = serviceAccount.getSecrets();
    Collection<String> newSecrets = new ArrayList<>();

    if (add) {
      serviceAccount.addSecret(secretName);

    } else {
      serviceAccount.getSecrets().remove(secretName);
    }
    serviceAccount = client.update(serviceAccount);
    logger.debug("New builder account " + serviceAccount.toJson());
    return serviceAccount.toJson();
  }
}
