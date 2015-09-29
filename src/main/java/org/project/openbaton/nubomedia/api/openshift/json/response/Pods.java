package org.project.openbaton.nubomedia.api.openshift.json.response;

import java.util.List;

/**
 * Created by maa on 29.09.15.
 */
public class Pods {

    private List<String> podNames;

    public Pods(List<String> podNames) {
        this.podNames = podNames;
    }

    public Pods() {
    }

    public List<String> getPodNames() {
        return podNames;
    }

    public void setPodNames(List<String> podNames) {
        this.podNames = podNames;
    }
}
