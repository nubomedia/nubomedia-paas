package org.project.openbaton.nubomedia.api.openshift.json;

import com.google.gson.annotations.SerializedName;
import org.apache.commons.codec.binary.Base64;

/**
 * Created by maa on 01.10.15.
 * @param ssh-primayKey: is the original ssh-primary key (protected by https connection)
 */
public class SshGitKeySecret implements SecretType{

    @SerializedName(value = "ssh-primarykey") private String sshPrimarykey;

    public SshGitKeySecret(String sshPrimarykey) {
        this.sshPrimarykey = Base64.encodeBase64String(sshPrimarykey.getBytes());
    }

    public SshGitKeySecret() {
    }

    public String getSshPrimarykey() {
        return sshPrimarykey;
    }

    public void setSshPrimarykey(String sshPrimarykey) {
        this.sshPrimarykey = sshPrimarykey;
    }
}
