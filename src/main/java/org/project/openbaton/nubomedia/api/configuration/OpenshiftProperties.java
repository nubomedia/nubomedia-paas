package org.project.openbaton.nubomedia.api.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/**
 * Created by maa on 20.01.16.
 */
@Service
@ConfigurationProperties (prefix="openshift")
public class OpenshiftProperties {

    private String baseURL;
    private String domainName;

    public String getBaseURL() {
        return baseURL;
    }

    public void setBaseURL(String baseURL) {
        this.baseURL = baseURL;
    }

    public String getDomainName() {
        return domainName;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }
}
