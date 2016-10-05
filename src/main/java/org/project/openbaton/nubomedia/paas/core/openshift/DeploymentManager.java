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
import com.openshift.internal.restclient.model.Pod;
import com.openshift.internal.restclient.model.ReplicationController;
import com.openshift.internal.restclient.model.properties.ResourcePropertiesRegistry;
import com.openshift.restclient.ClientBuilder;
import com.openshift.restclient.IClient;
import com.openshift.restclient.ResourceKind;
import com.openshift.restclient.model.IDeploymentConfig;
import com.openshift.restclient.model.IList;
import com.openshift.restclient.model.IResource;
import org.jboss.dmr.ModelNode;
import org.project.openbaton.nubomedia.paas.core.openshift.builders.MessageBuilderFactory;
import org.project.openbaton.nubomedia.paas.exceptions.openshift.DuplicatedException;
import org.project.openbaton.nubomedia.paas.exceptions.openshift.UnauthorizedException;
import org.project.openbaton.nubomedia.paas.messages.AppStatus;
import org.project.openbaton.nubomedia.paas.model.openshift.HorizontalPodAutoscaler;
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
import java.util.List;
import java.util.Map;

/**
 * Created by maa on 08.10.15.
 */
@Service
public class DeploymentManager {

  @Autowired private RestTemplate template;
  @Autowired private Gson mapper;
  private Logger logger;
  private String suffix;
  private String podSuffix;
  private String rcSuffix;

  private String kubernetesBaseURL;

  private IClient client;

  @Autowired private OpenShiftProperties openShiftProperties;

  @PostConstruct
  private void init() {
    this.logger = LoggerFactory.getLogger(this.getClass());
    this.suffix = "/deploymentconfigs/";
    this.podSuffix = "/pods/";
    this.rcSuffix = "/replicationcontrollers/";
    this.kubernetesBaseURL = openShiftProperties.getBaseURL() + "/api/v1/namespaces/";
    client =
        new ClientBuilder(openShiftProperties.getBaseURL())
            .usingToken(openShiftProperties.getToken())
            .build();
  }

  public IDeploymentConfig makeDeployment(
      String osName,
      String dockerRepo,
      List<Integer> ports,
      List<String> protocols,
      int repnumbers) {

    logger.debug(
        "params arg: "
            + osName
            + " "
            + dockerRepo
            + " "
            + ports
            + " "
            + protocols
            + " "
            + repnumbers);

    logger.debug("Creating DeploymentConfig ...");
    ModelNode deployNode =
        ModelNode.fromJSONString(
            mapper.toJson(
                MessageBuilderFactory.getDeployMessage(
                    osName,
                    dockerRepo,
                    ports,
                    protocols,
                    repnumbers,
                    openShiftProperties.getProject()),
                org.project.openbaton.nubomedia.paas.model.openshift.DeploymentConfig.class));
    Map propertyKeys =
        ResourcePropertiesRegistry.getInstance().get("v1", ResourceKind.DEPLOYMENT_CONFIG);
    IDeploymentConfig deploymentConfig =
        new com.openshift.internal.restclient.model.DeploymentConfig(
            deployNode, client, propertyKeys);

    logger.debug("Created DeploymentConfig {}", deploymentConfig);
    deploymentConfig = client.create(deploymentConfig);
    logger.debug("Triggered deployment {}", deploymentConfig.getName());

    //    DeploymentConfig message =
    //        MessageBuilderFactory.getDeployMessage(
    //            osName, dockerRepo, ports, protocols, repnumbers, namespace);
    //    logger.debug(mapper.toJson(message, DeploymentConfig.class));
    //    String URL = baseURL + namespace + suffix;
    //    HttpEntity<String> deployEntity =
    //        new HttpEntity<>(mapper.toJson(message, DeploymentConfig.class), authHeader);
    //    ResponseEntity<String> response =
    //        template.exchange(URL, HttpMethod.POST, deployEntity, String.class);
    //    logger.debug("Deployment response: " + response);
    //
    //    if (response.getStatusCode().equals(HttpStatus.CONFLICT)) {
    //      throw new DuplicatedException("Application with " + osName + " is already present");
    //    }
    //
    //    if (response.getStatusCode().equals(HttpStatus.UNAUTHORIZED)) {
    //
    //      throw new UnauthorizedException("Invalid or expired token");
    //    }

    return deploymentConfig;
  }

  public void deleteDeploymentConfig(String osName) {
    //      throws UnauthorizedException {

    logger.debug("Deleting deployment config " + osName);

    IResource deplyoment =
        client
            .getResourceFactory()
            .stub(ResourceKind.DEPLOYMENT_CONFIG, osName + "-dc", openShiftProperties.getProject());
    client.delete(deplyoment);
    //    String URL = baseURL + namespace + suffix + osName + "-dc";
    //    HttpEntity<String> deleteEntity = new HttpEntity<>(authHeader);
    //    ResponseEntity<String> res =
    //        template.exchange(URL, HttpMethod.DELETE, deleteEntity, String.class);

    //    if (res.getStatusCode() != HttpStatus.OK) {
    //      logger.debug(res.toString());
    //    }
    //
    //    if (res.getStatusCode().equals(HttpStatus.UNAUTHORIZED)) {
    //
    //      throw new UnauthorizedException("Invalid or expired token");
    //    }

    //    return res.getStatusCode();
  }

