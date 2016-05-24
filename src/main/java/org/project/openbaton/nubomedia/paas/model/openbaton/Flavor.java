package org.project.openbaton.nubomedia.paas.model.openbaton;

/**
 * Created by maa on 22.01.16.
 */
public enum Flavor {

    SMALL("d1.small"),
    MEDIUM("d1.medium"),
    LARGE("d1.large");

    private final String value;

    Flavor(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
