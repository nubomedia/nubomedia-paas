package org.project.openbaton.nubomedia.api.openshift.json;

/**
 * Created by maa on 03.02.16.
 */
public class RouteTls {

    private String termination;
    private String key;
    private String certificate;
    private String caCertificate;
    private String destinationCertificate;

    public RouteTls(String termination, String key, String certificate, String caCertificate, String destinationCertificate) {
        this.termination = termination;
        this.key = key;
        this.certificate = certificate;
        this.caCertificate = caCertificate;
        this.destinationCertificate = destinationCertificate;
    }

    public RouteTls() {
    }

    public String getTermination() {
        return termination;
    }

    public void setTermination(String termination) {
        this.termination = termination;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getCertificate() {
        return certificate;
    }

    public void setCertificate(String certificate) {
        this.certificate = certificate;
    }

    public String getCaCertificate() {
        return caCertificate;
    }

    public void setCaCertificate(String caCertificate) {
        this.caCertificate = caCertificate;
    }

    public String getDestinationCertificate() {
        return destinationCertificate;
    }

    public void setDestinationCertificate(String destinationCertificate) {
        this.destinationCertificate = destinationCertificate;
    }
}