  public void deleteReplicationController(String osName) {
    logger.debug("Deleting ReplicationController of " + osName);
    Collection<ReplicationController> replicationControllersToDelete = new ArrayList<>();
    IList rcs = client.get(ResourceKind.REPLICATION_CONTROLLER, openShiftProperties.getProject());
    logger.trace("ReplicationController list: " + rcs.toJson());
    for (IResource rc : rcs.getItems()) {
      logger.trace("ReplicationController : " + rc.toJson());
      if (rc.getName().contains(osName)) {
        replicationControllersToDelete.add((ReplicationController) rc);
      }
    }
    logger.trace("Deleting ReplicationController: " + replicationControllersToDelete);
    for (ReplicationController rc : replicationControllersToDelete) {
      logger.trace("Deleting ReplicationController: " + rc);
      client.delete(rc);
      logger.trace("Deleted ReplicationController: " + rc);
    }
    logger.debug("Deleted ReplicationController of " + osName);
  }

  public void deletePods(String osName) {
    //      throws UnauthorizedException {

    //    HttpEntity<String> requestEntity = new HttpEntity<>(authHeader);
    //    String rcURL = kubernetesBaseURL + namespace + rcSuffix + osName + "-dc-1";

    //    if (!this.existRC(requestEntity, rcURL)) {
    //      return HttpStatus.OK;
    //    }

    logger.debug("Deleting pods of " + osName);
    //    ResponseEntity<String> deleteEntity =
    //        template.exchange(rcURL, HttpMethod.DELETE, requestEntity, String.class);

    IList pods = client.get(ResourceKind.POD, openShiftProperties.getProject());
    logger.trace("Pod list: " + pods.toJson());
    for (IResource pod : pods.getItems()) {
      logger.trace("Pod: " + pod.toJson());
      if (pod.getName().contains(osName)) {
        logger.trace("Deleting Pod: " + pod);
        client.delete(pod);
        logger.trace("Deleted Pod: " + pod);
      }
    }
    logger.debug("Deleted Pods of " + osName);
    //    if (deleteEntity.getStatusCode() == HttpStatus.OK) {
    //      String podsURL = kubernetesBaseURL + namespace + podSuffix;
    //      Pods pods = this.getPodsList(podsURL, requestEntity);
    //      for (String pod : pods.getPodNames()) {
    //
    //        if (pod.contains(osName)) {
    //          deleteEntity =
    //              template.exchange(podsURL + pod, HttpMethod.DELETE, requestEntity, String.class);
    //          if (deleteEntity.getStatusCode() != HttpStatus.OK) {
    //            logger.debug(
    //                "Error HTTP:"
    //                    + deleteEntity.getStatusCode()
    //                    + " for POD "
    //                    + pod
    //                    + " response "
    //                    + deleteEntity.toString());
    //            break;
    //          }
    //        }
    //      }
    //    } else if (deleteEntity
    //        .getStatusCode()
    //        .equals(
    //            HttpStatus
    //                .NOT_FOUND)) { //means that you are deleting an application before the build was complete
    //      logger.debug("Status code " + deleteEntity.getStatusCode());
    //      return HttpStatus.OK;
    //    }

    //    if (deleteEntity.getStatusCode().equals(HttpStatus.UNAUTHORIZED)) {
    //
    //      throw new UnauthorizedException("Invalid or expired token");
    //    }
    //
    //    return deleteEntity.getStatusCode();
  }

  public AppStatus getDeployStatus(String osName) {

    AppStatus res =
        AppStatus
            .RUNNING; //if deploy pod will not exist is because the application is already deployed or the build is already failed :P
    //    String podsURL = kubernetesBaseURL + namespace + podSuffix;
    //    HttpEntity<String> podEntity = new HttpEntity<>(authHeader);
    List<Pod> podList = this.getPodsList();

    for (Pod pod : podList) {
      if (pod.getName().equals(osName + "-dc-1-deploy")) {
        //        ResponseEntity<String> deployPod =
        //            template.exchange(podsURL + podName, HttpMethod.GET, podEntity, String.class);
        //        Pod targetPod = mapper.fromJson(deployPod.getBody(), Pod.class);
        switch (pod.getStatus()) {
          case "Running":
            res = AppStatus.DEPLOYNG;
            break;
          case "Failed":
            res = AppStatus.FAILED;
            break;
          case "Complete":
            res = AppStatus.RUNNING;
            break;
        }
      }
    }

    return res;
  }

