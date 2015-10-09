package org.project.openbaton.nubomedia.api.persistence;

/**
 * Created by maa on 28.09.15.
 */
public class Application {

    private String appName;
    private String projectName;
    private String route;
    private String groupID;
    private int points;

    public Application(String appName, String projectName, String route, String groupID, int points) {
        this.appName = appName;
        this.projectName = projectName;
        this.route = route;
        this.groupID = groupID;
        this.points = points;
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

    public String getGroupID() {
        return groupID;
    }

    public void setGroupID(String groupID) {
        this.groupID = groupID;
    }


    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }
}
