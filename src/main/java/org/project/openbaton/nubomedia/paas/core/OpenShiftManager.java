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
import com.openshift.restclient.model.IImageStream;
import org.project.openbaton.nubomedia.paas.core.openshift.*;
import org.project.openbaton.nubomedia.paas.core.openshift.builders.MessageBuilderFactory;
import org.project.openbaton.nubomedia.paas.exceptions.NotFoundException;
import org.project.openbaton.nubomedia.paas.exceptions.openshift.UnauthorizedException;
import org.project.openbaton.nubomedia.paas.messages.AppStatus;
import org.project.openbaton.nubomedia.paas.model.openshift.RouteConfig;
import org.project.openbaton.nubomedia.paas.model.persistence.Application;
import org.project.openbaton.nubomedia.paas.properties.OpenShiftProperties;
import org.project.openbaton.nubomedia.paas.properties.VnfmProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.List;

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

  private Logger logger = LoggerFactory.getLogger(this.getClass());

  @Value("${openshift.keystore}")
  private String openshiftKeystore;

  @PostConstruct
  private void init() throws IOException {

    System.setProperty("javax.net.ssl.trustStore", openshiftKeystore);
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

    //    client =
    //        new ClientBuilder(openShiftProperties.getBaseURL())
    //            .usingToken(openShiftProperties.getToken())
    //            //              .sslCertificate("nubomedia", null)
    //            //                              (X509Certificate) javax.security.cert.X509Certificate.getInstance(new FileInputStream(new File(openshiftKeystore))))
    //            .build();
  }

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

    serviceManager.makeService(
        application.getOsName(),
        application.getPorts(),
        application.getTargetPorts(),
        application.getProtocols());

    routeManager.makeRoute(routeConfig);

    return routeConfig.getSpec().getHost();
  }

  public void deleteApplication(String osName) throws UnauthorizedException {
    imageStreamManager.deleteImageStream(osName);

    buildManager.deleteBuilds(osName);

    buildConfigManager.deleteBuildConfig(osName);

    deploymentManager.deleteDeploymentConfig(osName);

    deploymentManager.deleteReplicationController(osName);

    deploymentManager.deletePods(osName);

    serviceManager.deleteService(osName);

    routeManager.deleteRoute(osName);
  }

  public String createSecret(String privateKey) {
    return secretManager.createSecret(privateKey);
  }

  public void deleteSecret(String secretName) {
    secretManager.deleteSecret(secretName);
  }

  public AppStatus getStatus(String osName) throws NotFoundException {
    AppStatus res = AppStatus.INITIALISED;
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
    return deploymentManager.getPodNameList(osName);
  }
}
