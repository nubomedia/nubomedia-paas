package org.project.openbaton.nubomedia.api;

import org.openbaton.catalogue.mano.descriptor.NetworkServiceDescriptor;
import org.openbaton.catalogue.mano.record.NetworkServiceRecord;
import org.openbaton.catalogue.mano.record.VirtualNetworkFunctionRecord;
import org.openbaton.catalogue.nfvo.Action;
import org.openbaton.catalogue.nfvo.EndpointType;
import org.openbaton.catalogue.nfvo.EventEndpoint;
import org.openbaton.sdk.api.exception.SDKException;
import org.project.openbaton.nubomedia.api.messages.BuildingStatus;
import org.openbaton.sdk.NFVORequestor;
import org.project.openbaton.nubomedia.api.openbaton.OpenbatonConfiguration;
import org.project.openbaton.nubomedia.api.openbaton.OpenbatonCreateServer;
import org.project.openbaton.nubomedia.api.openshift.ConfigReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Properties;
import java.util.Set;

/**
 * Created by lto on 24/09/15.
 */
@Service
public class OpenbatonManager {

    private Logger logger;
    private NetworkServiceDescriptor nsd;
    private Properties properties;
    private NFVORequestor nfvoRequestor;
    private String apiPath;

    @PostConstruct
    private void init() throws IOException, SDKException {

        this.logger = LoggerFactory.getLogger(this.getClass());
        this.properties = ConfigReader.loadProperties();
        this.nfvoRequestor = new NFVORequestor(properties.getProperty("openbatonUsername"),properties.getProperty("openbatonPasswd"),properties.getProperty("openbatonIP"), properties.getProperty("openbatonPort"),"1");
        this.apiPath = "/api/v1/nubomedia/paas";
        NetworkServiceDescriptor tmp = OpenbatonConfiguration.getNSD();
        logger.debug("NSD tmp " + tmp.toString());
        this.nsd = this.nfvoRequestor.getNetworkServiceDescriptorAgent().create(tmp);
        logger.debug("Official NSD " + nsd.toString());
    }

    public OpenbatonCreateServer getMediaServerGroupID(String flavorID, String appID) {

        OpenbatonCreateServer res = new OpenbatonCreateServer();
        NetworkServiceRecord nsr = null;
        EventEndpoint eventEndpoint = new EventEndpoint();
        eventEndpoint.setType(EndpointType.REST);
        eventEndpoint.setEndpoint(properties.getProperty("internalURL") + apiPath +"/openbaton/" + appID);
        eventEndpoint.setEvent(Action.INSTANTIATE_FINISH);

        try {

            nsr = nfvoRequestor.getNetworkServiceRecordAgent().create(nsd.getId());
            logger.debug("NSR " + nsr.toString());
            eventEndpoint.setNetworkServiceId(nsr.getId());
            res.setMediaServerID(nsr.getId());
            Set<VirtualNetworkFunctionRecord> vnfrs = nsr.getVnfr();

            for(VirtualNetworkFunctionRecord record : vnfrs){
                if(record.getType().equals("media-server")){
                    res.setVnfrID(record.getId());
                }
            }

            eventEndpoint = this.nfvoRequestor.getEventAgent().create(eventEndpoint);
            res.setEventID(eventEndpoint.getName());
        } catch (SDKException e) {
            e.printStackTrace();
        }
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


}
