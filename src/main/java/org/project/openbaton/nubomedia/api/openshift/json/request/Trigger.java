package org.project.openbaton.nubomedia.api.openshift.json.request;

/**
 * Created by Carlo on 25/09/2015.
 */
public class Trigger {

    private String name;

    public Trigger(){
    }

    public Trigger(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