  public List<String> getPodNameList(String osName) {
    //      throws UnauthorizedException {
    //    String podsURL = kubernatesBaseURL + namespace + podSuffix;
    List<Pod> podList = this.getPodsList();
    logger.debug("POD LIST is " + podList.toString());
    List<String> res = new ArrayList<>();

    for (Pod pod : podList) {
      logger.trace("Current pod is " + pod.getName());
      CharSequence sequence = osName + "-dc-1";
      if (pod.getName().contains(sequence)) {
        logger.trace("Probably found target " + pod.getName());
        if (!pod.getName().contains("bc-1-build") || !pod.getName().contains("-deploy")) {
          logger.trace("Find compatible pod with name " + pod.getName());
          res.add(pod.getName());
        }
      }
    }

    logger.trace("RES is " + res.toString());
    return res;
  }

  public String getPodLogs(String osName, String podName) throws UnauthorizedException {
    logger.debug("Getting log of pod " + podName);
    String podsURL = kubernetesBaseURL + openShiftProperties.getProject() + podSuffix;
    HttpHeaders authHeader = new HttpHeaders();
    authHeader.add("Authorization", "Bearer " + openShiftProperties.getToken());
    HttpEntity<String> requestEntity = new HttpEntity<>(authHeader);
    /*        String targetPod = null;
    Pods podList = this.getPodsList(podsURL,requestEntity);
    logger.debug("POD LIST IS " + podList.toString());

    for(String pod : podList.getPodNames()){
        logger.debug("CURRENT POD IS " + pod);
        CharSequence sequence = appName + "-dc-1";
        if (pod.contains(sequence)) {
            logger.debug("Running with pod " + pod);
            if (!pod.contains("bc-1-build") || !pod.contains("-deploy")) {
                targetPod = pod;
                logger.debug("Target pod is " + targetPod);
            }
        }
    }*/

    String targetUrl = podsURL + podName + "/log";
    ResponseEntity<String> logEntity = null;
    try {

      logEntity = template.exchange(targetUrl, HttpMethod.GET, requestEntity, String.class);
    } catch (HttpClientErrorException e) {
      return "No log available for the application " + osName;
    } catch (HttpServerErrorException e) {
      return "Pod(s) crashed for too long time, logs are not anymore available";
    }

    if (!logEntity.getStatusCode().is2xxSuccessful())
      logger.debug("FAILED TO RETRIEVE LOGS " + logEntity.getBody());

    if (logEntity.getStatusCode().equals(HttpStatus.UNAUTHORIZED)) {
      throw new UnauthorizedException("Invalid or expired token");
    }

    logger.debug("Got log of pod " + podName + ":" + logEntity.getBody());
    return logEntity.getBody();
  }

  public HorizontalPodAutoscaler createHpa(
      String paasUrl,
      String osName,
      String namespace,
      int replicasNumber,
      int targetPerc,
      HttpHeaders authHeader)
      throws UnauthorizedException, DuplicatedException {

    String url =
        paasUrl + "/apis/extensions/v1beta1/namespaces/" + namespace + "/horizontalpodautoscalers";
    HorizontalPodAutoscaler body = MessageBuilderFactory.getHpa(osName, replicasNumber, targetPerc);
    HttpEntity<String> hpaEntity =
        new HttpEntity<>(mapper.toJson(body, HorizontalPodAutoscaler.class), authHeader);
    ResponseEntity<String> createHpa =
        template.exchange(url, HttpMethod.POST, hpaEntity, String.class);

    if (createHpa.getStatusCode().equals(HttpStatus.CONFLICT)) {
      throw new DuplicatedException("Application with " + osName + " is already present");
    }

    if (createHpa.getStatusCode().equals(HttpStatus.UNAUTHORIZED)) {

      throw new UnauthorizedException("Invalid or expired token");
    }

    return mapper.fromJson(createHpa.getBody(), HorizontalPodAutoscaler.class);
  }

  private List<Pod> getPodsList() {
    IList pods = client.get(ResourceKind.POD, openShiftProperties.getProject());
    List<Pod> podList = new ArrayList<>();
    for (IResource pod : pods.getItems()) {
      logger.info(pod.toJson());
      podList.add((Pod) pod);
    }
    //    ResponseEntity<String> pods =
    //        template.exchange(podsURL, HttpMethod.GET, requestEntity, String.class);
    //    logger.trace(pods.getBody());
    //    return mapper.fromJson(pods.getBody(), Pods.class);
    return podList;
  }

  private boolean existRC(HttpEntity<String> reqEntity, String rcURL) throws UnauthorizedException {

    try {
      ResponseEntity<String> rcEntity =
          template.exchange(rcURL, HttpMethod.GET, reqEntity, String.class);
      if (rcEntity.getStatusCode() == HttpStatus.UNAUTHORIZED)
        throw new UnauthorizedException("Invalid Token");
      return true;
    } catch (HttpClientErrorException e) {
      return false;
    }
  }
}
