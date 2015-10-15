package org.project.openbaton.nubomedia.api.openshift;

import org.project.openbaton.nubomedia.api.openshift.json.*;

/**
 * Created by maa on 25/09/2015.
 */

public class BuildMessageBuilder {

    private String name;
    private BuildStrategy bs;
    private BuildElements be;
    private String gitURL;
    private ConfigChangeTrigger[] triggers;
    private Source.SourceSecret secret;

    public BuildMessageBuilder(String name, BuildStrategy bs, BuildElements be, String gitURL, ConfigChangeTrigger[] triggers,Source.SourceSecret secret) {
        this.name = name;
        this.bs = bs;
        this.be = be;
        this.gitURL = gitURL;
        this.triggers = triggers;
        this.secret = secret;
    }


    public BuildConfig buildMessage() {

        Source src = new Source("Git",new Source.Git(gitURL),secret);
        Output output = new Output(be,null);
        BuildConfig.Spec spec = new BuildConfig.Spec(triggers,src,bs,output,new Resources());

        return new BuildConfig(new Metadata(name + "-bc","",""),spec);
    }
}
