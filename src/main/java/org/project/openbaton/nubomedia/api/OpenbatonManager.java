package org.project.openbaton.nubomedia.api;

import org.openbaton.catalogue.mano.common.*;
import org.openbaton.catalogue.mano.descriptor.InternalVirtualLink;
import org.openbaton.catalogue.mano.descriptor.NetworkServiceDescriptor;
import org.openbaton.catalogue.mano.descriptor.VirtualDeploymentUnit;
import org.openbaton.catalogue.mano.descriptor.VirtualNetworkFunctionDescriptor;
import org.openbaton.catalogue.mano.record.NetworkServiceRecord;
import org.openbaton.catalogue.mano.record.VirtualNetworkFunctionRecord;
import org.openbaton.catalogue.nfvo.*;
import org.openbaton.sdk.NFVORequestor;
import org.openbaton.sdk.api.exception.SDKException;
import org.project.openbaton.nubomedia.api.configuration.NfvoProperties;
import org.project.openbaton.nubomedia.api.messages.BuildingStatus;
import org.project.openbaton.nubomedia.api.openbaton.Flavor;
import org.project.openbaton.nubomedia.api.openbaton.OpenbatonCreateServer;
import org.project.openbaton.nubomedia.api.openbaton.QoS;
import org.project.openbaton.nubomedia.api.openbaton.exceptions.StunServerException;
import org.project.openbaton.nubomedia.api.openbaton.exceptions.turnServerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.*;

/**
 * Created by lto on 24/09/15.
 */
@Service
public class OpenbatonManager {

    @Autowired private VimInstance vimInstance;
    @Autowired private NfvoProperties nfvoProperties;
    @Autowired private VirtualNetworkFunctionDescriptor cloudRepository;
    @Autowired private NetworkServiceDescriptor nsdFromFile;
    private Logger logger;
    private NFVORequestor nfvoRequestor;
    private String apiPath;
    private Map<String, NetworkServiceRecord> records;

    @PostConstruct
    private void init() throws IOException {

        this.logger = LoggerFactory.getLogger(this.getClass());
        this.nfvoRequestor = new NFVORequestor(nfvoProperties.getOpenbatonUsername(), nfvoProperties.getOpenbatonPasswd(), nfvoProperties.getOpenbatonIP(), nfvoProperties.getOpenbatonPort(), "1");
        this.apiPath = "/api/v1/nubomedia/paas";

        try {
            vimInstance = this.nfvoRequestor.getVimInstanceAgent().create(vimInstance);
        } catch (SDKException e){
            try {
                List<VimInstance> instances = nfvoRequestor.getVimInstanceAgent().findAll();
                for (VimInstance instance : instances){
                    if (vimInstance.getName().equals(instance.getName())){
                        if (!vimInstance.getAuthUrl().equals(instance.getAuthUrl()) && !vimInstance.getUsername().equals(instance.getUsername()) && !vimInstance.getPassword().equals(instance.getPassword())){
                            nfvoRequestor.getVimInstanceAgent().update(vimInstance,instance.getId());
                        }
                        else{
                            vimInstance = instance;
                        }
                    }
                }
            } catch (SDKException | ClassNotFoundException e1) {
                e1.printStackTrace();
            }
        }

        this.records = new HashMap<>();
    }

