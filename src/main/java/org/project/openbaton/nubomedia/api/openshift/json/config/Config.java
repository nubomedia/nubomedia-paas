package org.project.openbaton.nubomedia.api.openshift.json.config;

/**
 * Created by Carlo on 26/09/2015.
 */
public class Config {
    //TODO: add username and password authentication
    private String baseURL;
    private String token;
    private  String dockerRepo;

    public Config() {
    }

    public Config(String baseURL, String token, String dockerRepo) {
        this.baseURL = baseURL;
        this.token = token;
        this.dockerRepo = dockerRepo;
    }

    public String getBaseURL() {
        return baseURL;
    }

    public void setBaseURL(String baseURL) {
        this.baseURL = baseURL;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getDockerRepo() {
        return dockerRepo;
    }

    public void setDockerRepo(String dockerRepo) {
        this.dockerRepo = dockerRepo;
    }
}
