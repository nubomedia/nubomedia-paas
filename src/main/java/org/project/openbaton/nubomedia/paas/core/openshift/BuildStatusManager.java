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
import com.openshift.internal.restclient.model.Build;
import com.openshift.internal.restclient.model.BuildConfig;
import com.openshift.internal.restclient.model.KubernetesResource;
import com.openshift.internal.restclient.model.build.BuildRequest;
import com.openshift.internal.restclient.model.build.BuildStatus;
import com.openshift.restclient.ClientBuilder;
import com.openshift.restclient.IClient;
import com.openshift.restclient.NotFoundException;
import com.openshift.restclient.ResourceKind;
import com.openshift.restclient.model.IList;
import com.openshift.restclient.model.IResource;
import com.openshift.restclient.model.build.IBuildStatus;
import org.project.openbaton.nubomedia.paas.exceptions.openshift.UnauthorizedException;
import org.project.openbaton.nubomedia.paas.messages.AppStatus;
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
import java.util.Collection;

/**
 * Created by maa on 08.10.15.
 */
@Service
public class BuildStatusManager {

  @Autowired private BuildManager buildManager;

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

  //Responses: Complete, Running, Failed
  public AppStatus getBuildStatus(String osName)
      throws org.project.openbaton.nubomedia.paas.exceptions.NotFoundException {

    AppStatus res = null;
    IBuildStatus status = buildManager.retrieveBuild(osName).getBuildStatus();
    if (status != null) {
      switch (status.getPhase()) {
        case "Running":
          res = AppStatus.BUILDING;
          break;
        case "Failed":
          res = AppStatus.FAILED;
          break;
        case "Complete":
          res = AppStatus.BUILD_OK;
          break;
      }
    } else {
      res = AppStatus.PAAS_RESOURCE_MISSING;
    }

    return res;
  }

  public void deleteBuild(String osName) {

    //        Build target = this.retrieveBuild(baseURL, osName, namespace, authHeader);
    //        String URL = baseURL + namespace + suffix + target.getMetadata().getName();
    //        HttpEntity<String> deleteEntity = new HttpEntity<>(authHeader);
    //        logger.debug("Deleting " + osName + " builds of project " + namespace);
    //        ResponseEntity<String> responseEntity =
    //                template.exchange(URL, HttpMethod.DELETE, deleteEntity, String.class);
    //
    //        if (responseEntity.getStatusCode() != HttpStatus.OK) logger.debug(responseEntity.toString());
    //
    //        if (responseEntity.getStatusCode().equals(HttpStatus.UNAUTHORIZED)) {
    //
    //            throw new UnauthorizedException("Invalid or expired token");
    //        }

    //        return responseEntity.getStatusCode();
  }
}
