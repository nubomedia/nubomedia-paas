package org.project.openbaton.nubomedia.paas.openshift.builders;

import org.project.openbaton.nubomedia.paas.model.openshift.Metadata;
import org.project.openbaton.nubomedia.paas.model.openshift.SecretConfig;
import org.project.openbaton.nubomedia.paas.model.openshift.SshGitChallengeSecret;

/**
 * Created by maa on 05.10.15.
 */
public class SecretChallengeMessageBuilder {

    private String name;
    private String user;
    private String password;

    public SecretChallengeMessageBuilder(String appName, String user, String password) {
        this.name = appName;
        this.user = user;
        this.password = password;
    }

    public SecretConfig buildMessage(){
        Metadata secretMeta = new Metadata(name + "-secret","","");
        SshGitChallengeSecret secret = new SshGitChallengeSecret(user, password);
        return new SecretConfig(secretMeta,secret);
    }

}
