package org.project.openbaton.nubomedia.api.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/**
 * Created by maa on 20.01.16.
 */
@Service
@Scope
@ConfigurationProperties (prefix="openshift")
public class OpenshiftProperties {

    private String baseURL;

    public String getOsBaseUrl() {
        return baseURL;
    }

    public void setOsBaseUrl(String baseURL) {
        this.baseURL = baseURL;
    }
}
