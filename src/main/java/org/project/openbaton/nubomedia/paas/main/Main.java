package org.project.openbaton.nubomedia.paas.main;

import org.project.openbaton.nubomedia.paas.openshift.OpenshiftConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ContextConfiguration;

/**
 * Created by lto on 24/09/15.
 */
@SpringBootApplication
@ContextConfiguration(classes = OpenshiftConfiguration.class)
@EnableJpaRepositories ("org.project.openbaton.nubomedia.paas")
@EntityScan (basePackages = "org.project.openbaton")
@ComponentScan(basePackages = "org.project.openbaton")
// /@EnableScheduling
public class Main {


    public static void main(String[] args) {

        ApplicationContext context = SpringApplication.run(Main.class, args);
    }
}
