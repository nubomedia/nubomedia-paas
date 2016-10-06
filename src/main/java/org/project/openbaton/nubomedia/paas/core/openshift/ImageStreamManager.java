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

import com.openshift.restclient.ClientBuilder;
import com.openshift.restclient.IClient;
import com.openshift.restclient.ResourceKind;
import com.openshift.restclient.model.IImageStream;
import com.openshift.restclient.model.IResource;
import org.project.openbaton.nubomedia.paas.exceptions.openshift.UnauthorizedException;
import org.project.openbaton.nubomedia.paas.properties.OpenShiftProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * Created by maa on 08.10.15.
 */
@Service
public class ImageStreamManager {

  private Logger logger;
  private String suffix;

  private IClient client;

  @Autowired private OpenShiftProperties openShiftProperties;

  @PostConstruct
  private void init() {
    this.logger = LoggerFactory.getLogger(this.getClass());
    this.suffix = "/imagestreams/";
    client =
        new ClientBuilder(openShiftProperties.getBaseURL())
            .usingToken(openShiftProperties.getToken())
            .build();
  }

  public IImageStream crateImageStream(String osName) {
    IImageStream is =
        client
            .getResourceFactory()
            .stub(ResourceKind.IMAGE_STREAM, osName, openShiftProperties.getProject());
    logger.debug("Creating imagestream {}", is);
    is = client.create(is);
    logger.debug("Generated imagestream {}", is);
    return is;
  }

  public void deleteImageStream(String osName) throws UnauthorizedException {
    logger.debug("Deleting ImageStream for {}", osName);
    IResource imageStream =
        client
            .getResourceFactory()
            .stub(ResourceKind.IMAGE_STREAM, osName, openShiftProperties.getProject());
    client.delete(imageStream);
    logger.debug("Deleted ImageStream for {}", osName);
  }
}
