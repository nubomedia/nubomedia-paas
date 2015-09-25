package org.project.openbaton.nubomedia.api.openshift.json;

/**
 * Created by Carlo on 25/09/2015.
 */
public class DockerBuildStrategy implements BuildStrategy{

    private final String type = "Docker";
    private DockerStrategy dockerStrategy;

    static class DockerStrategy{
        BuildElements from;

        public DockerStrategy(BuildElements from) {
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
