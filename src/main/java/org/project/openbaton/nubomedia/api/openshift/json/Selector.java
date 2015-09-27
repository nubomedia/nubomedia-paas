package org.project.openbaton.nubomedia.api.openshift.json;

/**
 * Created by Carlo on 25/09/2015.
 */
public class Selector {

    private String name;

    public Selector(String name) {
        this.name = name;
    }

    public Selector() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
