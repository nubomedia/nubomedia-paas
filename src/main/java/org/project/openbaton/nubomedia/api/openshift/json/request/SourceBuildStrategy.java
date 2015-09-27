package org.project.openbaton.nubomedia.api.openshift.json.request;

import com.google.gson.annotations.SerializedName;

/**
 * Created by maa on 25/09/2015.
 */
public class SourceBuildStrategy implements BuildStrategy {

    private final String type = "Source";
    private SourceStrategy sourceStrategy;
    private boolean forcePull;

    public SourceBuildStrategy(){}

    public SourceBuildStrategy(SourceStrategy sourceStrategy, boolean forcePull) {
        this.sourceStrategy = sourceStrategy;
        this.forcePull = forcePull;
    }

    public String getType() {
        return type;
    }

    public SourceStrategy getSourceStrategy() {
        return sourceStrategy;
    }

    public void setSourceStrategy(SourceStrategy sourceStrategy) {
        this.sourceStrategy = sourceStrategy;
    }

    public boolean isForcePull() {
        return forcePull;
    }

    public void setForcePull(boolean forcePull) {
        this.forcePull = forcePull;
    }

    public static class SourceStrategy{

        @SerializedName("from")
        BuildElements from;

        public SourceStrategy(){
        }

        public SourceStrategy(BuildElements from) {
            this.from = from;
        }

        public BuildElements getFrom() {
            return from;
        }

        public void setFrom(BuildElements from) {
            this.from = from;
        }
    }


}
