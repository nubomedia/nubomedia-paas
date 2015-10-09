package org.project.openbaton.nubomedia.api.openshift.json;

/**
 * Created by maa on 25/09/2015.
 */
public class DockerBuildStrategy implements BuildStrategy{

    private final String type = "Docker";
    private DockerStrategy dockerStrategy;

    public static class DockerStrategy{
        EnviromentVariable[] variables;
        BuildElements from;

        public DockerStrategy(EnviromentVariable[] variables, BuildElements from) {
            this.variables = variables;
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
            return variables;
        }

        public void setVariables(EnviromentVariable[] variables) {
            this.variables = variables;
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
