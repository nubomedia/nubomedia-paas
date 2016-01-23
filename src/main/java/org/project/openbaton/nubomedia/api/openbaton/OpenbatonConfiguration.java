package org.project.openbaton.nubomedia.api.openbaton;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.openbaton.catalogue.mano.common.VNFDeploymentFlavour;
import org.openbaton.catalogue.mano.descriptor.*;
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
import java.util.HashSet;
import java.util.Set;

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

    public static NetworkServiceDescriptor getNSD(String flavor,String Qos){
        NetworkServiceDescriptor nsd = new NetworkServiceDescriptor();
        Gson mapper = new GsonBuilder().create();

        try{
            FileReader nsdFile = new FileReader("/etc/nubomedia/nubomedia-nsd.json");
            nsd = mapper.fromJson(nsdFile,NetworkServiceDescriptor.class);
        }
        catch (FileNotFoundException e){
            logger.debug("DO NOT REMOVE OR RENAME THE FILE /etc/nubomedia/nubomedia-nsd.json!!!!\nexiting");
        }

        nsd = OpenbatonConfiguration.injectFlavor(flavor,nsd);

        if (Qos != null){

            nsd = OpenbatonConfiguration.injectQoS(Qos,nsd);

        }

        return nsd;
    }

    private static NetworkServiceDescriptor injectQoS(String qos, NetworkServiceDescriptor nsd) {

        Set<VirtualNetworkFunctionDescriptor> vnfds = new HashSet<>();

        for (VirtualNetworkFunctionDescriptor vnfd : nsd.getVnfd()){
            Set<InternalVirtualLink> vlds = new HashSet<>();
            for (InternalVirtualLink vld : vnfd.getVirtual_link()) {
                Set<String> qoss = new HashSet<>();
                qoss.add("minimum_bandwith:" + qos);
                vld.setQos(qoss);
                vlds.add(vld);
            }
            vnfd.setVirtual_link(vlds);
            vnfds.add(vnfd);
        }
        nsd.setVnfd(vnfds);
        return nsd;
    }

    private static NetworkServiceDescriptor injectFlavor(String flavour, NetworkServiceDescriptor networkServiceDescriptor){

        Set<VirtualNetworkFunctionDescriptor> vnfds = new HashSet<>();

        for (VirtualNetworkFunctionDescriptor vnfd : networkServiceDescriptor.getVnfd()){
            Set<VNFDeploymentFlavour> flavours = new HashSet<>();
            VNFDeploymentFlavour newFlavour = new VNFDeploymentFlavour();
            newFlavour.setFlavour_key(flavour);
            flavours.add(newFlavour);
            vnfd.setDeployment_flavour(flavours);
            vnfds.add(vnfd);
        }

        networkServiceDescriptor.setVnfd(vnfds);
        return networkServiceDescriptor;
    }

}
