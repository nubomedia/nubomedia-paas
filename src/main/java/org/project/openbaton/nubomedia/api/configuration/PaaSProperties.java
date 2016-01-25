package org.project.openbaton.nubomedia.api.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

/**
 * Created by maa on 22.01.16.
 */
@Service
@ConfigurationProperties(prefix="paas")
public class PaaSProperties {

    private String internalURL;
    private String vnfmIP;
    private String vnfmPort;

    public PaaSProperties() {
    }

    public String getInternalURL() {
        return internalURL;
    }

    public void setInternalURL(String internalURL) {
        this.internalURL = internalURL;
    }

    public String getVnfmIP() {
        return vnfmIP;
    }

    public void setVnfmIP(String vnfmIP) {
        this.vnfmIP = vnfmIP;
    }

    public String getVnfmPort() {
        return vnfmPort;
    }

    public void setVnfmPort(String vnfmPort) {
        this.vnfmPort = vnfmPort;
    }
}
