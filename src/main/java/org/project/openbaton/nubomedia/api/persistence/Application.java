package org.project.openbaton.nubomedia.api.persistence;

/**
 * Created by maa on 28.09.15.
 */
public class Application {

    private String appName;
    private String projectName;
    private String openBatonID;
    private String route;

    public Application(String appName, String projectName, String openBatonID, String route) {
        this.appName = appName;
        this.projectName = projectName;
        this.openBatonID = openBatonID;
        this.route = route;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public String getOpenBatonID() {
        return openBatonID;
    }

    public void setOpenBatonID(String openBatonID) {
        this.openBatonID = openBatonID;
    }
}
