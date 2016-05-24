package org.project.openbaton.nubomedia.paas.model.openshift;

import com.google.gson.annotations.SerializedName;

/**
 * Created by maa on 25/09/2015.
 */
public class BuildConfig {

    private final String kind = "BuildConfig";
    private final String apiVersion = "v1";
    private Metadata metadata;
    private Spec spec;
    private BuildConfigStatus status;

    public static class Spec{
       ConfigChangeTrigger[] triggers; //TODO: add other triggers in class definition
       Source source;
       @SerializedName("strategy") BuildStrategy bs;
       Output output;
       Resources resources;

       public Spec(ConfigChangeTrigger[] triggers, Source type, BuildStrategy bs, Output output,Resources resources) {
           this.triggers = triggers;
           this.source = type;
           this.bs = bs;
           this.output = output;
           this.resources = resources;
       }

       public Spec() {
       }

       public ConfigChangeTrigger[] getTriggers() {
            return triggers;
       }

       public void setTriggers(ConfigChangeTrigger[] triggers) {
           this.triggers = triggers;
       }

       public Source getType() {
           return source;
       }

       public void setType(Source type) {
           this.source = type;
       }

       public BuildStrategy getBs() {
           return bs;
       }

       public void setBs(BuildStrategy bs) {
            this.bs = bs;
       }

       public Output getOutput() {
           return output;
       }

       public void setOutput(Output output) {
           this.output = output;
       }
    }

    public static class BuildConfigStatus{
        private String lastVersion;

        public BuildConfigStatus(String lastVersion) {
            this.lastVersion = lastVersion;
        }

        public BuildConfigStatus() {
        }

        public String getLastVersion() {
            return lastVersion;
        }

        public void setLastVersion(String lastVersion) {
            this.lastVersion = lastVersion;
        }
    }

    public BuildConfig() {
    }

    public BuildConfig(Metadata metadata, Spec spec) {
        this.metadata = metadata;
        this.spec = spec;
    }

    public String getKind() {
        return kind;
    }

    public String getApiV() {
        return apiVersion;
    }

    public Metadata getMetadata() {
        return metadata;
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }

    public Spec getSpec() {
        return spec;
    }

    public void setSpec(Spec spec) {
        this.spec = spec;
    }
}
