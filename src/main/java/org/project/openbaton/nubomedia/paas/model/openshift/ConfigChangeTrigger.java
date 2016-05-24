package org.project.openbaton.nubomedia.paas.model.openshift;

/**
 * Created by maa on 25/09/2015.
 */
public class ConfigChangeTrigger implements Trigger{

    private String type;

    public ConfigChangeTrigger(){
    }

    public ConfigChangeTrigger(String name) {
        this.type = name;
    }

    public String getName() {
        return type;
    }

    public void setName(String type) {
        this.type = type;
    }
}
