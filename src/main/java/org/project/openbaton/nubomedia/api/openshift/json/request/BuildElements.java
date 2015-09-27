package org.project.openbaton.nubomedia.api.openshift.json.request;

/**
 * Created by Carlo on 25/09/2015.
 */
public class BuildElements {

    private String kind;
    private String name;

    public BuildElements() {
    }

    public BuildElements(String kind, String name) {
        this.kind = kind;
        this.name = name;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
