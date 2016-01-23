package org.project.openbaton.nubomedia.api;

import org.openbaton.catalogue.mano.descriptor.NetworkServiceDescriptor;
import org.openbaton.catalogue.mano.record.NetworkServiceRecord;
import org.openbaton.catalogue.nfvo.Action;
import org.openbaton.catalogue.nfvo.EndpointType;
import org.openbaton.catalogue.nfvo.EventEndpoint;
import org.openbaton.catalogue.nfvo.VimInstance;
import org.openbaton.sdk.NFVORequestor;
import org.openbaton.sdk.api.exception.SDKException;
import org.project.openbaton.nubomedia.api.configuration.NfvoProperties;
import org.project.openbaton.nubomedia.api.messages.BuildingStatus;
import org.project.openbaton.nubomedia.api.openbaton.OpenbatonConfiguration;
import org.project.openbaton.nubomedia.api.openbaton.OpenbatonCreateServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lto on 24/09/15.
 */
@Service
public class OpenbatonManager {

    @Autowired private VimInstance vimInstance;
    @Autowired private NfvoProperties nfvoProperties;
    private Logger logger;
    private NetworkServiceDescriptor nsd;
//    private Properties properties;
    private NFVORequestor nfvoRequestor;
    private String apiPath;
    private Map<String, NetworkServiceRecord> records;

    @PostConstruct
    private void init() throws IOException, SDKException {
//        this.properties = ConfigReader.loadProperties();
        this.logger = LoggerFactory.getLogger(this.getClass());
        this.nfvoRequestor = new NFVORequestor(nfvoProperties.getOpenbatonUsername(),nfvoProperties.getOpenbatonPasswd(),nfvoProperties.getOpenbatonIP(), nfvoProperties.getOpenbatonPort(),"1");
        //this.vim = configuration;
        this.apiPath = "/api/v1/nubomedia/paas";
        NetworkServiceDescriptor tmp = new NetworkServiceDescriptor();
        logger.debug("NSD tmp " + tmp.toString());
        this.nfvoRequestor.getVimInstanceAgent().create(vimInstance);
        this.nsd = this.nfvoRequestor.getNetworkServiceDescriptorAgent().create(tmp);
        this.records = new HashMap<>();
        logger.debug("Official NSD " + nsd.toString());
    }

    public OpenbatonCreateServer getMediaServerGroupID(String flavorID, String appID,String callbackUrl) throws SDKException {

        OpenbatonCreateServer res = new OpenbatonCreateServer();
        NetworkServiceRecord nsr = null;
        EventEndpoint eventEndpoint = new EventEndpoint();
        eventEndpoint.setType(EndpointType.REST);
        eventEndpoint.setEndpoint(callbackUrl + apiPath +"/openbaton/" + appID);
        eventEndpoint.setEvent(Action.INSTANTIATE_FINISH);

        nsr = nfvoRequestor.getNetworkServiceRecordAgent().create(nsd.getId());
        logger.debug("NSR " + nsr.toString());
        this.records.put(nsr.getId(),nsr);
        eventEndpoint.setNetworkServiceId(nsr.getId());
        res.setMediaServerID(nsr.getId());

        eventEndpoint = this.nfvoRequestor.getEventAgent().create(eventEndpoint);
        res.setEventID(eventEndpoint.getId());

        logger.debug("Result " + res.toString());
        return res;
    }

    public BuildingStatus getStatus(String nsrID){
        NetworkServiceRecord nsr = null;
        BuildingStatus res = BuildingStatus.CREATED;
        try {
             nsr = nfvoRequestor.getNetworkServiceRecordAgent().findById(nsrID);
        } catch (SDKException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        if (nsr != null) {
            switch (nsr.getStatus()){
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

    public void deleteRecord(String nsrID){
        try {
            records.remove(nsrID);
            nfvoRequestor.getNetworkServiceRecordAgent().delete(nsrID);
        } catch (SDKException e) {
            e.printStackTrace();
        }
    }

    public void deleteEvent(String eventID){
        try {
            this.nfvoRequestor.getEventAgent().delete(eventID);
        } catch (SDKException e) {
            e.printStackTrace();
        }
    }

    @PreDestroy
    private void deleteNSD() throws SDKException {
        if(!this.records.isEmpty()){
            for(String nsrId : records.keySet()){
                this.nfvoRequestor.getNetworkServiceRecordAgent().delete(nsrId);
                this.records.remove(nsrId);
            }
        }
        this.nfvoRequestor.getNetworkServiceDescriptorAgent().delete(nsd.getId());
        this.nfvoRequestor.getVimInstanceAgent().delete(this.vimInstance.getId());

    }


}