    public OpenbatonCreateServer getMediaServerGroupID(Flavor flavorID, String appID, String callbackUrl, boolean cloudRepositorySet, QoS qos,boolean turnServerActivate, String serverTurnIp,String serverTurnUsername, String serverTurnPassword, boolean stunServerActivate, String stunServerIp, String stunServerPort, int scaleInOut, double scale_out_threshold) throws SDKException, turnServerException, StunServerException {

        logger.debug("FlavorID " + flavorID + " appID " + appID + " callbackURL " + callbackUrl + " isCloudRepo " + cloudRepositorySet + " QOS " + qos + "turnServerIp " + serverTurnIp + " serverTurnName " + serverTurnUsername + " scaleInOut " + scaleInOut);

        NetworkServiceDescriptor targetNSD = this.configureDescriptor(nsdFromFile,flavorID,qos,turnServerActivate, serverTurnIp,serverTurnUsername,serverTurnPassword,stunServerActivate, stunServerIp, stunServerPort, scaleInOut,scale_out_threshold);

        if (cloudRepositorySet){
            Set<VirtualNetworkFunctionDescriptor> vnfds = targetNSD.getVnfd();
            vnfds.add(cloudRepository);
            logger.debug("VNFDS " + vnfds.toString());
            targetNSD.setVnfd(vnfds);
        }
        targetNSD = nfvoRequestor.getNetworkServiceDescriptorAgent().create(targetNSD);

        OpenbatonCreateServer res = new OpenbatonCreateServer();
        res.setNsdID(targetNSD.getId());
        NetworkServiceRecord nsr = null;

        nsr = nfvoRequestor.getNetworkServiceRecordAgent().create(targetNSD.getId());
        logger.debug("NSR " + nsr.toString());
        this.records.put(nsr.getId(), nsr);

        EventEndpoint eventEndpointCreation = new EventEndpoint();
        eventEndpointCreation.setType(EndpointType.REST);
        eventEndpointCreation.setEndpoint(callbackUrl + apiPath + "/openbaton/" + appID);
        eventEndpointCreation.setEvent(Action.INSTANTIATE_FINISH);
        eventEndpointCreation.setNetworkServiceId(nsr.getId());

        EventEndpoint eventEndpointError = new EventEndpoint();
        eventEndpointError.setType(EndpointType.REST);
        eventEndpointError.setEndpoint(callbackUrl + apiPath + "/openbaton/" + appID);
        eventEndpointError.setEvent(Action.ERROR);
        eventEndpointError.setNetworkServiceId(nsr.getId());

        this.records.put(nsr.getId(), nsr);
        res.setMediaServerID(nsr.getId());

        eventEndpointCreation = this.nfvoRequestor.getEventAgent().create(eventEndpointCreation);
        res.setEventAllocatedID(eventEndpointCreation.getId());

        eventEndpointError = this.nfvoRequestor.getEventAgent().create(eventEndpointError);
        res.setEventErrorID(eventEndpointError.getId());

        logger.debug("Result " + res.toString());
        return res;
    }

    public BuildingStatus getStatus(String nsrID) {
        NetworkServiceRecord nsr = null;
        BuildingStatus res = BuildingStatus.CREATED;
        try {
            nsr = nfvoRequestor.getNetworkServiceRecordAgent().findById(nsrID);
        } catch (SDKException | ClassNotFoundException e) {
            logger.debug("no NSR found");
            res = BuildingStatus.FAILED;
        }

        if (nsr != null) {
            if(nsr.getStatus() != null) {
                switch (nsr.getStatus()) {
                    case NULL:
                        res = BuildingStatus.CREATED;
                        break;
                    case INITIALIZED:
                        res = BuildingStatus.INITIALIZING;
                        break;
                    case ERROR:
                        res = BuildingStatus.FAILED;
                        break;
                    case ACTIVE:
                        res = BuildingStatus.INITIALISED;
                        break;
                }
            }
            else{
                res = BuildingStatus.FAILED;
            }
        }
        else{
            res = BuildingStatus.FAILED;
        }


        return res;
    }

    public void deleteRecord(String nsrID) {
        try {
            records.remove(nsrID);
            nfvoRequestor.getNetworkServiceRecordAgent().delete(nsrID);
        } catch (SDKException e) {
            e.printStackTrace();
        }
    }

    public void deleteEvent(String eventID) {
        try {
            this.nfvoRequestor.getEventAgent().delete(eventID);
        } catch (SDKException e) {
            e.printStackTrace();
        }
    }

    public void deleteDescriptor(String nsdID){
        try {
            this.nfvoRequestor.getNetworkServiceDescriptorAgent().delete(nsdID);
        } catch (SDKException e){
            e.printStackTrace();
        }
    }

    private NetworkServiceDescriptor configureDescriptor(NetworkServiceDescriptor nsd, Flavor flavor, QoS Qos, boolean turnServerActivate, String mediaServerTurnIP, String mediaServerTurnUsername, String mediaServerTurnPassword, boolean stunServerActivate, String stunServerAddress, String stunServerPort, int scaleInOut, double scale_out_threshold) throws turnServerException, StunServerException {
        logger.debug("Start configure");
        nsd = this.injectFlavor(flavor.getValue(), nfvoProperties.getImageName(), nsd);
        logger.debug("After flavor the nsd is\n" + nsd.toString() + "\n****************************");

        if (Qos != null){
            logger.debug("\nSetting QoS");
            nsd = this.injectQoS(Qos.toString(),nsd);
            logger.debug("After QOS the nsd is\n" + nsd.toString() + "\n############################");
        }

        logger.debug("Setting Configuration parameters for mediaserver");
        nsd = this.setConfigurationParameters(turnServerActivate,mediaServerTurnIP,mediaServerTurnUsername,mediaServerTurnPassword,stunServerActivate,stunServerAddress,stunServerPort,nsd);
        logger.debug("Settled Configuration parameters for mediaserver, the new NSD is " + nsd.toString());

        if (scaleInOut > 1) {
            logger.debug("Setting autoscaling policies");
            nsd = this.enableAutoscaling(scaleInOut,scale_out_threshold,nsd);
        }

        return nsd;
    }

