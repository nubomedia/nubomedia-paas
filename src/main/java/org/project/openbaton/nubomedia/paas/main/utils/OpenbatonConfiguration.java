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

package org.project.openbaton.nubomedia.paas.main.utils;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.openbaton.catalogue.mano.descriptor.NetworkServiceDescriptor;
import org.openbaton.catalogue.mano.descriptor.VirtualNetworkFunctionDescriptor;
import org.openbaton.catalogue.nfvo.Location;
import org.openbaton.catalogue.nfvo.VimInstance;
import org.project.openbaton.nubomedia.paas.utils.VimProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.io.FileNotFoundException;
import java.io.FileReader;

/**
 * Created by maa on 13.10.15.
 */
@Configuration
@ComponentScan ("org.project.openbaton.nubomedia.paas")
public class OpenbatonConfiguration {

    @Autowired private VimProperties vimProperties;
    private static Logger logger = LoggerFactory.getLogger(OpenbatonConfiguration.class);

    @Bean
    public VimInstance getVimInstance(){
        VimInstance vim = new VimInstance();
        logger.debug("Creating VIM Object "+vimProperties.getName());
        vim.setName(vimProperties.getName());
        vim.setAuthUrl(vimProperties.getAuthURL());
        vim.setKeyPair(vimProperties.getKeypair());
        vim.setPassword(vimProperties.getPassword());
        vim.setTenant(vimProperties.getTenant());
        vim.setUsername(vimProperties.getUsername());
        vim.setType(vimProperties.getType());
        Location location = new Location();
        location.setName(vimProperties.getLocationName());
        location.setLatitude(vimProperties.getLocationLatitude());
        location.setLongitude(vimProperties.getLocationLongitude());
        vim.setLocation(location);
        logger.debug("VIM Object generated " + vim);
        return vim;
    }

    @Bean
    public VirtualNetworkFunctionDescriptor getCloudRepository(){
        VirtualNetworkFunctionDescriptor vnfd = null;
        Gson mapper = new GsonBuilder().create();
        try{
            logger.debug("Reading cloud repository descriptor");
            FileReader vnfdFile = new FileReader("/etc/nubomedia/cloudrepo-vnfd.json");
            vnfd = mapper.fromJson(vnfdFile,VirtualNetworkFunctionDescriptor.class);
            logger.debug("CLOUD REPOSITORY IS " + vnfd.toString());

        }
        catch (FileNotFoundException e){
            logger.debug("DO NOT REMOVE OR RENAME THE FILE /etc/nubomedia/cloudrepo-vnfd.json!!!!\nexiting");
        }
        return vnfd;
    }

    @Bean
    public NetworkServiceDescriptor networkServiceDescriptorNubo(){
        logger.debug("Reading descriptor");
        NetworkServiceDescriptor nsd = new NetworkServiceDescriptor();
        Gson mapper = new GsonBuilder().create();
        try{
            logger.debug("Trying to read the descriptor");
            FileReader nsdFile = new FileReader("/etc/nubomedia/nubomedia-nsd.json");
            nsd = mapper.fromJson(nsdFile,NetworkServiceDescriptor.class);
            logger.debug("DESCRIPTOR " + nsd.toString());
        }
        catch (FileNotFoundException e){
            logger.error("DO NOT REMOVE OR RENAME THE FILE /etc/nubomedia/nubomedia-nsd.json!!!!\nexiting", e);
        }
        return nsd;
    }

}
