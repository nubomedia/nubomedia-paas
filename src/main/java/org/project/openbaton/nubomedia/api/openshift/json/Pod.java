package org.project.openbaton.nubomedia.api.openshift.json;

/**
 * Created by maa on 15.10.15.
 */
public class Pod {

    private final String kind = "Pod";
    private final String apiVersion = "v1";
    private Metadata metadata;
    private PodStatus status;

    public static class PodStatus{

        private String phase;
        private String hostIP;
        private String podIP;

        public PodStatus() {
        }

        public PodStatus(String phase, String hostIP, String podIP) {
            this.phase = phase;
            this.hostIP = hostIP;
            this.podIP = podIP;
        }

        public String getPhase() {
            return phase;
        }

        public void setPhase(String phase) {
            this.phase = phase;
        }

        public String getHostIP() {
            return hostIP;
        }

        public void setHostIP(String hostIP) {
            this.hostIP = hostIP;
        }

        public String getPodIP() {
            return podIP;
        }

        public void setPodIP(String podIP) {
            this.podIP = podIP;
        }
    }

    public Pod(Metadata metadata, PodStatus status) {
        this.metadata = metadata;
        this.status = status;
    }

    public Pod() {
    }

    public String getKind() {
        return kind;
    }

    public String getApiVersion() {
        return apiVersion;
    }

    public Metadata getMetadata() {
        return metadata;
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }

    public PodStatus getStatus() {
        return status;
    }

    public void setStatus(PodStatus status) {
        this.status = status;
    }
}