    private NetworkServiceDescriptor enableAutoscaling(int scaleInOut, double scale_out_threshold, NetworkServiceDescriptor nsd) {

        AutoScalePolicy scaleOutPolicy = new AutoScalePolicy();
        scaleOutPolicy.setName("scale-out");
        scaleOutPolicy.setComparisonOperator(">=");
        scaleOutPolicy.setPeriod(30);
        scaleOutPolicy.setCooldown(60);
        scaleOutPolicy.setMode(ScalingMode.REACTIVE);
        scaleOutPolicy.setType(ScalingType.VOTED);
        ScalingAlarm scaleOutAlarm = new ScalingAlarm();
        scaleOutAlarm.setComparisonOperator(">=");
        scaleOutAlarm.setStatistic("avg");
        scaleOutAlarm.setMetric("CONSUMED_CAPACITY");

        if (scale_out_threshold > 0){
            scaleOutAlarm.setThreshold(scale_out_threshold);
        }
        else{
            scaleOutAlarm.setThreshold(140);
        }
        scaleOutAlarm.setWeight(1);
        Set<ScalingAlarm> scaleOutalarms = new HashSet<>();
        scaleOutalarms.add(scaleOutAlarm);
        Set<ScalingAction> scaleOutActions = new HashSet<>();
        ScalingAction scaleOutAction = new ScalingAction();
        scaleOutAction.setType(ScalingActionType.SCALE_OUT);
        scaleOutAction.setValue("2");
        scaleOutActions.add(scaleOutAction);
        scaleOutPolicy.setActions(scaleOutActions);
        scaleOutPolicy.setAlarms(scaleOutalarms);


        AutoScalePolicy scaleInPolicy = new AutoScalePolicy();
        scaleInPolicy.setName("scale-in");
        scaleInPolicy.setComparisonOperator(">=");
        scaleInPolicy.setPeriod(30);
        scaleInPolicy.setCooldown(60);
        scaleInPolicy.setMode(ScalingMode.REACTIVE);
        scaleInPolicy.setType(ScalingType.VOTED);
        ScalingAlarm scaleInAlarm = new ScalingAlarm();
        scaleInAlarm.setComparisonOperator(">=");
        scaleInAlarm.setStatistic("avg");
        scaleInAlarm.setMetric("CONSUMED_CAPACITY");
        scaleInAlarm.setThreshold(60);
        scaleInAlarm.setWeight(1);
        Set<ScalingAlarm> scaleInalarms = new HashSet<>();
        scaleInalarms.add(scaleInAlarm);
        Set<ScalingAction> scaleInActions = new HashSet<>();
        ScalingAction scaleInAction = new ScalingAction();
        scaleInAction.setType(ScalingActionType.SCALE_IN);
        scaleInAction.setValue("2");
        scaleInActions.add(scaleInAction);
        scaleInPolicy.setAlarms(scaleInalarms);
        scaleInPolicy.setActions(scaleInActions);

        Set<AutoScalePolicy> autoscaling = new HashSet<>();
        autoscaling.add(scaleOutPolicy);
        autoscaling.add(scaleInPolicy);

        Set<VirtualNetworkFunctionDescriptor> virtualNetworkFunctionDescriptors = new HashSet<>();
        for (VirtualNetworkFunctionDescriptor vnfd : nsd.getVnfd()){
            if(vnfd.getEndpoint().equals("media-server")){
                vnfd.setAuto_scale_policy(autoscaling);
                Set<VirtualDeploymentUnit> vdus = new HashSet<>();
                for (VirtualDeploymentUnit vdu : vnfd.getVdu()){
                    vdu.setScale_in_out(scaleInOut);
                    vdus.add(vdu);
                }
                vnfd.setVdu(vdus);
            }
            virtualNetworkFunctionDescriptors.add(vnfd);
        }
        nsd.setVnfd(virtualNetworkFunctionDescriptors);

        return nsd;
    }

