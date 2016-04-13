package org.project.openbaton.nubomedia.api.openbaton;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.openbaton.catalogue.mano.descriptor.NetworkServiceDescriptor;
import org.openbaton.catalogue.mano.descriptor.VirtualNetworkFunctionDescriptor;
import org.openbaton.catalogue.nfvo.Location;
import org.openbaton.catalogue.nfvo.VimInstance;
import org.project.openbaton.nubomedia.api.configuration.VimProperties;
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
@ComponentScan ("org.project.openbaton.nubomedia.api")
public class OpenbatonConfiguration {

    @Autowired private VimProperties vimProperties;
    private static Logger logger = LoggerFactory.getLogger(OpenbatonConfiguration.class);

    @Bean
    public VimInstance getVimInstance(){
        VimInstance vim = new VimInstance();
        logger.debug("Creating vim");
        vim.setName("nubomedia-vim");
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
        logger.debug("Sending VIM " + vim.toString());

        return vim;
    }

    @Bean
    public VirtualNetworkFunctionDescriptor getCloudRepository(){
        VirtualNetworkFunctionDescriptor vnfd = null;
        Gson mapper = new GsonBuilder().create();

        try{
            logger.debug("Reading cloud repository");
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
    public NetworkServiceDescriptor networkServiceDescriptor(){
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
            e.printStackTrace();
            logger.debug("DO NOT REMOVE OR RENAME THE FILE /etc/nubomedia/nubomedia-nsd.json!!!!\nexiting");
        }
        return nsd;
    }

}
