package org.project.openbaton.nubomedia.api.openshift.json;

import java.util.List;

/**
 * Created by maa on 25/09/2015.
 */
public class Container {

    private String name;
    private String image;
    private Port[] ports;
    private List<ContainerVolume> volumeMounts;

    public static class Port{
        String protocol;
        int containerPort;

        public Port(String protocol, int containerPort) {
            this.protocol = protocol;
            this.containerPort = containerPort;
        }

        public Port() {
        }

        public String getProtocol() {
            return protocol;
        }

        public void setProtocol(String protocol) {
            this.protocol = protocol;
        }

        public int getContainerPort() {
            return containerPort;
        }

        public void setContainerPort(int containerPort) {
            this.containerPort = containerPort;
        }
    }

    public Container(String name, String image, Port[] ports, List<ContainerVolume> volumeMounts) {
        this.name = name;
        this.image = image;
        this.ports = ports;
        this.volumeMounts = volumeMounts;
    }

    public Container(String name, String image, Port[] ports) {
        this.name = name;
        this.image = image;
        this.ports = ports;
    }

    public Container() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Port[] getPorts() {
        return ports;
    }

    public void setPorts(Port[] ports) {
        this.ports = ports;
    }

    public List<ContainerVolume> getVolumeMounts() {
        return volumeMounts;
    }

    public void setVolumeMounts(List<ContainerVolume> volumeMounts) {
        this.volumeMounts = volumeMounts;
    }
}
