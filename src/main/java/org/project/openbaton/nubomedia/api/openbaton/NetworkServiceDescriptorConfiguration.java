package org.project.openbaton.nubomedia.api.openbaton;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.openbaton.catalogue.mano.common.AutoScalePolicy;
import org.openbaton.catalogue.mano.common.ScalingAlarm;
import org.openbaton.catalogue.mano.common.VNFDeploymentFlavour;
import org.openbaton.catalogue.mano.descriptor.InternalVirtualLink;
import org.openbaton.catalogue.mano.descriptor.NetworkServiceDescriptor;
import org.openbaton.catalogue.mano.descriptor.VirtualDeploymentUnit;
import org.openbaton.catalogue.mano.descriptor.VirtualNetworkFunctionDescriptor;
import org.openbaton.catalogue.nfvo.ConfigurationParameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by maa on 27.01.16.
 */
public class NetworkServiceDescriptorConfiguration {

    private static Logger logger = LoggerFactory.getLogger(NetworkServiceDescriptorConfiguration.class);

    public static NetworkServiceDescriptor getNSD(String flavor, String Qos, String mediaServerTurnIP, String mediaServerTurnUsername, String mediaServerTurnPassword, int scaleInOut, double scale_in_threshold, double scale_out_threshold){

        logger.debug("Starting the descriptor");
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

        nsd = NetworkServiceDescriptorConfiguration.injectFlavor(flavor,scaleInOut, nsd);
        logger.debug("NSD WITH FLAVOR\n" + nsd.toString() + "\n*********************************");

        if (Qos != null){

            nsd = NetworkServiceDescriptorConfiguration.injectQoS(Qos,nsd);

        }

        if (mediaServerTurnIP != null){

            if (mediaServerTurnUsername == null || mediaServerTurnPassword == null){
                throw new IllegalArgumentException("No Authentication for Turn Server");
            }

            nsd = NetworkServiceDescriptorConfiguration.setStunServer(mediaServerTurnIP,mediaServerTurnUsername, mediaServerTurnPassword, nsd);
        }

        if (scale_in_threshold > 0){
            nsd = NetworkServiceDescriptorConfiguration.setScaleInThreshold(scale_in_threshold,nsd);
        }

        if (scale_out_threshold > 0){
            nsd = NetworkServiceDescriptorConfiguration.setScaleOutThreshold(scale_out_threshold,nsd);
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

    private static NetworkServiceDescriptor injectFlavor(String flavour,int scaleInOut, NetworkServiceDescriptor networkServiceDescriptor){

        Set<VirtualNetworkFunctionDescriptor> vnfds = new HashSet<>();

        for (VirtualNetworkFunctionDescriptor vnfd : networkServiceDescriptor.getVnfd()) {
            if (vnfd.getEndpoint().equals("media-server")){
                Set<VirtualDeploymentUnit> virtualDeploymentUnits = new HashSet<>();
                for (VirtualDeploymentUnit vdu : vnfd.getVdu()){
                    vdu.setScale_in_out(scaleInOut);
                    virtualDeploymentUnits.add(vdu);
                }
                vnfd.setVdu(virtualDeploymentUnits);
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
