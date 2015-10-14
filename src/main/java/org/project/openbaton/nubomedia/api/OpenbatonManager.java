package org.project.openbaton.nubomedia.api;

import org.openbaton.catalogue.mano.descriptor.NetworkServiceDescriptor;
import org.openbaton.catalogue.mano.record.NetworkServiceRecord;
import org.openbaton.catalogue.mano.record.Status;
import org.openbaton.sdk.api.exception.SDKException;
import org.project.openbaton.nubomedia.api.messages.BuildingStatus;
import org.openbaton.sdk.NFVORequestor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * Created by lto on 24/09/15.
 */
@Service
public class OpenbatonManager {

    @Autowired private NetworkServiceDescriptor nsd;
    private NFVORequestor nfvoRequestor;

    @PostConstruct
    private void init(){
        this.nfvoRequestor = new NFVORequestor("","","","","");
    }


    //Stati: NULL, INSTANTIATED, ACTIVE
    public String getMediaServerGroupID(String flavorID) {

//        NetworkServiceRecord nsr = null;
//
//        try {
//            this.nfvoRequestor.getNetworkServiceDescriptorAgent().create(nsd);
//            nsr = nfvoRequestor.getNetworkServiceRecordAgent().create(nsd.getId());
//        } catch (SDKException e) {
//            e.printStackTrace();
//        }


        return "ciaoPippo";
    }

    public BuildingStatus getStatus(String nsrID){
        NetworkServiceRecord nsr = null;
        BuildingStatus res = BuildingStatus.INITIALISED;
//        try {
//             nsr = nfvoRequestor.getNetworkServiceRecordAgent().findById(nsrID);
//        } catch (SDKException e) {
//            e.printStackTrace();
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }
//
//        switch (nsr.getStatus()){
//            case NULL:
//                res = BuildingStatus.CREATED;
//                break;
//            case INITIALIZED:
//                res = BuildingStatus.INITIALIZING;
//                break;
//            case ERROR:
//                res = BuildingStatus.FAILED;
//                break;
//            case ACTIVE:
//                res = BuildingStatus.INITIALISED;
//                break;
//        }

        return res;
    }

    public void deleteRecord(String nsrID){
       /* try {
            nfvoRequestor.getNetworkServiceRecordAgent().delete(nsrID);
        } catch (SDKException e) {
            e.printStackTrace();
        }*/
    }


}
