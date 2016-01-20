package org.project.openbaton.nubomedia.api.openshift.json;

/**
 * Created by maa on 08.10.15.
 */
public class Output {

    private BuildElements be;
    private SecretID pushSecret;

    public Output(BuildElements be, SecretID pushSecret) {
        this.be = be;
        this.pushSecret = pushSecret;
    }

    public Output() {
    }

    public BuildElements getBe() {
        return be;
    }

    public void setBe(BuildElements be) {
        this.be = be;
    }

    public SecretID getPushSecret() {
        return pushSecret;
    }

    public void setPushSecret(SecretID pushSecret) {
        this.pushSecret = pushSecret;
    }
}
