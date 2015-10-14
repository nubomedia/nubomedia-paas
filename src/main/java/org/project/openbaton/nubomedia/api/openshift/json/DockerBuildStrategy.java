package org.project.openbaton.nubomedia.api.openshift.json;

/**
 * Created by maa on 25/09/2015.
 */
public class DockerBuildStrategy implements BuildStrategy{

    private final String type = "Docker";
    private DockerStrategy dockerStrategy;

    public static class DockerStrategy{
        EnviromentVariable[] env;
        BuildElements from;

        public DockerStrategy(EnviromentVariable[] variables, BuildElements from) {
            this.env = variables;
            this.from = from;
        }

        public DockerStrategy() {
        }

        public BuildElements getFrom() {
            return from;
        }

        public void setFrom(BuildElements from) {
            this.from = from;
        }

        public EnviromentVariable[] getVariables() {
            return env;
        }

        public void setVariables(EnviromentVariable[] variables) {
            this.env = variables;
        }
    }

    public DockerBuildStrategy(DockerStrategy dockerStrategy) {
        this.dockerStrategy = dockerStrategy;
    }

    public DockerBuildStrategy() {
    }

    public String getType() {
        return type;
    }

    public DockerStrategy getDockerStrategy() {
        return dockerStrategy;
    }

    public void setDockerStrategy(DockerStrategy dockerStrategy) {
        this.dockerStrategy = dockerStrategy;
    }
}
