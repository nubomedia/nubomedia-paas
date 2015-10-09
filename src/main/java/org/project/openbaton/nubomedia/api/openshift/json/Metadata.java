package org.project.openbaton.nubomedia.api.openshift.json;

/**
 * Created by maa on 25/09/2015.
 */
public class Metadata {

    private String name;
    private String selflink;
    private String resourceVersion;

    public Metadata(String name, String selflink, String resourceVersion) {
        this.name = name;
        this.selflink = selflink;
        this.resourceVersion = resourceVersion;
    }

    public Metadata() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSelflink() {
        return selflink;
    }

    public void setSelflink(String selflink) {
        this.selflink = selflink;
    }

    public String getResourceVersion() {
        return resourceVersion;
    }

    public void setResourceVersion(String resourceVersion) {
        this.resourceVersion = resourceVersion;
    }
}
