package org.project.openbaton.nubomedia.api;

import org.project.openbaton.nubomedia.api.openshift.ConfigReader;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Properties;

/**
 * Created by maa on 29.09.15.
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@Component
public class PropertyReader implements CommandLineRunner {

    private Properties properties;

    @Override
    public void run(String... args) throws Exception {
        this.properties = ConfigReader.loadProperties();
    }

    public Properties getProperties() {
        return properties;
    }
}
