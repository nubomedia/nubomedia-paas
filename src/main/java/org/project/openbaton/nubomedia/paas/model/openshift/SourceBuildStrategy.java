package org.project.openbaton.nubomedia.paas.model.openshift;

import com.google.gson.annotations.SerializedName;

/**
 * Created by maa on 25/09/2015.
 */
public class SourceBuildStrategy implements BuildStrategy {

    private final String type = "Source";
    private SourceStrategy sourceStrategy;


    public SourceBuildStrategy(){}

    public SourceBuildStrategy(SourceStrategy sourceStrategy) {
        this.sourceStrategy = sourceStrategy;
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

    public static class SourceStrategy{

        @SerializedName("from")
        BuildElements from;
        private boolean forcePull;

        public SourceStrategy(){
        }

        public SourceStrategy(BuildElements from, boolean forcePull){
            this.from = from;
            this.forcePull = forcePull;
        }

        public boolean isForcePull() {
            return forcePull;
        }

        public void setForcePull(boolean forcePull) {
            this.forcePull = forcePull;
        }

        public BuildElements getFrom() {
            return from;
        }

        public void setFrom(BuildElements from) {
            this.from = from;
        }
    }


}
