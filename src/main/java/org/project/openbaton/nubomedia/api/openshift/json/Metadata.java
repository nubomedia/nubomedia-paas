package org.project.openbaton.nubomedia.api.openshift.json;

/**
 * Created by Carlo on 25/09/2015.
 */
public class Metadata {

    private String name;

    public Metadata(String name){
        this.name = name;
    }

    public Metadata() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
