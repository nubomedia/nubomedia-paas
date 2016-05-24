package org.project.openbaton.nubomedia.paas.model.openshift;

/**
 * Created by maa on 08.10.15.
 */
public class SecretID {

    private String name;

    public SecretID(String name) {
        this.name = name;
    }

    public SecretID() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
