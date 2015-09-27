package org.project.openbaton.nubomedia.api.openshift;

/**
 * Created by Carlo on 26/09/2015.
 */
public class Testmain {
    public static void main(String[] args){

        OpenshiftRestRequest ors = new OpenshiftRestRequest("hello-world","nubomedia","https://github.com/charliemaiors/demo-app-sti",new int[]{7777},new int[]{7777},new String[]{"TCP"},2);
        ors.buildApplication();

    }
}
