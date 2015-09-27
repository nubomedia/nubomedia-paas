package org.project.openbaton.nubomedia.api.openshift.json;

/**
 * Created by maa on 25/09/2015.
 * S2i JSon message
 */
public class Source{

    private String type;
    private Git git; //TODO: add other sources and git clone secrets


    public static class Git{
        String URI;

        public Git(String URI) {
            this.URI = URI;
        }

        public Git() {
        }

        public String getURI() {
            return URI;
        }

        public void setURI(String URI) {
            this.URI = URI;
        }
    }

    public Source(String type, Git git) {
        this.type = type;
        this.git = git;
    }

    public Source() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Git getGit() {
        return git;
    }

    public void setGit(Git git) {
        this.git = git;
    }
}
