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

package org.project.openbaton.nubomedia.paas.core;

import com.google.gson.Gson;
import com.openshift.internal.restclient.model.BuildConfig;
import com.openshift.internal.restclient.model.DeploymentConfig;
import com.openshift.internal.restclient.model.Route;
import com.openshift.internal.restclient.model.properties.ResourcePropertiesRegistry;
import com.openshift.restclient.ClientBuilder;
import com.openshift.restclient.IClient;
import com.openshift.restclient.ResourceKind;
import com.openshift.restclient.model.*;
import com.openshift.restclient.model.route.IRoute;
import org.jboss.dmr.ModelNode;
import org.project.openbaton.nubomedia.paas.core.openshift.*;
import org.project.openbaton.nubomedia.paas.core.openshift.builders.MessageBuilderFactory;
import org.project.openbaton.nubomedia.paas.exceptions.BadRequestException;
import org.project.openbaton.nubomedia.paas.exceptions.NotFoundException;
import org.project.openbaton.nubomedia.paas.exceptions.openshift.DuplicatedException;
import org.project.openbaton.nubomedia.paas.exceptions.openshift.UnauthorizedException;
import org.project.openbaton.nubomedia.paas.messages.AppStatus;
import org.project.openbaton.nubomedia.paas.model.openshift.RouteConfig;
import org.project.openbaton.nubomedia.paas.model.openshift.ServiceConfig;
import org.project.openbaton.nubomedia.paas.model.persistence.Application;
import org.project.openbaton.nubomedia.paas.properties.OpenShiftProperties;
import org.project.openbaton.nubomedia.paas.properties.VnfmProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by maa on 27/09/2015.
 */
@Service
public class OpenShiftManager {

  @Autowired private Gson mapper;
  @Autowired private SecretManager secretManager;
  @Autowired private ImageStreamManager imageStreamManager;
  @Autowired private BuildManager buildManager;
  @Autowired private BuildConfigManager buildConfigManager;
  @Autowired private DeploymentManager deploymentManager;
  @Autowired private ServiceManager serviceManager;
  @Autowired private RouteManager routeManager;

  @Autowired private OpenShiftProperties openShiftProperties;

  @Autowired private VnfmProperties vnfmProperties;

  private IClient client;

  private Logger logger = LoggerFactory.getLogger(this.getClass());

  @Value("${openshift.keystore}")
  private String openshiftKeystore;

  @PostConstruct
  private void init() throws IOException {

    System.setProperty("javax.net.ssl.trustStore", openshiftKeystore);
    //
    //    logger.info(openShiftProperties.getToken());
    //    logger.info(openShiftProperties.getBaseURL());
    //    logger.info(openshiftKeystore);

    //    try {
    //      KeyStore ks = KeyStore.getInstance("JKS");
    //      ks.load(new FileInputStream(openshiftKeystore), null);
    //      Enumeration<String> aliases = ks.aliases();
    //      while (aliases.hasMoreElements()) {
    //        logger.info(aliases.nextElement());
    //      }
    //      logger.info(String.valueOf(ks.aliases()));
    //    } catch (KeyStoreException e) {
    //      e.printStackTrace();
    //    } catch (NoSuchAlgorithmException e) {
    //      e.printStackTrace();
    //    }
    //

    client =
        new ClientBuilder(openShiftProperties.getBaseURL())
            .usingToken(openShiftProperties.getToken())
            //              .sslCertificate("nubomedia", null)
            //                              (X509Certificate) javax.security.cert.X509Certificate.getInstance(new FileInputStream(new File(openshiftKeystore))))
            .build();

    IList list = client.get(ResourceKind.SERVICE, openShiftProperties.getProject());
    for (IResource resource : list.getItems()) {
      logger.info(resource.toJson());
    }
    IList list1 = client.get(ResourceKind.BUILD, openShiftProperties.getProject());
    logger.info(list1.toJson());
    for (IResource resource : list.getItems()) {
      logger.trace(resource.toJson());
    }
    IList list2 = client.get(ResourceKind.REPLICATION_CONTROLLER, openShiftProperties.getProject());
    logger.info(list2.toJson());
    for (IResource resource : list.getItems()) {
      logger.trace(resource.toJson());
    }
  }

