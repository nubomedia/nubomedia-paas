package org.project.openbaton.nubomedia.api;

import org.project.openbaton.nubomedia.api.messages.BuildingStatus;
import org.project.openbaton.sdk.NFVORequestor;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * Created by lto on 24/09/15.
 */
@Service
public class OpenbatonManager {

    private NFVORequestor nfvoRequestor;

    @PostConstruct
    private void init(){
        this.nfvoRequestor = new NFVORequestor("","","","","");
    }

    public String getMediaServerGroupID(){
        return "";
    }

    public BuildingStatus getStatus(){
        return BuildingStatus.INITIALISED;
    }


}
