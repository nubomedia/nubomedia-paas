package org.project.openbaton.nubomedia.paas.model.openshift;

import org.apache.commons.codec.binary.Base64;

/**
 * Created by maa on 01.10.15.
 */
public class SshGitChallengeSecret implements SecretType{

    private String username;
    private String password;

    public SshGitChallengeSecret(String username, String password) {
        this.username = Base64.encodeBase64String(username.getBytes());
        this.password = Base64.encodeBase64String(password.getBytes());
    }

    public SshGitChallengeSecret() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = Base64.encodeBase64String(username.getBytes());
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = Base64.encodeBase64String(password.getBytes());
    }
}
