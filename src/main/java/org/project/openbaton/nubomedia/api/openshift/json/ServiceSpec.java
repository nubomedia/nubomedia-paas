package org.project.openbaton.nubomedia.api.openshift.json;

/**
 * Created by Carlo on 25/09/2015.
 */
public class ServiceSpec {

    private Selector selector;

    private ServicePort[] ports;

    public static class ServicePort{
        String protocol;
        int port;
        int targetPort;

        public ServicePort() {
        }

        public ServicePort(String protocol, int port, int targetPort) {
            this.protocol = protocol;
            this.port = port;
            this.targetPort = targetPort;
        }

        public String getProtocol() {
            return protocol;
        }

        public void setProtocol(String protocol) {
            this.protocol = protocol;
        }

        public int getPort() {
            return port;
        }

        public void setPort(int port) {
            this.port = port;
        }

        public int getTargetPort() {
            return targetPort;
        }

        public void setTargetPort(int targetPort) {
            this.targetPort = targetPort;
        }
    }

    public ServiceSpec() {
    }

    public ServiceSpec(Selector selector, ServicePort[] ports) {
        this.selector = selector;
        this.ports = ports;
    }

    public Selector getSelector() {
        return selector;
    }

    public void setSelector(Selector selector) {
        this.selector = selector;
    }

    public ServicePort[] getPorts() {
        return ports;
    }

    public void setPorts(ServicePort[] ports) {
        this.ports = ports;
    }
}
