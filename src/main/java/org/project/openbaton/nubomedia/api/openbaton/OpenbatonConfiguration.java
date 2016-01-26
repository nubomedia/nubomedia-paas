package org.project.openbaton.nubomedia.api.openbaton;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.openbaton.catalogue.mano.common.AutoScalePolicy;
import org.openbaton.catalogue.mano.common.ScalingAlarm;
import org.openbaton.catalogue.mano.common.VNFDeploymentFlavour;
import org.openbaton.catalogue.mano.descriptor.InternalVirtualLink;
import org.openbaton.catalogue.mano.descriptor.NetworkServiceDescriptor;
import org.openbaton.catalogue.mano.descriptor.VirtualNetworkFunctionDescriptor;
import org.openbaton.catalogue.nfvo.ConfigurationParameter;
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

    @Bean
    public VirtualNetworkFunctionDescriptor getCloudRepository(){
        VirtualNetworkFunctionDescriptor vnfd = null;
        Gson mapper = new GsonBuilder().create();

        try{
            FileReader vnfdFile = new FileReader("/etc/nubomedia/cloudrepo-vnfd.json");
            vnfd = mapper.fromJson(vnfdFile,VirtualNetworkFunctionDescriptor.class);

        }
        catch (FileNotFoundException e){
            logger.debug("DO NOT REMOVE OR RENAME THE FILE /etc/nubomedia/cloudrepo-vnfd.json!!!!\nexiting");
        }

        return vnfd;
    }

    public static NetworkServiceDescriptor getNSD(String flavor, String Qos, String mediaServerTurnIP, String mediaServerTurnUsername, String mediaServerTurnPassword, double scale_in_threshold, double scale_out_threshold){
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

        if (mediaServerTurnIP != null){

            if (mediaServerTurnUsername == null || mediaServerTurnPassword == null){
                throw new IllegalArgumentException("No Authentication for Turn Server");
            }

            nsd = OpenbatonConfiguration.setStunServer(mediaServerTurnIP,mediaServerTurnUsername, mediaServerTurnPassword, nsd);
        }

        if (scale_in_threshold > 0){
            nsd = OpenbatonConfiguration.setScaleInThreshold(scale_in_threshold,nsd);
        }

        if (scale_out_threshold > 0){
            nsd = OpenbatonConfiguration.setScaleOutThreshold(scale_out_threshold,nsd);
        }

        return nsd;
    }

    private static NetworkServiceDescriptor setScaleOutThreshold(double scale_out_threshold, NetworkServiceDescriptor nsd) {
        Set<VirtualNetworkFunctionDescriptor> vnfds = new HashSet<>();

        for (VirtualNetworkFunctionDescriptor vnfd : nsd.getVnfd()){
            Set<AutoScalePolicy> autoScalePolicy = vnfd.getAuto_scale_policy();

            for (AutoScalePolicy policy : autoScalePolicy){
                if (policy.getName().equals("scale-out")){
                    for (ScalingAlarm alarm : policy.getAlarms()){
                        if (alarm.getMetric().equals("CONSUMED_CAPACITY")){
                            alarm.setThreshold(scale_out_threshold);
                        }
                    }
                }
            }
            vnfd.setAuto_scale_policy(autoScalePolicy);
            vnfds.add(vnfd);
        }
        nsd.setVnfd(vnfds);

        return nsd;
    }

    private static NetworkServiceDescriptor setScaleInThreshold(double scale_in_threshold, NetworkServiceDescriptor nsd) {

        Set<VirtualNetworkFunctionDescriptor> vnfds = new HashSet<>();

        for (VirtualNetworkFunctionDescriptor vnfd : nsd.getVnfd()){
            Set<AutoScalePolicy> autoScalePolicy = vnfd.getAuto_scale_policy();

            for (AutoScalePolicy policy : autoScalePolicy){
                if (policy.getName().equals("scale-in")){
                    for (ScalingAlarm alarm : policy.getAlarms()){
                        if (alarm.getMetric().equals("CONSUMED_CAPACITY")){
                            alarm.setThreshold(scale_in_threshold);
                        }
                    }
                }
            }
            vnfd.setAuto_scale_policy(autoScalePolicy);
            vnfds.add(vnfd);
        }
        nsd.setVnfd(vnfds);

        return nsd;
    }

    private static NetworkServiceDescriptor setStunServer(String mediaServerStunIP, String mediaServerTurnUsername, String mediaServerTurnPassword, NetworkServiceDescriptor nsd) {

        Set<VirtualNetworkFunctionDescriptor> vnfds = new HashSet<>();

        for (VirtualNetworkFunctionDescriptor vnfd : nsd.getVnfd()) {
            if (vnfd.getEndpoint().equals("media-server")){
                ConfigurationParameter cpIp = new ConfigurationParameter();
                cpIp.setConfKey("mediaserver.stun.url");
                cpIp.setValue(mediaServerStunIP);
                ConfigurationParameter cpUser = new ConfigurationParameter();
                cpUser.setConfKey("mediaserver.turn-server.username");
                cpUser.setValue(mediaServerTurnUsername);
                ConfigurationParameter cpPassword = new ConfigurationParameter();
                cpPassword.setConfKey("mediaserver.turn-server.password");
                cpPassword.setValue(mediaServerTurnPassword);
                org.openbaton.catalogue.nfvo.Configuration configuration = new org.openbaton.catalogue.nfvo.Configuration();
                configuration.setName("mediaserver");
                HashSet<ConfigurationParameter> cps = new HashSet<>();
                cps.add(cpIp);
                cps.add(cpUser);
                cps.add(cpPassword);
                configuration.setConfigurationParameters(cps);
                vnfd.setConfigurations(configuration);
                vnfds.add(vnfd);
            }
            else
                vnfds.add(vnfd);
        }
        nsd.setVnfd(vnfds);

        return nsd;
    }

    private static NetworkServiceDescriptor injectQoS(String qos, NetworkServiceDescriptor nsd) {

        Set<VirtualNetworkFunctionDescriptor> vnfds = new HashSet<>();

        for (VirtualNetworkFunctionDescriptor vnfd : nsd.getVnfd()) {
            if (vnfd.getEndpoint().equals("media-server")){
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
            else
                vnfds.add(vnfd);
        }
        nsd.setVnfd(vnfds);
        return nsd;
    }

    private static NetworkServiceDescriptor injectFlavor(String flavour, NetworkServiceDescriptor networkServiceDescriptor){

        Set<VirtualNetworkFunctionDescriptor> vnfds = new HashSet<>();

        for (VirtualNetworkFunctionDescriptor vnfd : networkServiceDescriptor.getVnfd()) {
            if (vnfd.getEndpoint().equals("media-server")){
                Set<VNFDeploymentFlavour> flavours = new HashSet<>();
                VNFDeploymentFlavour newFlavour = new VNFDeploymentFlavour();
                newFlavour.setFlavour_key(flavour);
                flavours.add(newFlavour);
                vnfd.setDeployment_flavour(flavours);
                vnfds.add(vnfd);
            }
        }

        networkServiceDescriptor.setVnfd(vnfds);
        return networkServiceDescriptor;
    }

}
