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
import com.openshift.internal.restclient.model.BuildConfig;
import com.openshift.restclient.ClientBuilder;
import com.openshift.restclient.IClient;
import com.openshift.restclient.ResourceKind;
import com.openshift.restclient.model.IResource;
import org.project.openbaton.nubomedia.paas.core.openshift.builders.MessageBuilderFactory;
import org.project.openbaton.nubomedia.paas.model.openshift.RouteConfig;
import org.project.openbaton.nubomedia.paas.properties.OpenShiftProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;

/**
 * Created by maa on 08.10.15.
 */
@Service
public class BuildConfigManager {

  @Autowired private RestTemplate template;
  @Autowired private Gson mapper;
  private Logger logger;

  private IClient client;

  @Autowired private OpenShiftProperties openShiftProperties;

  @PostConstruct
  public void init() {
    this.logger = LoggerFactory.getLogger(this.getClass());
    client =
        new ClientBuilder(openShiftProperties.getBaseURL())
            .usingToken(openShiftProperties.getToken())
            .build();
  }

  public BuildConfig createBuildConfig(
      String osName,
      String namespace,
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

    String buildJson =
        mapper.toJson(
            MessageBuilderFactory.getBuilderMessage(
                osName,
                namespace,
                dockerRepo,
                gitURL,
                secretName,
                mediaServerGID,
                mediaServerIP,
                mediaServerPort,
                cloudRepositoryIp,
                cloudRepoPort,
                cdnServerIp,
                routeConfig),
            org.project.openbaton.nubomedia.paas.model.openshift.BuildConfig.class);
    BuildConfig buildConfig = client.getResourceFactory().create(buildJson);

    logger.debug("Creating BuildConfig {}", buildConfig);
    buildConfig = client.create(buildConfig);
    logger.debug("Created BuildConfig {}", buildConfig);
    return buildConfig;
  }

  public void deleteBuildConfig(String osName) {
    logger.debug("Deleting BuildConfig for {}", osName);
    IResource buildConfig =
        client
            .getResourceFactory()
            .stub(ResourceKind.BUILD_CONFIG, osName + "-bc", openShiftProperties.getProject());
    client.delete(buildConfig);
    logger.debug("Deleted BuildConfig for {}", osName);
  }
}
