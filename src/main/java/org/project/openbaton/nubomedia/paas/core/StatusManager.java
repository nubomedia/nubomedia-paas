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

import org.project.openbaton.nubomedia.paas.exceptions.openshift.UnauthorizedException;
import org.project.openbaton.nubomedia.paas.messages.AppStatus;
import org.project.openbaton.nubomedia.paas.model.persistence.Application;
import org.project.openbaton.nubomedia.paas.model.persistence.ApplicationRepository;
import org.project.openbaton.nubomedia.paas.utils.OpenshiftProperties;
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
public class StatusManager {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private OpenshiftManager osmanager;
    @Autowired
    private OpenbatonManager obmanager;

    @Autowired
    private ApplicationRepository appRepo;
    @Autowired
    private OpenshiftProperties properties;

    private String token;

    @PostConstruct
    public void init(){
        token = properties.getToken();
    }

    @Scheduled(initialDelay=1000, fixedRate=5000)
    private void refreshApplicationStatus() {
        //BETA
        Iterable<Application> applications = this.appRepo.findAll();
        for (Application app : applications) {
            try {
                app.setStatus(this.getStatus(this.token, app));
            } catch (UnauthorizedException e) {
                logger.error("There were issues in connecting to OpenShift ");
                e.printStackTrace();
            }
        }
        this.appRepo.save(applications);
    }

    private AppStatus getStatus(String token, Application app) throws UnauthorizedException {

        AppStatus res = null;

        logger.debug("application ("+app.getAppID()+"-"+app.getAppName()+") status is "+app.getStatus());

        switch (app.getStatus()) {
            case CREATED:
                res = obmanager.getStatus(app.getNsrID());
                break;
            case INITIALIZING:
                res = obmanager.getStatus(app.getNsrID());
                break;
            case INITIALISED:
                try {
                    res = osmanager.getStatus(token, app.getAppName());
                } catch (ResourceAccessException e) {
                    res = AppStatus.PAAS_RESOURCE_MISSING;
                }
                break;
            case BUILDING:
                try {
                    res = osmanager.getStatus(token, app.getAppName());
                } catch (ResourceAccessException e) {
                    res = AppStatus.PAAS_RESOURCE_MISSING;
                }
                break;
            case DEPLOYNG:
                try {
                    res = osmanager.getStatus(token, app.getAppName());
                } catch (ResourceAccessException e) {
                    res = AppStatus.PAAS_RESOURCE_MISSING;
                }
                break;
            case FAILED:
                logger.debug("FAILED: app has resource ok? " + app.isResourceOK());
                if (!app.isResourceOK()) {
                    res = AppStatus.FAILED;
                    break;
                } else {
                    try {
                        res = osmanager.getStatus(token, app.getAppName());
                    } catch (ResourceAccessException e) {
                        res = AppStatus.PAAS_RESOURCE_MISSING;
                    }
                }
                break;
            case RUNNING:
                try {
                    res = osmanager.getStatus(token, app.getAppName());
                } catch (ResourceAccessException e) {
                    res = AppStatus.PAAS_RESOURCE_MISSING;
                }
                break;
            case PAAS_RESOURCE_MISSING:
                res = osmanager.getStatus(token, app.getAppName());
                break;
        }

        return res;
    }
}
