package org.project.openbaton.nubomedia.api.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * Created by maa on 20.01.16.
 */
@Service
@Scope
@ConfigurationProperties (prefix="openshift")
public class OpenshiftProperties {

    private Logger log = LoggerFactory.getLogger(this.getClass());
    private String baseURL;

    public String getOsBaseUrl() {
        return baseURL;
    }

    public void setOsBaseUrl(String baseURL) {
        this.baseURL = baseURL;
    }

    @PostConstruct
    private void init(){
        log.debug("Base URL for OShift is: " + baseURL);
    }
}
