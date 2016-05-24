package org.project.openbaton.nubomedia.paas.model.openshift;

/**
 * Created by maa on 15.10.15.
 */
public class ImageChangeTrigger implements Trigger{

    private String type;
    private ImageChangeParams imageChangeParams;


    public static class ImageChangeParams{
        boolean automatic;
        BuildElements from;
        String[] containerNames;

        public ImageChangeParams(boolean automatic, BuildElements from, String[] containerNames) {
            this.automatic = automatic;
            this.from = from;
            this.containerNames = containerNames;
        }

        public ImageChangeParams() {
        }

        public boolean isAutomatic() {
            return automatic;
        }

        public void setAutomatic(boolean automatic) {
            this.automatic = automatic;
        }

        public BuildElements getFrom() {
            return from;
        }

        public void setFrom(BuildElements from) {
            this.from = from;
        }

        public String[] getContainerNames() {
            return containerNames;
        }

        public void setContainerNames(String[] containerNames) {
            this.containerNames = containerNames;
        }
    }

    public ImageChangeTrigger(String type, ImageChangeParams imageChangeParams) {
        this.type = type;
        this.imageChangeParams = imageChangeParams;
    }

    public ImageChangeTrigger() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ImageChangeParams getImageChangeParams() {
        return imageChangeParams;
    }

    public void setImageChangeParams(ImageChangeParams imageChangeParams) {
        this.imageChangeParams = imageChangeParams;
    }


}
