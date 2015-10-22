package org.project.openbaton.nubomedia.api.openbaton;

import org.openbaton.catalogue.mano.descriptor.NetworkServiceDescriptor;
import org.openbaton.catalogue.mano.record.NetworkServiceRecord;
import org.openbaton.catalogue.nfvo.Action;

/**
 * Created by maa on 21.10.15.
 */
public class OpenbatonEvent {

    private Action action;
    private NetworkServiceRecord payload;

    public OpenbatonEvent() {
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public NetworkServiceRecord getPayload() {
        return payload;
    }

    public void setPayload(NetworkServiceRecord payload) {
        this.payload = payload;
    }
}