  //  public String authenticate(String username, String password) throws UnauthorizedException {
  //
  //    return this.authManager.authenticate(openShiftProperties.getBaseURL(), username, password);
  //  }

  public String buildApplication(
      Application application,
      String cloudRepositoryIp,
      String cloudRepositoryPort,
      String cdnServerIp) {

    logger.info(
        "Starting build app "
            + application.getName()
            + " in project "
            + openShiftProperties.getProject());

    IImageStream imageStream = imageStreamManager.crateImageStream(application.getOsName());

    logger.info(imageStream.toJson());

    RouteConfig routeConfig =
        MessageBuilderFactory.getRouteMessage(
            openShiftProperties.getProject(),
            application.getOsName(),
            openShiftProperties.getDomainName());

    buildManager.createBuild(
        application.getOsName(),
        openShiftProperties.getProject(),
        application.getGitURL(),
        imageStream.getDockerImageRepository().getAbsoluteUri(),
        application.getSecretName(),
        application.getMediaServerGroup().getId(),
        vnfmProperties.getIp(),
        vnfmProperties.getPort(),
        cloudRepositoryIp,
        cloudRepositoryPort,
        cdnServerIp,
        routeConfig);

    deploymentManager.makeDeployment(
        application.getOsName(),
        imageStream.getDockerImageRepository().getAbsoluteUri(),
        application.getTargetPorts(),
        application.getProtocols(),
        application.getReplicasNumber());

    //    IService service = client.getResourceFactory().stub(ResourceKind.SERVICE, osName, openShiftProperties.getProject());

    serviceManager.makeService(
        application.getOsName(),
        application.getPorts(),
        application.getTargetPorts(),
        application.getProtocols());

    routeManager.makeRoute(routeConfig);

    //    ResponseEntity<String> appBuilEntity =
    //        imageStreamManager.makeImageStream(
    //            openshiftBaseURL, osName, openShiftProperties.getProject(), creationHeader);
    //    if (!appBuilEntity.getStatusCode().is2xxSuccessful()) {
    //      logger.debug("Failed creation of imagestream " + appBuilEntity.toString());
    //      return appBuilEntity.getBody();
    //    }

    //    ImageStreamConfig isConfig = mapper.fromJson(appBuilEntity.getBody(), ImageStreamConfig.class);
    //

    //    appBuilEntity =
    //            buildManager.createBuild(
    //            openshiftBaseURL,
    //            osName,
    //            openShiftProperties.getProject(),
    //            gitURL,
    //            isConfig.getStatus().getDockerImageRepository(),
    //            creationHeader,
    //            secretName,
    //            mediaServerGID,
    //            vnfmIp,
    //            vnfmPort,
    //            cloudRepositoryIp,
    //            cloudRepositoryPort,
    //            cdnServerIp,
    //            routeConfig);
    //    if (!appBuilEntity.getStatusCode().is2xxSuccessful()) {
    //      logger.debug("Failed creation of buildconfig " + appBuilEntity.toString());
    //      return appBuilEntity.getBody();
    //    }
    //
    //    appBuilEntity =
    //        deploymentManager.makeDeployment(
    //            openshiftBaseURL,
    //            osName,
    //            isConfig.getStatus().getDockerImageRepository(),
    //            targetPorts,
    //            protocols,
    //            replicasnumber,
    //            openShiftProperties.getProject(),
    //            creationHeader);
    //    if (!appBuilEntity.getStatusCode().is2xxSuccessful()) {
    //      logger.debug("Failed creation of deploymentconfig " + appBuilEntity.toString());
    //      return appBuilEntity.getBody();
    //    }
    //
    //    appBuilEntity =
    //        serviceManager.makeService(
    //            kubernetesBaseURL,
    //            osName,
    //            openShiftProperties.getProject(),
    //            ports,
    //            targetPorts,
    //            protocols,
    //            creationHeader);
    //    if (!appBuilEntity.getStatusCode().is2xxSuccessful()) {
    //      logger.debug("Failed creation of service " + appBuilEntity.toString());
    //      return appBuilEntity.getBody();
    //    }
    //
    //    appBuilEntity =
    //        routeManager.makeRoute(
    //            openshiftBaseURL,
    //            osName,
    //            openShiftProperties.getProject(),
    //            creationHeader,
    //            routeConfig);
    //    if (!appBuilEntity.getStatusCode().is2xxSuccessful()) {
    //      logger.debug("Failed creation of route " + appBuilEntity.toString());
    //      return appBuilEntity.getBody();
    //    }
    //    RouteConfig route = mapper.fromJson(appBuilEntity.getBody(), RouteConfig.class);
    //    logger.debug("Created Route " + route.getSpec().getHost());
    //
    return routeConfig.getSpec().getHost();
  }

