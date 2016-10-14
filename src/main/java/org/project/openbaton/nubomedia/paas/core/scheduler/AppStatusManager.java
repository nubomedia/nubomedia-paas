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

package org.project.openbaton.nubomedia.paas.core.scheduler;

import org.project.openbaton.nubomedia.paas.core.OpenShiftManager;
import org.project.openbaton.nubomedia.paas.core.OpenbatonManager;
import org.project.openbaton.nubomedia.paas.exceptions.NotFoundException;
import org.project.openbaton.nubomedia.paas.exceptions.openshift.UnauthorizedException;
import org.project.openbaton.nubomedia.paas.messages.AppStatus;
import org.project.openbaton.nubomedia.paas.model.persistence.Application;
import org.project.openbaton.nubomedia.paas.model.persistence.SupportingService;
import org.project.openbaton.nubomedia.paas.properties.OpenShiftProperties;
import org.project.openbaton.nubomedia.paas.repository.application.ApplicationRepository;
import org.project.openbaton.nubomedia.paas.repository.service.ServiceRepository;
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
  @Autowired private ServiceRepository serviceRepository;
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
      app.setStatus(this.getStatus(app));
      logger.trace("Updated Application status: " + app);
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
      logger.trace(
          "Updating Application status of "
              + app.getName()
              + "-"
              + app.getOsName()
              + " while running");
      try {
        app.setStatus(this.getStatus(app));
        logger.trace("Updated Application status: " + app);
        app.setPodList(osmanager.getPodList(app.getOsName()));
      } catch (NotFoundException e) {
        logger.warn("Not found Pod of " + app.getName() + " with name " + app.getOsName());
      }
    }
    this.appRepo.save(applications);
  }

  @Scheduled(initialDelay = 10000, fixedRate = 180000)
  private void refreshServiceStatus() {
    // /BETA
    logger.trace("Refreshing Application (running) status");
    Iterable<SupportingService> services = this.serviceRepository.findAll();
    for (SupportingService service : services) {
      logger.trace(
          "Updating Service status of "
              + service.getName()
              + "-"
              + service.getOsName()
              + " while running");
      try {
        service.setStatus(this.getServiceStatus(service));
        logger.trace("Updated Application status: " + service);
        service.setPodList(osmanager.getPodList(service.getOsName()));
      } catch (NotFoundException e) {
        logger.warn("Not found Pod of " + service.getName() + " with name " + service.getOsName());
      }
    }
    this.serviceRepository.save(services);
  }

  private AppStatus getStatus(Application app) {
    AppStatus res = AppStatus.PAAS_RESOURCE_MISSING;
    logger.debug(
        "application ("
            + app.getId()
            + "-"
            + app.getName()
            + "-"
            + app.getOsName()
            + ") status is "
            + app.getStatus());

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
            res = osmanager.getStatus(app.getOsName());
          } catch (ResourceAccessException e) {
            res = AppStatus.PAAS_RESOURCE_MISSING;
          } catch (NotFoundException e) {
            logger.warn("Not found Pod of " + app.getName() + " with name " + app.getOsName());
          }
        }
        break;
      default:
        try {
          res = osmanager.getStatus(app.getOsName());
        } catch (ResourceAccessException e) {
          res = AppStatus.PAAS_RESOURCE_MISSING;
        } catch (NotFoundException e) {
          logger.warn("Not found Pod of " + app.getName() + " with name " + app.getOsName());
        }
        break;
    }

    return res;
  }

  private String getServiceStatus(SupportingService service) {
    String res = AppStatus.PAAS_RESOURCE_MISSING.toString();
    logger.debug(
        "application ("
            + service.getId()
            + "-"
            + service.getName()
            + "-"
            + service.getOsName()
            + ") status is "
            + service.getStatus());

    try {
      res = osmanager.getOsStatus(service.getOsName());
    } catch (ResourceAccessException e) {
      res = AppStatus.PAAS_RESOURCE_MISSING.toString();
    } catch (NotFoundException e) {
      logger.warn("Not found Pod of " + service.getName() + " with name " + service.getOsName());
      res = AppStatus.PAAS_RESOURCE_MISSING.toString();
    }
    return res;
  }

  private void refreshMediaServerGroup(Application app) throws Exception {
    obmanager.updateMediaServerGroup(app);
  }
}
