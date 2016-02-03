package org.project.openbaton.nubomedia.api.openshift;

import org.project.openbaton.nubomedia.api.openshift.json.*;

/**
 * Created by maa on 25/09/2015.
 */
public class RouteMessageBuilder {

    private String name;
    private String appID;
    private RouteTls tls;

    public RouteMessageBuilder(String name, String appID, RouteTls tls){
        this.name = name;
        this.appID = appID;
        this.tls = tls;
    }

    public RouteConfig buildMessage() {

        Metadata metadata = new Metadata(name + "-route","","");
        BuildElements be = new BuildElements("Service",name+"-svc");
        RouteSpec spec = new RouteSpec(name+ appID + ".paas.nubomedia.eu",be,tls,new RouteSpec.Status());

        return new RouteConfig(metadata,spec);
    }
}
