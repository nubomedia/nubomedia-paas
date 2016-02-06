package org.project.openbaton.nubomedia.api;

import org.project.openbaton.nubomedia.api.openshift.beans.OpenshiftConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ContextConfiguration;

/**
 * Created by lto on 24/09/15.
 */
@SpringBootApplication
@ContextConfiguration(classes = OpenshiftConfiguration.class)
@EnableJpaRepositories ("org.project.openbaton.nubomedia.api")
@EntityScan (basePackages = "org.project.openbaton.nubomedia.api")
@ComponentScan
// /@EnableScheduling
public class Main {


    public static void main(String[] args) {

        SpringApplication.run(Main.class,args);
    }
}
