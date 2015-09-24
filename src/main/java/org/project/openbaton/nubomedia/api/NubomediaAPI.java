package org.project.openbaton.nubomedia.api;

import org.project.openbaton.sdk.NFVORequestor;

/**
 * Created by lto on 24/09/15.
 */
public class NubomediaAPI {

    private NFVORequestor nfvoRequestor;

    public NubomediaAPI() {
        this.nfvoRequestor = new NFVORequestor("","","","","");
    }
}
