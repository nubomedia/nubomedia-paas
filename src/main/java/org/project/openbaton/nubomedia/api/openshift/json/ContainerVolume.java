package org.project.openbaton.nubomedia.api.openshift.json;

/**
 * Created by Carlo on 09/02/2016.
 */
public class ContainerVolume {

    private String name;
    private boolean readOnly;
    private String mountPath;

    public ContainerVolume(String name, boolean readOnly, String mountPath) {
        this.name = name;
        this.readOnly = readOnly;
        this.mountPath = mountPath;
    }

    public ContainerVolume() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isReadOnly() {
        return readOnly;
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }

    public String getMountPath() {
        return mountPath;
    }

    public void setMountPath(String mountPath) {
        this.mountPath = mountPath;
    }
}
