package org.project.openbaton.nubomedia.api.openshift;

import org.project.openbaton.nubomedia.api.openshift.json.*;

/**
 * Created by maa on 26/09/2015.
 */
public class MessageBuilderFactory {

    public static ImageStreamConfig getImageStreamMessage(String name) {
        ImageStreamMessageBuilder ism = new ImageStreamMessageBuilder(name);
        return ism.buildMessage();
    }

    public static BuildConfig getBuilderMessage(String name, String dockerRepo, String gitURL,String secretName,String mediaServerGID, String mediaServerIP, String mediaServerPort) {
        DockerBuildStrategy.DockerStrategy ds = new DockerBuildStrategy.DockerStrategy(new EnviromentVariable[]{new EnviromentVariable("MEDIA_SERVER_GID",mediaServerGID),new EnviromentVariable("MEDIA_SERVER_IP",mediaServerIP),new EnviromentVariable("MEDIA_SERVER_PORT",mediaServerPort)},null);
        DockerBuildStrategy strategy = new DockerBuildStrategy(ds);
        Source.SourceSecret secret;

        if (secretName.equals("") || secretName.equals(null)){
            secret = null;
        }
        else{
            secret = new Source.SourceSecret(secretName);
        }

        ConfigChangeTrigger trigger = new ConfigChangeTrigger("ConfigChange");
        BuildMessageBuilder builder = new BuildMessageBuilder(name, strategy, new BuildElements("DockerImage", dockerRepo + ":latest"), gitURL, new ConfigChangeTrigger[]{trigger},secret);

        return builder.buildMessage();
    }

    public static DeploymentConfig getDeployMessage(String name, String dockerRepo, int[] ports, String[] protocols, int replicasNumber) {
        Container.Port[] cports = new Container.Port[ports.length];
        for (int i = 0; i < ports.length; i++) {
            cports[i] = new Container.Port(protocols[i], ports[i]);
        }
        Container container = new Container(name + "-cnt", dockerRepo + ":latest", cports);
        ImageChangeTrigger.ImageChangeParams params = new ImageChangeTrigger.ImageChangeParams(true,new BuildElements("ImageStream",name),new String[]{container.getName()});
        ImageChangeTrigger trigger = new ImageChangeTrigger("ImageChange",params);
        DeploymentMessageBuilder builder = new DeploymentMessageBuilder(name, new Container[]{container}, replicasNumber, new ImageChangeTrigger[]{trigger}, "Rolling");

        return builder.buildMessage();
    }

    public static ServiceConfig getServiceMessage(String name, int[] ports, int[] targetPorts, String[] protocols) {
        ServiceMessageBuilder smb = new ServiceMessageBuilder(name, protocols, ports, targetPorts);
        return smb.buildMessage();
    }

    public static RouteConfig getRouteMessage(String name, String appID) {
        RouteMessageBuilder rmb = new RouteMessageBuilder(name, appID);
        return rmb.buildMessage();
    }

    public static SecretConfig getSecretMessage(String namespace,String privateKey){
        SecretKeyMessageBuilder secretKeyMessageBuilder = new SecretKeyMessageBuilder(namespace,privateKey);
        return secretKeyMessageBuilder.buildMessage();
    }

}
