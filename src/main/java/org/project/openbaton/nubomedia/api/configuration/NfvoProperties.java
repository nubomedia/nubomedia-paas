package org.project.openbaton.nubomedia.api.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * Created by maa on 22.01.16.
 */
@Service
@ConfigurationProperties(prefix="nfvo")
public class NfvoProperties {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private String openbatonIP;
    private String openbatonPort;
    private String openbatonUsername;
    private String openbatonPasswd;

    public String getOpenbatonIP() {
        return openbatonIP;
    }

    public void setOpenbatonIP(String openbatonIP) {
        this.openbatonIP = openbatonIP;
    }

    public String getOpenbatonPort() {
        return openbatonPort;
    }

    public void setOpenbatonPort(String openbatonPort) {
        this.openbatonPort = openbatonPort;
    }

    public String getOpenbatonUsername() {
        return openbatonUsername;
    }

    public void setOpenbatonUsername(String openbatonUsername) {
        this.openbatonUsername = openbatonUsername;
    }

    public String getOpenbatonPasswd() {
        return openbatonPasswd;
    }

    public void setOpenbatonPasswd(String openbatonPasswd) {
        this.openbatonPasswd = openbatonPasswd;
    }

    @PostConstruct
    private void init(){
        logger.debug("Openbaton IP: " + openbatonIP + "\nOpenbaton Port: " + openbatonPort + "\nOpenbaton Username: " + openbatonUsername + "\nOpenbaton Passwd: " + openbatonPasswd );
    }
}
