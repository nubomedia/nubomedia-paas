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
import org.project.openbaton.nubomedia.paas.messages.AppStatus;
import org.project.openbaton.nubomedia.paas.model.openshift.DeploymentConfig;
import org.project.openbaton.nubomedia.paas.model.openshift.HorizontalPodAutoscaler;
import org.project.openbaton.nubomedia.paas.model.openshift.Pod;
import org.project.openbaton.nubomedia.paas.model.openshift.Pods;
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
import java.util.List;

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

  @PostConstruct
  private void init() {
    this.logger = LoggerFactory.getLogger(this.getClass());
    this.suffix = "/deploymentconfigs/";
    this.podSuffix = "/pods/";
    this.rcSuffix = "/replicationcontrollers/";
  }

  public ResponseEntity<String> makeDeployment(
      String baseURL,
      String appName,
      String dockerRepo,
      int[] ports,
      String[] protocols,
      int repnumbers,
      String namespace,
      HttpHeaders authHeader)
      throws DuplicatedException, UnauthorizedException {

    logger.debug(
        "params arg: "
            + appName
            + " "
            + dockerRepo
            + " "
            + ports
            + " "
            + protocols
            + " "
            + repnumbers);
    DeploymentConfig message =
        MessageBuilderFactory.getDeployMessage(appName, dockerRepo, ports, protocols, repnumbers);
    logger.debug(mapper.toJson(message, DeploymentConfig.class));
    String URL = baseURL + namespace + suffix;
    HttpEntity<String> deployEntity =
        new HttpEntity<>(mapper.toJson(message, DeploymentConfig.class), authHeader);
    ResponseEntity<String> response =
        template.exchange(URL, HttpMethod.POST, deployEntity, String.class);
    logger.debug("Deployment response: " + response);

    if (response.getStatusCode().equals(HttpStatus.CONFLICT)) {
      throw new DuplicatedException("Application with " + appName + " is already present");
    }

    if (response.getStatusCode().equals(HttpStatus.UNAUTHORIZED)) {

      throw new UnauthorizedException("Invalid or expired token");
    }

    return response;
  }

  public HttpStatus deleteDeployment(
      String baseURL, String appName, String namespace, HttpHeaders authHeader)
      throws UnauthorizedException {

    logger.debug("Deleting deployment config " + appName + " of project " + namespace);
    String URL = baseURL + namespace + suffix + appName + "-dc";
    HttpEntity<String> deleteEntity = new HttpEntity<>(authHeader);
    ResponseEntity<String> res =
        template.exchange(URL, HttpMethod.DELETE, deleteEntity, String.class);

    if (res.getStatusCode() != HttpStatus.OK) {
      logger.debug(res.toString());
    }

    if (res.getStatusCode().equals(HttpStatus.UNAUTHORIZED)) {

      throw new UnauthorizedException("Invalid or expired token");
    }

    return res.getStatusCode();
  }

  public HttpStatus deletePodsRC(
      String kubernetesBaseURL, String appName, String namespace, HttpHeaders authHeader)
      throws UnauthorizedException {

    HttpEntity<String> requestEntity = new HttpEntity<>(authHeader);
    String rcURL = kubernetesBaseURL + namespace + rcSuffix + appName + "-dc-1";

    if (!this.existRC(requestEntity, rcURL)) {
      return HttpStatus.OK;
    }

    logger.debug("deleting replication controller for " + appName + " of project " + namespace);
    ResponseEntity<String> deleteEntity =
        template.exchange(rcURL, HttpMethod.DELETE, requestEntity, String.class);

    if (deleteEntity.getStatusCode() == HttpStatus.OK) {
      String podsURL = kubernetesBaseURL + namespace + podSuffix;
      Pods pods = this.getPodsList(podsURL, requestEntity);
      for (String pod : pods.getPodNames()) {

        if (pod.contains(appName)) {
          deleteEntity =
              template.exchange(podsURL + pod, HttpMethod.DELETE, requestEntity, String.class);
          if (deleteEntity.getStatusCode() != HttpStatus.OK) {
            logger.debug(
                "Error HTTP:"
                    + deleteEntity.getStatusCode()
                    + " for POD "
                    + pod
                    + " response "
                    + deleteEntity.toString());
            break;
          }
        }
      }
    } else if (deleteEntity
        .getStatusCode()
        .equals(
            HttpStatus
                .NOT_FOUND)) { //means that you are deleting an application before the build was complete
      logger.debug("Status code " + deleteEntity.getStatusCode());
      return HttpStatus.OK;
    }

    if (deleteEntity.getStatusCode().equals(HttpStatus.UNAUTHORIZED)) {

      throw new UnauthorizedException("Invalid or expired token");
    }

    return deleteEntity.getStatusCode();
  }

  public AppStatus getDeployStatus(
      String kubernetesBaseURL, String appName, String namespace, HttpHeaders authHeader) {

    AppStatus res =
        AppStatus
            .RUNNING; //if deploy pod will not exist is because the application is already deployed or the build is already failed :P
    String podsURL = kubernetesBaseURL + namespace + podSuffix;
    HttpEntity<String> podEntity = new HttpEntity<>(authHeader);
    Pods podList = this.getPodsList(podsURL, podEntity);

    for (String podName : podList.getPodNames()) {
      if (podName.equals(appName + "-dc-1-deploy")) {
        ResponseEntity<String> deployPod =
            template.exchange(podsURL + podName, HttpMethod.GET, podEntity, String.class);
        Pod targetPod = mapper.fromJson(deployPod.getBody(), Pod.class);

        switch (targetPod.getStatus().getPhase()) {
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

  public List<String> getPodNameList(
      String kubernatesBaseURL, String namespace, String appName, HttpEntity<String> requestEntity)
      throws UnauthorizedException {
    String podsURL = kubernatesBaseURL + namespace + podSuffix;
    Pods podList = this.getPodsList(podsURL, requestEntity);
    logger.debug("POD LIST is " + podList.toString());
    List<String> res = new ArrayList<>();

    for (String podName : podList.getPodNames()) {
      logger.debug("Current pod is " + podName);
      CharSequence sequence = appName + "-dc-1";
      if (podName.contains(sequence)) {
        logger.debug("Probably found target " + podName);
        if (!podName.contains("bc-1-build") || !podName.contains("-deploy")) {
          logger.debug("Find compatible pod with name " + podName);
          res.add(podName);
        }
      }
    }

    logger.debug("RES is " + res.toString());
    return res;
  }

  public String getPodLogs(
      String kubernetesBaseURL,
      String namespace,
      String appName,
      String podName,
      HttpEntity<String> requestEntity)
      throws UnauthorizedException {
    String podsURL = kubernetesBaseURL + namespace + podSuffix;
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
      return "No log available for the application " + appName;
    } catch (HttpServerErrorException e) {
      return "Pod(s) crashed for too long time, logs are not anymore available";
    }

    if (!logEntity.getStatusCode().is2xxSuccessful())
      logger.debug("FAILED TO RETRIEVE LOGS " + logEntity.getBody());

    if (logEntity.getStatusCode().equals(HttpStatus.UNAUTHORIZED)) {

      throw new UnauthorizedException("Invalid or expired token");
    }

    logger.debug("LOG IS " + logEntity.getBody());
    return logEntity.getBody();
  }

  public HorizontalPodAutoscaler createHpa(
      String paasUrl,
      String appName,
      String namespace,
      int replicasNumber,
      int targetPerc,
      HttpHeaders authHeader)
      throws UnauthorizedException, DuplicatedException {

    String url =
        paasUrl + "/apis/extensions/v1beta1/namespaces/" + namespace + "/horizontalpodautoscalers";
    HorizontalPodAutoscaler body =
        MessageBuilderFactory.getHpa(appName, replicasNumber, targetPerc);
    HttpEntity<String> hpaEntity =
        new HttpEntity<>(mapper.toJson(body, HorizontalPodAutoscaler.class), authHeader);
    ResponseEntity<String> createHpa =
        template.exchange(url, HttpMethod.POST, hpaEntity, String.class);

    if (createHpa.getStatusCode().equals(HttpStatus.CONFLICT)) {
      throw new DuplicatedException("Application with " + appName + " is already present");
    }

    if (createHpa.getStatusCode().equals(HttpStatus.UNAUTHORIZED)) {

      throw new UnauthorizedException("Invalid or expired token");
    }

    return mapper.fromJson(createHpa.getBody(), HorizontalPodAutoscaler.class);
  }

  private Pods getPodsList(String podsURL, HttpEntity<String> requestEntity) {

    ResponseEntity<String> pods =
        template.exchange(podsURL, HttpMethod.GET, requestEntity, String.class);
    logger.debug(pods.getBody());
    return mapper.fromJson(pods.getBody(), Pods.class);
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
