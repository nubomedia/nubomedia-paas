package org.project.openbaton.nubomedia.api.openshift;

import org.project.openbaton.nubomedia.api.openshift.json.*;

/**
 * Created by maa on 25/09/2015.
 */
public class DeploymentMessageBuilder {

    private String name;
    private Container[] containers;
    private int replicaNumber;
    private Trigger[] triggers;
    private String strategyType;

    public DeploymentMessageBuilder(String name, Container[] containers, int replicaNumber, Trigger[] triggers, String strategyType) {
        this.name = name;
        this.containers = containers;
        this.replicaNumber = replicaNumber;
        this.triggers = triggers;
        this.strategyType = strategyType;
    }

    public DeploymentConfig buildMessage(){

        Metadata metadata = new Metadata(name + "-dc","","");
        MetadataDeploy metadeploy = new MetadataDeploy(new MetadataDeploy.Labels(name));
        Selector selector = new Selector(name);
        DeploymentConfigSpec.Strategy strategy = new DeploymentConfigSpec.Strategy(strategyType);

        DeploymentConfigSpec spec = new DeploymentConfigSpec(new Template(metadeploy, new SpecDeploy(containers)),replicaNumber,selector,triggers,strategy);

        return new DeploymentConfig(metadata,spec);
    }
}
