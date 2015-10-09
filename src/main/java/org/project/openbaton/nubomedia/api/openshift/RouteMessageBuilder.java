package org.project.openbaton.nubomedia.api.openshift;

import org.project.openbaton.nubomedia.api.openshift.json.BuildElements;
import org.project.openbaton.nubomedia.api.openshift.json.Metadata;
import org.project.openbaton.nubomedia.api.openshift.json.RouteConfig;
import org.project.openbaton.nubomedia.api.openshift.json.RouteSpec;

/**
 * Created by maa on 25/09/2015.
 */
public class RouteMessageBuilder {

    private String name;

    public RouteMessageBuilder(String name){
        this.name = name;
    }

    public RouteConfig buildMessage() {

        Metadata metadata = new Metadata(name + "-route","","");
        BuildElements be = new BuildElements("Service",name+"-svc");
        RouteSpec spec = new RouteSpec(name+".paas.nubomedia.eu",be,new RouteSpec.Status());

        return new RouteConfig(metadata,spec);
    }
}
