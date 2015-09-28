package org.project.openbaton.nubomedia.api.openshift;

import org.project.openbaton.nubomedia.api.openshift.json.request.*;

/**
 * Created by Carlo on 26/09/2015.
 */
public class MessageBuilderFactory {

    public static ImageStreamConfig getImageStreamMessage(String name){
        ImageStreamMessageBuilder ism = new ImageStreamMessageBuilder(name);
        return ism.buildMessage();
    }

    public static BuildConfig getBuilderMessage(String name, String dockerRepo, String gitURL){
        SourceBuildStrategy.SourceStrategy srcstr= new SourceBuildStrategy.SourceStrategy(new BuildElements("DockerImage","flaviomu/app-builder:v1"),true); //Hardcoded, must change based on nubomedia meeting decision
        SourceBuildStrategy strategy = new SourceBuildStrategy(srcstr);
        Trigger trigger = new Trigger("ConfigChange"); //To be decided
        BuildMessageBuilder builder = new BuildMessageBuilder(name,strategy,new BuildElements("DockerImage",dockerRepo),gitURL,new Trigger[]{trigger});

        return builder.buildMessage();
    }

    public static DeploymentConfig getDeployMessage(String name, String dockerRepo,int[] ports,String[] protocols,int replicasNumber){
        Container.Port[] cports = new Container.Port[ports.length];
        for (int i= 0; i< ports.length; i++){
            cports[i] = new Container.Port(protocols[i],ports[i]);
        }
        Container container = new Container(name+"-cnt",dockerRepo,cports);
        Trigger trigger = new Trigger("ConfigChange");
        DeploymentMessageBuilder builder = new DeploymentMessageBuilder(name,new Container[]{container},replicasNumber,new Trigger[]{trigger},"Rolling");

        return builder.buildMessage();
    }

    public static ServiceConfig getServiceMessage(String name, int[] ports, int[] targetPorts,String[] protocols){
        ServiceMessageBuilder smb = new ServiceMessageBuilder(name,protocols,ports,targetPorts);
        return smb.buildMessage();
    }

    public static RouteConfig getRouteMessage(String name){
        RouteMessageBuilder rmb = new RouteMessageBuilder(name);
        return rmb.buildMessage();
    }

}
