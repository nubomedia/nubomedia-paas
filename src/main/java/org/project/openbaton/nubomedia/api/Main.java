package org.project.openbaton.nubomedia.api;

import org.project.openbaton.nubomedia.api.openshift.OpenshiftRestRequest;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Created by lto on 24/09/15.
 */
@SpringBootApplication
public class Main {


    public static void main(String[] args) {

        OpenshiftRestRequest ors = new OpenshiftRestRequest("hello-world","nubomedia","git://github.com/charliemaiors/demo-app-xsp.git  ",new int[]{7777},new int[]{7777},new String[]{"TCP"},2);
        ors.buildApplication();
        SpringApplication.run(Main.class);
    }
}
