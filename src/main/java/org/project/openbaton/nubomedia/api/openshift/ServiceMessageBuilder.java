package org.project.openbaton.nubomedia.api.openshift;

import org.project.openbaton.nubomedia.api.openshift.json.Metadata;
import org.project.openbaton.nubomedia.api.openshift.json.Selector;
import org.project.openbaton.nubomedia.api.openshift.json.ServiceConfig;
import org.project.openbaton.nubomedia.api.openshift.json.ServiceSpec;

/**
 * Created by maa on 25/09/2015.
 */
public class ServiceMessageBuilder {

    private String name;
    private String[] protocols;
    private int[] targetPorts;
    private int[] ports;

    public ServiceMessageBuilder(String name, String[] protocols,int[] ports, int[] targetPorts) {
        this.name = name;
        this.protocols = protocols;
        this.targetPorts = targetPorts;
        this.ports = ports;
    }

    public ServiceConfig buildMessage() {
        ServiceSpec.ServicePort[] sPorts = new ServiceSpec.ServicePort[targetPorts.length];

        if(ports == null){
            for(int i = 0; i< targetPorts.length; i++) {
                sPorts[i] = new ServiceSpec.ServicePort(protocols[i], targetPorts[i], targetPorts[i]);
            }
        }
        else {
            for (int i = 0; i < targetPorts.length; i++) {
                sPorts[i] = new ServiceSpec.ServicePort(protocols[i], ports[i], targetPorts[i]);
            }
        }
        Metadata metadata = new Metadata(name + "-svc","","");
        Selector selector = new Selector(name);
        ServiceSpec spec = new ServiceSpec(selector,sPorts);

        return new ServiceConfig(metadata,spec);
    }
}
