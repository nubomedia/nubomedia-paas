package org.project.openbaton.nubomedia.paas.model.openshift;

/**
 * Created by maa on 25/09/2015.
 */
public class ImageStreamConfig {

    private final String kind = "ImageStream";
    private final String apiVersion = "v1";

    private Metadata metadata;

    private ImageStreamSpecification spec;
    private ImageStreamStatus status;

    public static class ImageStreamSpecification{
        //TODO: add imagestream spec

        public ImageStreamSpecification() {
        }
    }

    public static class ImageStreamStatus{
        String dockerImageRepository;

        public ImageStreamStatus(String dockerImageRepository) {
            this.dockerImageRepository = dockerImageRepository;
        }

        public ImageStreamStatus() {
        }

        public String getDockerImageRepository() {
            return dockerImageRepository;
        }

        public void setDockerImageRepository(String dockerImageRepository) {
            this.dockerImageRepository = dockerImageRepository;
        }
    }

    public ImageStreamConfig(Metadata metadata, ImageStreamSpecification spec, ImageStreamStatus status) {
        this.metadata = metadata;
        this.spec = spec;
        this.status = status;
    }

    public ImageStreamConfig() {
    }

    public String getKind() {
        return kind;
    }

    public String getApiVersion() {
        return apiVersion;
    }

    public Metadata getMetadata() {
        return metadata;
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }

    public ImageStreamSpecification getSpec() {
        return spec;
    }

    public void setSpec(ImageStreamSpecification spec) {
        this.spec = spec;
    }

    public ImageStreamStatus getStatus() {
        return status;
    }

    public void setStatus(ImageStreamStatus status) {
        this.status = status;
    }
}
