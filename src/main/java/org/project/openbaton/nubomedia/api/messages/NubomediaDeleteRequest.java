package org.project.openbaton.nubomedia.api.messages;

/**
 * Created by maa on 28.09.15.
 */
public class NubomediaDeleteRequest {

    private String appName;
    private int id;
    private String projectName;

    public NubomediaDeleteRequest() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }
}
