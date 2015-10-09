package org.project.openbaton.nubomedia.api;

import org.project.openbaton.nubomedia.api.openshift.beans.OpenshiftConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;

/**
 * Created by lto on 24/09/15.
 */
@SpringBootApplication
@ContextConfiguration(classes = OpenshiftConfiguration.class)
public class Main {


    public static void main(String[] args) {

        SpringApplication.run(Main.class);
    }
}
