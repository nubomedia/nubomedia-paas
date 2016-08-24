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

package org.project.openbaton.nubomedia.paas.core.scheduler;

import org.project.openbaton.nubomedia.paas.core.OpenShiftManager;
import org.project.openbaton.nubomedia.paas.core.OpenbatonManager;
import org.project.openbaton.nubomedia.paas.exceptions.openshift.UnauthorizedException;
import org.project.openbaton.nubomedia.paas.messages.AppStatus;
import org.project.openbaton.nubomedia.paas.model.persistence.Application;
import org.project.openbaton.nubomedia.paas.properties.OpenShiftProperties;
import org.project.openbaton.nubomedia.paas.repository.application.ApplicationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;

import javax.annotation.PostConstruct;

/**
 * Created by gca on 29/05/16.
 */
@Service
@Scope
public class AppStatusManager {
  private Logger logger = LoggerFactory.getLogger(this.getClass());

  @Autowired private OpenShiftManager osmanager;
  @Autowired private OpenbatonManager obmanager;

  @Autowired private ApplicationRepository appRepo;
  @Autowired private OpenShiftProperties properties;

  private String token;

  @PostConstruct
  public void init() {
    token = properties.getToken();
  }

  @Scheduled(initialDelay = 1000, fixedRate = 5000)
  private void refreshBuildingApplicationStatus() {
    //BETA
    logger.trace("Refreshing Application (building) status");
    Iterable<Application> applications = this.appRepo.findAll();
    for (Application app : applications) {
      if (!app.getStatus().equals(AppStatus.RUNNING))
        logger.trace("Updating Application status of " + app.getName() + " while building");
      try {
        app.setStatus(this.getStatus(this.token, app));
        logger.trace("Updated Application status: " + app);
      } catch (UnauthorizedException e) {
        logger.error("There were issues in connecting to OpenShift ");
        e.printStackTrace();
      }
      try {
        this.refreshMediaServerGroup(app);
      } catch (Exception e) {
        logger.error(e.getMessage());
        e.printStackTrace();
      }
    }
    this.appRepo.save(applications);
  }

  @Scheduled(initialDelay = 10000, fixedRate = 180000)
  private void refreshApplicationStatus() {
    // /BETA
    logger.trace("Refreshing Application (running) status");
    Iterable<Application> applications = this.appRepo.findAll();
    for (Application app : applications) {
      logger.trace("Updating Application status of " + app.getName() + " while running");
      try {
        app.setStatus(this.getStatus(this.token, app));
        logger.trace("Updated Application status: " + app);
        app.setPodList(osmanager.getPodList(this.token, app.getName()));
      } catch (UnauthorizedException e) {
        logger.error("There were issues in connecting to OpenShift ");
        e.printStackTrace();
      }
    }
    this.appRepo.save(applications);
  }

  private AppStatus getStatus(String token, Application app) throws UnauthorizedException {
    AppStatus res;
    logger.debug(
        "application (" + app.getId() + "-" + app.getName() + ") status is " + app.getStatus());

    switch (app.getStatus()) {
      case CREATED:
        res = obmanager.getStatus(app.getMediaServerGroup().getNsrID());
        break;
      case INITIALIZING:
        res = obmanager.getStatus(app.getMediaServerGroup().getNsrID());
        break;
      case FAILED:
        logger.debug("FAILED: app has resource ok? " + app.isResourceOK());
        if (!app.isResourceOK()) {
          res = AppStatus.FAILED;
          break;
        } else {
          try {
            res = osmanager.getStatus(token, app.getName());
          } catch (ResourceAccessException e) {
            res = AppStatus.PAAS_RESOURCE_MISSING;
          }
        }
        break;
      default:
        try {
          res = osmanager.getStatus(token, app.getName());
        } catch (ResourceAccessException e) {
          res = AppStatus.PAAS_RESOURCE_MISSING;
        }
        break;
    }

    return res;
  }

  private void refreshMediaServerGroup(Application app) throws Exception {
    obmanager.updateMediaServerGroup(app);
  }
}
