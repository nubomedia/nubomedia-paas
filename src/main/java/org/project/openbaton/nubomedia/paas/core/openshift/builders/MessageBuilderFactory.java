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

package org.project.openbaton.nubomedia.paas.core.openshift.builders;

import org.project.openbaton.nubomedia.paas.model.openshift.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by maa on 26/09/2015.
 */
public class MessageBuilderFactory {

  public static ImageStreamConfig getImageStreamMessage(String osName) {
    ImageStreamMessageBuilder ism = new ImageStreamMessageBuilder(osName);
    return ism.buildMessage();
  }

  public static BuildConfig getBuilderMessage(
      String osName,
      String dockerRepo,
      String gitURL,
      String secretName,
      String mediaServerGID,
      String mediaServerIP,
      String mediaServerPort,
      String cloudRepositoryIp,
      String cloudRepoPort,
      String cdnServerIp,
      RouteConfig routeConfig) {
    DockerBuildStrategy.DockerStrategy ds =
        new DockerBuildStrategy.DockerStrategy(
            new EnviromentVariable[] {
              new EnviromentVariable("BUILD_LOGLEVEL", "5"),
              new EnviromentVariable("VNFR_ID", mediaServerGID),
              new EnviromentVariable("VNFM_IP", mediaServerIP),
              new EnviromentVariable("VNFM_PORT", mediaServerPort),
              new EnviromentVariable("CLOUDREPO_IP", cloudRepositoryIp),
              new EnviromentVariable("CLOUDREPO_PORT", cloudRepoPort),
              new EnviromentVariable("CDN_SERVER_IP", cdnServerIp),
              new EnviromentVariable("ROUTE", routeConfig.getSpec().getHost())
            },
            null);
    DockerBuildStrategy strategy = new DockerBuildStrategy(ds);
    Source.SourceSecret secret;

    if (secretName == null) {
      secret = null;
    } else {
      secret = new Source.SourceSecret(secretName);
    }

    ConfigChangeTrigger trigger = new ConfigChangeTrigger("ConfigChange");
    BuildMessageBuilder builder =
        new BuildMessageBuilder(
            osName,
            strategy,
            new BuildElements("DockerImage", dockerRepo + ":latest"),
            gitURL,
            new ConfigChangeTrigger[] {trigger},
            secret);

    return builder.buildMessage();
  }

  public static DeploymentConfig getDeployMessage(
      String osName, String dockerRepo, int[] ports, String[] protocols, int replicasNumber) {
    Container.Port[] cports = new Container.Port[ports.length];
    for (int i = 0; i < ports.length; i++) {
      cports[i] = new Container.Port(protocols[i], ports[i]);
    }
    List<ContainerVolume> volumes = new ArrayList<>();
    ContainerVolume sharedMemory = new ContainerVolume("dshm", false, "/dev/shm");
    volumes.add(sharedMemory);
    Container container = new Container(osName + "-cnt", dockerRepo + ":latest", cports);
    ImageChangeTrigger.ImageChangeParams params =
        new ImageChangeTrigger.ImageChangeParams(
            true, new BuildElements("ImageStream", osName), new String[] {container.getName()});
    ImageChangeTrigger trigger = new ImageChangeTrigger("ImageChange", params);
    DeploymentMessageBuilder builder =
        new DeploymentMessageBuilder(
            osName,
            new Container[] {container},
            replicasNumber,
            new ImageChangeTrigger[] {trigger},
            "Rolling");

    return builder.buildMessage();
  }

  public static ProjectRequest getProjectRequest(String name) {
    ProjectRequestBuilder builder =
        new ProjectRequestBuilder(name, "This is the project of " + name);
    return builder.buildMessage();
  }

  public static ServiceConfig getServiceMessage(
      String osName, int[] ports, int[] targetPorts, String[] protocols) {
    ServiceMessageBuilder smb = new ServiceMessageBuilder(osName, protocols, ports, targetPorts);
    return smb.buildMessage();
  }

  public static RouteConfig getRouteMessage(String osName, String domainName) {

    RouteTls tls = new RouteTls("passthrough", null, null, null, null);
    RouteMessageBuilder rmb = new RouteMessageBuilder(osName, domainName, tls);
    return rmb.buildMessage();
  }

  public static SecretConfig getSecretMessage(String namespace, String privateKey) {
    SecretKeyMessageBuilder secretKeyMessageBuilder =
        new SecretKeyMessageBuilder(namespace, privateKey);
    return secretKeyMessageBuilder.buildMessage();
  }

  public static HorizontalPodAutoscaler getHpa(String osName, int replicasNumber, int targetPerc) {

    HorizontalPodAutoscalerMessageBuilder builder =
        new HorizontalPodAutoscalerMessageBuilder(osName, 1, replicasNumber, targetPerc);
    return builder.buildMessage();
  }
}
