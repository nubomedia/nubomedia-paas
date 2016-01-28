package org.project.openbaton.nubomedia.api.openshift;

import org.project.openbaton.nubomedia.api.openshift.json.Metadata;
import org.project.openbaton.nubomedia.api.openshift.json.ProjectRequest;

/**
 * Created by maa on 26.01.16.
 */
public class ProjectRequestBuilder {

    private String name;
    private String description;

    public ProjectRequestBuilder(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public ProjectRequest buildMessage(){
        Metadata metadata = new Metadata();
        metadata.setName(name);
        return new ProjectRequest(metadata, name+"-project", description);
    }
}
