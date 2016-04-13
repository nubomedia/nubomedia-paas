package org.project.openbaton.nubomedia.api.openshift.builders;

import org.project.openbaton.nubomedia.api.openshift.json.ImageStreamConfig;
import org.project.openbaton.nubomedia.api.openshift.json.Metadata;

/**
 * Created by maa on 26/09/2015.
 */
public class ImageStreamMessageBuilder {

    private String name;

    public ImageStreamMessageBuilder(String name){
        this.name = name;
    }

    public ImageStreamConfig buildMessage(){
        ImageStreamConfig.ImageStreamStatus status = new ImageStreamConfig.ImageStreamStatus("");
        Metadata metadata = new Metadata(name,"","");
        return new ImageStreamConfig(metadata,new ImageStreamConfig.ImageStreamSpecification(),status);
    }

}
