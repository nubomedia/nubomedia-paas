package org.project.openbaton.nubomedia.paas.openshift.builders;

import org.project.openbaton.nubomedia.paas.model.openshift.Metadata;
import org.project.openbaton.nubomedia.paas.model.openshift.SecretConfig;
import org.project.openbaton.nubomedia.paas.model.openshift.SshGitKeySecret;

import java.util.UUID;

/**
 * Created by maa on 05.10.15.
 */
public class SecretKeyMessageBuilder {

    private String name;
    private String sshPrivateKey;

    public SecretKeyMessageBuilder(String name, String sshPrivateKey) {
        this.name = name;
        this.sshPrivateKey = sshPrivateKey;
    }

    public SecretConfig buildMessage(){
        UUID uuid = UUID.randomUUID();
        Metadata secretMeta = new Metadata(name + Long.toString(uuid.getLeastSignificantBits(),64) +"-secret","","");
        SshGitKeySecret secret = new SshGitKeySecret(sshPrivateKey);
        return new SecretConfig(secretMeta,secret);
    }

}
