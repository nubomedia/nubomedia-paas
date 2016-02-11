package org.project.openbaton.nubomedia.api.openshift;

import org.project.openbaton.nubomedia.api.openshift.json.*;

/**
 * Created by maa on 25/09/2015.
 */
public class RouteMessageBuilder {

    private String name;
    private String appID;
    private String domainName;
    private RouteTls tls;

    public RouteMessageBuilder(String name, String appID, String domainName, RouteTls tls){
        this.name = name;
        this.appID = appID;
        this.domainName = domainName;
        this.tls = tls;
    }

    public RouteConfig buildMessage() {

        Metadata metadata = new Metadata(name + "-route","","");
        BuildElements be = new BuildElements("Service",name+"-svc");
        RouteSpec spec = new RouteSpec(name+ appID + "." + domainName,be,tls,new RouteSpec.Status());

        return new RouteConfig(metadata,spec);
    }
}
