package org.project.openbaton.nubomedia.api.openshift;

import org.project.openbaton.nubomedia.api.openshift.json.request.*;

/**
 * Created by maa on 25/09/2015.
 */

public class BuildMessageBuilder {

    private String name;
    private BuildStrategy bs;
    private BuildElements be;
    private String gitURL;
    private Trigger[] triggers;

    public BuildMessageBuilder(String name, BuildStrategy bs, BuildElements be, String gitURL, Trigger[] triggers) {
        this.name = name;
        this.bs = bs;
        this.be = be;
        this.gitURL = gitURL;
        this.triggers = triggers;
    }


    public BuildConfig buildMessage() {

        Source src = new Source("Git",new Source.Git(gitURL));
        BuildConfig.Output output = new BuildConfig.Output(be);
        BuildConfig.Spec spec = new BuildConfig.Spec(triggers,src,bs,output);

        return new BuildConfig(new Metadata(name + "-bc"),spec);
    }
}
