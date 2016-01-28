package org.project.openbaton.nubomedia.api.messages;

/**
 * Created by maa on 09.11.15.
 */
public class NubomediaPort {

    private int targetPort;
    private String protocol;
    private int port;

    public NubomediaPort() {
    }

    public int getTargetPort() {
        return targetPort;
    }

    public void setTargetPort(int targetPort) {
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

    @Override
    public String toString() {
        return "NubomediaPort{" +
                "targetPort=" + targetPort +
                ", protocol='" + protocol + '\'' +
                ", port=" + port +
                '}';
    }
}
