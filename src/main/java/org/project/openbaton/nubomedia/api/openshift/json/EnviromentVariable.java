package org.project.openbaton.nubomedia.api.openshift.json;

/**
 * Created by maa on 08.10.15.
 */
public class EnviromentVariable {

    private String name;
    private String value;

    public EnviromentVariable(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public EnviromentVariable() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