  public void deleteApplication(String osName) throws UnauthorizedException {

    //    HttpHeaders deleteHeader = new HttpHeaders();
    //    deleteHeader.add("Authorization", "Bearer " + token);

    imageStreamManager.deleteImageStream(osName);

    buildManager.deleteBuilds(osName);

    buildConfigManager.deleteBuildConfig(osName);

    //    HttpStatus res =
    deploymentManager.deleteDeploymentConfig(osName);
    //    if (!res.is2xxSuccessful()) return res;

    deploymentManager.deleteReplicationController(osName);

    //    res =
    deploymentManager.deletePods(osName);
    //    if (!res.is2xxSuccessful() && !res.equals(HttpStatus.NOT_FOUND)) return res;
    //    if (res.equals(HttpStatus.NOT_FOUND))
    //      logger.debug("No Replication controller, build probably failed");

    //    res =
    serviceManager.deleteService(osName);
    //    if (!res.is2xxSuccessful()) return res;

    //    res =
    routeManager.deleteRoute(osName);

    //    return res;
  }

  public String createSecret(String privateKey) {
    //    HttpHeaders authHeader = new HttpHeaders();
    //    authHeader.add("Authorization", "Bearer " + token);
    return secretManager.createSecret(privateKey);
  }

  public void deleteSecret(String secretName) {
    //    HttpHeaders authHeader = new HttpHeaders();
    //    authHeader.add("Authorization", "Bearer " + token);
    secretManager.deleteSecret(secretName);
  }

  public AppStatus getStatus(String osName) throws NotFoundException {
    AppStatus res = AppStatus.INITIALISED;
    //    HttpHeaders authHeader = new HttpHeaders();
    //    authHeader.add("Authorization", "Bearer " + token);

    AppStatus status = buildManager.getApplicationStatus(osName);
    try {
      switch (status) {
        case BUILDING:
          res = AppStatus.BUILDING;
          break;
        case FAILED:
          res = AppStatus.FAILED;
          break;
        case BUILD_OK:
          res = deploymentManager.getDeployStatus(osName);
          break;
        case PAAS_RESOURCE_MISSING:
          res = AppStatus.PAAS_RESOURCE_MISSING;
          break;
      }
    } catch (NullPointerException e) {
      res = AppStatus.BUILDING;
    }
    return res;
  }

  public String getApplicationLog(String osName, String podName) throws UnauthorizedException {
    return deploymentManager.getPodLogs(osName, podName);
  }

  public String getBuildLogs(String osName) throws NotFoundException, UnauthorizedException {
    return buildManager.getBuildLogs(osName);
  }

  public List<String> getPodList(String osName) throws NotFoundException {
    //    HttpHeaders authHeader = new HttpHeaders();
    //    authHeader.add("Authorization", "Bearer " + token);
    //    HttpEntity<String> requestEntity = new HttpEntity<>(authHeader);
    return deploymentManager.getPodNameList(osName);
  }
}
