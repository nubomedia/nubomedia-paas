package org.project.openbaton.nubomedia.api.openshift.json;

/**
 * Created by maa on 25.01.16.
 */
public class ProjectRequest {

    private final String kind = "ProjectRequest";
    private final String apiVersion = "v1";
    private Metadata metadata;
    private String displayName;
    private String description;

    public ProjectRequest(Metadata metadata, String displayName, String description) {
        this.metadata = metadata;
        this.displayName = displayName;
        this.description = description;
    }

    public ProjectRequest() {
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

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
