package org.project.openbaton.nubomedia.api.openshift.json.request;

/**
 * Created by maa on 25/09/2015.
 */
public class SpecDeploy {

    private Container[] containers;

    public SpecDeploy(Container[] containers) {
        this.containers = containers;
    }

    public SpecDeploy() {
    }

    public Container[] getContainers() {
        return containers;
    }

    public void setContainers(Container[] containers) {
        this.containers = containers;
    }
}
