package org.project.openbaton.nubomedia.api.openshift.json;

/**
 * Created by maa on 08.10.15.
 */
public class BuildStatus {

    private String phase;
    private String startTimestamp;
    private String completitionTimestamp;
    private double duration;
    private ConfigBuild config;

    public BuildStatus() {
    }

    public BuildStatus(String phase, String startTimestamp, String completitionTimestamp, double duration, ConfigBuild config) {
        this.phase = phase;
        this.startTimestamp = startTimestamp;
        this.completitionTimestamp = completitionTimestamp;
        this.duration = duration;
        this.config = config;
    }

    public String getPhase() {
        return phase;
    }

    public void setPhase(String phase) {
        this.phase = phase;
    }

    public String getStartTimestamp() {
        return startTimestamp;
    }

    public void setStartTimestamp(String startTimestamp) {
        this.startTimestamp = startTimestamp;
    }

    public String getCompletitionTimestamp() {
        return completitionTimestamp;
    }

    public void setCompletitionTimestamp(String completitionTimestamp) {
        this.completitionTimestamp = completitionTimestamp;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public ConfigBuild getConfig() {
        return config;
    }

    public void setConfig(ConfigBuild config) {
        this.config = config;
    }
}
