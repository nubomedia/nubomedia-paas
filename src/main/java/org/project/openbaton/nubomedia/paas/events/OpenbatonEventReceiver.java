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
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import org.openbaton.catalogue.mano.record.NetworkServiceRecord;
import org.openbaton.catalogue.mano.record.VirtualNetworkFunctionRecord;
import org.openbaton.catalogue.nfvo.Action;
import org.openbaton.sdk.api.exception.SDKException;
import org.project.openbaton.nubomedia.paas.core.AppManager;
import org.project.openbaton.nubomedia.paas.model.openbaton.OpenbatonEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by mob on 23.03.16.
 */
@Service
public class OpenbatonEventReceiver {
    private static final Logger logger = LoggerFactory.getLogger(OpenbatonEventReceiver.class);

    @Autowired private Gson mapper;

    @Autowired
    private AppManager appManager;

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


}
