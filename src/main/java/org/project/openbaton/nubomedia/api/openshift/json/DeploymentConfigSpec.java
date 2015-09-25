package org.project.openbaton.nubomedia.api.openshift.json;

import java.util.Map;

/**
 * Created by maa on 25/09/2015.
 */
public class DeploymentConfigSpec {

    private Template template;
    private DeploymentConfigSpec spec;
    private int replicas;
    private Selector selectors;
    private Trigger[] triggers;
    private Strategy strategy;

    static class Strategy{
        String type;

        public Strategy(String type) {
            this.type = type;
        }

        public Strategy() {
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }

    public DeploymentConfigSpec(Template template, DeploymentConfigSpec spec, int replicas, Selector selectors, Trigger[] triggers, Strategy strategy) {
        this.template = template;
        this.spec = spec;
        this.replicas = replicas;
        this.selectors = selectors;
        this.triggers = triggers;
        this.strategy = strategy;
    }

    public DeploymentConfigSpec() {
    }

    public Template getTemplate() {
        return template;
    }

    public void setTemplate(Template template) {
        this.template = template;
    }

    public DeploymentConfigSpec getSpec() {
        return spec;
    }

    public void setSpec(DeploymentConfigSpec spec) {
        this.spec = spec;
    }

    public int getReplicas() {
        return replicas;
    }

    public void setReplicas(int replicas) {
        this.replicas = replicas;
    }

    public Selector getSelectors() {
        return selectors;
    }

    public void setSelectors(Selector selectors) {
        this.selectors = selectors;
    }

    public Trigger[] getTriggers() {
        return triggers;
    }

    public void setTriggers(Trigger[] triggers) {
        this.triggers = triggers;
    }

    public Strategy getStrategy() {
        return strategy;
    }

    public void setStrategy(Strategy strategy) {
        this.strategy = strategy;
    }
}
