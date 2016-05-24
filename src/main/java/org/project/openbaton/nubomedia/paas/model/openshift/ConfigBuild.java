package org.project.openbaton.nubomedia.paas.model.openshift;

/**
 * Created by maa on 08.10.15.
 */
public class ConfigBuild {

    private String kind;
    private String namespace;
    private String name;

    public ConfigBuild(String kind, String namespace, String name) {
        this.kind = kind;
        this.namespace = namespace;
        this.name = name;
    }

    public ConfigBuild() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }
}