    private NetworkServiceDescriptor setConfigurationParameters(boolean turnServerActivate, String mediaServerTurnIP, String mediaServerTurnUsername, String mediaServerTurnPassword, boolean stunServerActivate, String stunServerAddress, String stunServerPort, NetworkServiceDescriptor nsd) throws turnServerException, StunServerException {
        Set<VirtualNetworkFunctionDescriptor> vnfds = new HashSet<>();

        for (VirtualNetworkFunctionDescriptor vnfd : nsd.getVnfd()) {
            if (vnfd.getEndpoint().equals("media-server")){

                org.openbaton.catalogue.nfvo.Configuration configuration = new org.openbaton.catalogue.nfvo.Configuration();
                configuration.setName("mediaserver");
                HashSet<ConfigurationParameter> cps = new HashSet<>();

                if (turnServerActivate) {
                    ConfigurationParameter cpTurnActivate = new ConfigurationParameter();
                    cpTurnActivate.setConfKey("mediaserver.turn-server.activate");
                    cpTurnActivate.setValue("true");
                    cps.add(cpTurnActivate);

                    if (mediaServerTurnIP != null) {

                        if (mediaServerTurnUsername == null || mediaServerTurnPassword == null) {
                            throw new turnServerException("No Authentication for Turn Server");
                        }
                        ConfigurationParameter cpIp = new ConfigurationParameter();
                        cpIp.setConfKey("mediaserver.turn-server.url");
                        cpIp.setValue(mediaServerTurnIP);
                        cps.add(cpIp);

                        ConfigurationParameter cpUser = new ConfigurationParameter();
                        cpUser.setConfKey("mediaserver.turn-server.username");
                        cpUser.setValue(mediaServerTurnUsername);
                        cps.add(cpUser);

                        ConfigurationParameter cpPassword = new ConfigurationParameter();
                        cpPassword.setConfKey("mediaserver.turn-server.password");
                        cpPassword.setValue(mediaServerTurnPassword);
                        cps.add(cpPassword);
                    }
                }
                else{
                    ConfigurationParameter cpTurnActivate = new ConfigurationParameter();
                    cpTurnActivate.setConfKey("mediaserver.turn-server.activate");
                    cpTurnActivate.setValue("false");
                    cps.add(cpTurnActivate);
                }

                if (stunServerActivate){
                    ConfigurationParameter cpStunAct = new ConfigurationParameter();
                    cpStunAct.setConfKey("mediaserver.stun-server.activate");
                    cpStunAct.setValue("true");
                    cps.add(cpStunAct);

                    if ((stunServerAddress != null && stunServerPort == null) || (stunServerAddress == null && stunServerPort != null)){
                        throw new StunServerException("Is mandatory to specify Stun server address and Stun server port");
                    }
                    else if (stunServerAddress != null && stunServerPort != null){
                        ConfigurationParameter cpStunAddr = new ConfigurationParameter();
                        cpStunAddr.setConfKey("mediaserver.stun-server.address");
                        cpStunAddr.setValue(stunServerAddress);
                        cps.add(cpStunAddr);

                        ConfigurationParameter cpStunPort = new ConfigurationParameter();
                        cpStunPort.setConfKey("mediaserver.stun-server.port");
                        cpStunPort.setValue(stunServerPort);
                        cps.add(cpStunPort);
                    }

                }else{
                    ConfigurationParameter cpStunAct = new ConfigurationParameter();
                    cpStunAct.setConfKey("mediaserver.stun-server.activate");
                    cpStunAct.setValue("false");
                    cps.add(cpStunAct);
                }

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

    private NetworkServiceDescriptor injectQoS(String qos, NetworkServiceDescriptor nsd) {

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

    private NetworkServiceDescriptor injectFlavor(String flavour, String imageName, NetworkServiceDescriptor networkServiceDescriptor){

        Set<VirtualNetworkFunctionDescriptor> vnfds = new HashSet<>();

        for (VirtualNetworkFunctionDescriptor vnfd : networkServiceDescriptor.getVnfd()) {
            if (vnfd.getEndpoint().equals("media-server")){
                Set<VirtualDeploymentUnit> virtualDeploymentUnits = new HashSet<>();
                for (VirtualDeploymentUnit vdu : vnfd.getVdu()){
                    Set<String> images = new HashSet<>();
                    images.add(imageName);
                    vdu.setVm_image(images);
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
