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

package org.project.openbaton.nubomedia.paas.events;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import org.openbaton.catalogue.mano.record.NetworkServiceRecord;
import org.openbaton.catalogue.mano.record.VirtualNetworkFunctionRecord;
import org.openbaton.catalogue.nfvo.Action;
import org.openbaton.catalogue.nfvo.EndpointType;
import org.openbaton.catalogue.nfvo.EventEndpoint;
import org.openbaton.sdk.NFVORequestor;
import org.openbaton.sdk.api.exception.SDKException;
import org.project.openbaton.nubomedia.paas.core.AppManager;
import org.project.openbaton.nubomedia.paas.model.openbaton.OpenbatonEvent;
import org.project.openbaton.nubomedia.paas.utils.NfvoProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * Created by mob on 23.03.16.
 */
@Service
public class OpenbatonEventReceiver implements CommandLineRunner{
    private static final Logger logger = LoggerFactory.getLogger(OpenbatonEventReceiver.class);

    private Gson mapper = new GsonBuilder().serializeNulls().create();;
    private EventEndpoint eventEndpointCreation, eventEndpointError;
    @Autowired
    private NfvoProperties nfvoProperties;

    @Autowired
    private AppManager appManager;

    private NFVORequestor nfvoRequestor;


    @PreDestroy
    private void close(){
        logger.debug("Destroying ConfigurationBeans, deleting queues");
        try {
            this.nfvoRequestor.getEventAgent().delete(eventEndpointCreation.getId());
        } catch (SDKException e) {
            logger.error("error deleting the nsr creation endpoint");
        }
        try {
            this.nfvoRequestor.getEventAgent().delete(eventEndpointError.getId());
        } catch (SDKException e) {
            logger.error("error deleting the nsr error endpoint");
        }

    }

    public void receiveNewNsr(String message) {
        OpenbatonEvent openbatonEvent;
        try {
            openbatonEvent = getOpenbatonEvent(message);
            logger.debug("Received nfvo event with action: " + openbatonEvent.getAction());
            appManager.startOpenshiftBuild(openbatonEvent, openbatonEvent.getPayload().getId());

        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return;
        }
    }


    public void deleteNsr(String message){
        OpenbatonEvent openbatonEvent;
        try {
            openbatonEvent = getOpenbatonEvent(message);

            logger.debug("Received nfvo event with action: " + openbatonEvent.getAction());

        } catch (Exception e) {
            logger.warn(e.getMessage(),e);
            return;
        }
    }

    private OpenbatonEvent getOpenbatonEvent(String message) throws Exception {
        OpenbatonEvent openbatonEvent;

        try {
            openbatonEvent = mapper.fromJson(message, OpenbatonEvent.class);
        } catch (JsonParseException e) {
            throw new Exception(e.getMessage(),e);
        }
        return openbatonEvent;
    }

    private void createEventCreationEndpoint(){
        eventEndpointCreation = createEventEndpoint("paas-nsr-instantiate-finish", EndpointType.RABBIT,Action.INSTANTIATE_FINISH, ConfigurationBeans.queueName_eventInstatiateFinish);
        try {
            logger.info("creating endpoint creation event");
            eventEndpointCreation = this.nfvoRequestor.getEventAgent().create(eventEndpointCreation);
        } catch (SDKException e) {
            logger.error("problem creating the nsr instantiate finish event endpoint");
        }
    }

    private void createEventErrorEndpoint(){
        eventEndpointError = createEventEndpoint("paas-nsr-error",EndpointType.RABBIT,Action.ERROR, ConfigurationBeans.queueName_error);
        try {
            logger.info("creating endpoint error event");
            eventEndpointError = this.nfvoRequestor.getEventAgent().create(eventEndpointError);
        } catch (SDKException e) {
            logger.error("problem creating the nsr error event endpoint");
        }

    }


    private EventEndpoint createEventEndpoint(String name, EndpointType type, Action action, String url){
        EventEndpoint eventEndpoint = new EventEndpoint();
        eventEndpoint.setEvent(action);
        eventEndpoint.setName(name);
        eventEndpoint.setType(type);
        eventEndpoint.setEndpoint(url);
        return eventEndpoint;
    }


    @Override
    public void run(String... args) throws Exception {
        logger.debug("Creating queues");
        this.nfvoRequestor = new NFVORequestor(nfvoProperties.getOpenbatonUsername(), nfvoProperties.getOpenbatonPasswd(), nfvoProperties.getOpenbatonIP(), nfvoProperties.getOpenbatonPort(), "1");
        this.createEventCreationEndpoint();
        this.createEventErrorEndpoint();
    }
}
