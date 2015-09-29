package org.project.openbaton.nubomedia.api.openshift.json.request;

/**
 * Created by maa on 25/09/2015.
 */
public class Trigger {

    private String type;

    public Trigger(){
    }

    public Trigger(String name) {
        this.type = name;
    }

    public String getName() {
        return type;
    }

    public void setName(String type) {
        this.type = type;
    }
}
