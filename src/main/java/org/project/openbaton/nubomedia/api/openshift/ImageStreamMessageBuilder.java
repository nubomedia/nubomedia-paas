package org.project.openbaton.nubomedia.api.openshift;

import org.project.openbaton.nubomedia.api.openshift.json.request.ImageStreamConfig;
import org.project.openbaton.nubomedia.api.openshift.json.request.Metadata;

/**
 * Created by Carlo on 26/09/2015.
 */
public class ImageStreamMessageBuilder {

    private String name;

    public ImageStreamMessageBuilder(String name){
        this.name = name;
    }

    public ImageStreamConfig buildMessage(){
        ImageStreamConfig.ImageStreamStatus status = new ImageStreamConfig.ImageStreamStatus("");
        Metadata metadata = new Metadata(name + "-stream");
        return new ImageStreamConfig(metadata,new ImageStreamConfig.ImageStreamSpecification(),status);
    }

}
