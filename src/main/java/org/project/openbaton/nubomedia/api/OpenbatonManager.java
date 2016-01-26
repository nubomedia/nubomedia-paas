package org.project.openbaton.nubomedia.api;

import org.openbaton.catalogue.mano.descriptor.NetworkServiceDescriptor;
import org.openbaton.catalogue.mano.descriptor.VirtualNetworkFunctionDescriptor;
import org.openbaton.catalogue.mano.record.NetworkServiceRecord;
import org.openbaton.catalogue.nfvo.*;
import org.openbaton.sdk.NFVORequestor;
import org.openbaton.sdk.api.exception.SDKException;
import org.project.openbaton.nubomedia.api.configuration.NfvoProperties;
import org.project.openbaton.nubomedia.api.messages.BuildingStatus;
import org.project.openbaton.nubomedia.api.openbaton.Flavor;
import org.project.openbaton.nubomedia.api.openbaton.OpenbatonConfiguration;
import org.project.openbaton.nubomedia.api.openbaton.OpenbatonCreateServer;
import org.project.openbaton.nubomedia.api.openbaton.QoS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by lto on 24/09/15.
 */
@Service
public class OpenbatonManager {

    @Autowired private VimInstance vimInstance;
    @Autowired private NfvoProperties nfvoProperties;
    @Autowired private VirtualNetworkFunctionDescriptor cloudRepository;
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
                    }
                }
            } catch (SDKException | ClassNotFoundException e1) {
                e1.printStackTrace();
            }
        }

        this.records = new HashMap<>();
    }

    public OpenbatonCreateServer getMediaServerGroupID(Flavor flavorID, String appID, String callbackUrl, boolean cloudRepositorySet, QoS qos,String serverTurnIp,String serverTurnUsername, String serverTurnPassword, double scale_in_threshold, double scale_out_threshold) throws SDKException {

        NetworkServiceDescriptor targetNSD = OpenbatonConfiguration.getNSD(flavorID.getValue(),qos.toString(),serverTurnIp,serverTurnUsername,serverTurnPassword, scale_in_threshold,scale_out_threshold);

        if (cloudRepositorySet){
            VirtualNetworkFunctionDescriptor cloudRepoDef = this.setRandomPassword(cloudRepository);
            Set<VirtualNetworkFunctionDescriptor> vnfds = targetNSD.getVnfd();
            vnfds.add(cloudRepoDef);
            targetNSD.setVnfd(vnfds);
        }
        targetNSD = nfvoRequestor.getNetworkServiceDescriptorAgent().create(targetNSD);

        OpenbatonCreateServer res = new OpenbatonCreateServer();
        res.setNsdID(targetNSD.getId());
        NetworkServiceRecord nsr = null;
        EventEndpoint eventEndpoint = new EventEndpoint();
        eventEndpoint.setType(EndpointType.REST);
        eventEndpoint.setEndpoint(callbackUrl + apiPath + "/openbaton/" + appID);
        eventEndpoint.setEvent(Action.INSTANTIATE_FINISH);

        nsr = nfvoRequestor.getNetworkServiceRecordAgent().create(targetNSD.getId());
        logger.debug("NSR " + nsr.toString());
        this.records.put(nsr.getId(), nsr);
        eventEndpoint.setNetworkServiceId(nsr.getId());
        res.setMediaServerID(nsr.getId());

        eventEndpoint = this.nfvoRequestor.getEventAgent().create(eventEndpoint);
        res.setEventID(eventEndpoint.getId());

        logger.debug("Result " + res.toString());
        return res;
    }

    public BuildingStatus getStatus(String nsrID) {
        NetworkServiceRecord nsr = null;
        BuildingStatus res = BuildingStatus.CREATED;
        try {
            nsr = nfvoRequestor.getNetworkServiceRecordAgent().findById(nsrID);
        } catch (SDKException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        if (nsr != null) {
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

    public VirtualNetworkFunctionDescriptor setRandomPassword(VirtualNetworkFunctionDescriptor cloudRepository){

        SecureRandom random = new SecureRandom();
        Configuration configuration = cloudRepository.getConfigurations();
        Set<ConfigurationParameter> parameters = configuration.getConfigurationParameters();
        for (ConfigurationParameter configurationParameter : parameters){
            if (configurationParameter.getConfKey().equals("PASSWORD")){
                configurationParameter.setValue(new BigInteger(130,random).toString(32));
            }
            parameters.add(configurationParameter);
        }
        configuration.setConfigurationParameters(parameters);
        cloudRepository.setConfigurations(configuration);
        return cloudRepository;
    }

//    @PreDestroy
//    private void deleteNSD() throws SDKException {
//        if (!this.records.isEmpty()) {
//            for (String nsrId : records.keySet()) {
//                this.nfvoRequestor.getNetworkServiceRecordAgent().delete(nsrId);
//                this.records.remove(nsrId);
//            }
//        }
//        this.nfvoRequestor.getNetworkServiceDescriptorAgent().delete(nsd.getId());
//        this.nfvoRequestor.getVimInstanceAgent().delete(this.vimInstance.getId());
//
//    }


}
