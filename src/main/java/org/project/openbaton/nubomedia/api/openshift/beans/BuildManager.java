package org.project.openbaton.nubomedia.api.openshift.beans;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.project.openbaton.nubomedia.api.openshift.json.BuildStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;

/**
 * Created by maa on 07.10.15.
 */

@Service
public class BuildManager {

    @Autowired private RestTemplate template;
    @Autowired private BuildConfigManager builderManager;
    @Autowired private BuildStatusManager statusManager;
    @Autowired private Gson mapper;
    private Logger logger;

    @PostConstruct
    public void init(){
        this.logger = LoggerFactory.getLogger(this.getClass());
    }

    public ResponseEntity<String> createBuild(String baseURL, String appName,String namespace,String gitURL,String dockerRepo, HttpHeaders authHeader, String secretName,String mediaServerGID){
        logger.info("Creating buildconfig for " + appName + " in project " + namespace + " from gitURL " + gitURL + " with secret " + secretName);
        return builderManager.createBuildConfig(baseURL,appName,namespace,dockerRepo,gitURL,authHeader,secretName,mediaServerGID);
    }

    public HttpStatus deleteBuild(String baseURL, String appName,String namespace, HttpHeaders authHeader){
        logger.info("Deleting buildconfig for " + appName + " in project " + namespace);
        return builderManager.deleteBuildConfig(baseURL, appName, namespace, authHeader);
    }

    public String getApplicationStatus(String baseURL, String appName,String namespace, HttpHeaders authHeader){
        BuildStatus status = statusManager.getBuildStatus(baseURL,appName,namespace,authHeader);
        logger.info(mapper.toJson(status, BuildStatus.class));
        return status.getPhase();
    }

    public String getBuildLogs(String baseURL, String appName,String namespace, HttpHeaders authHeader){
        String logs = statusManager.retrieveLogs(baseURL,appName,namespace,authHeader);
        logger.info("Build for " + appName + "logs are " + logs);
        return logs;
    }


}
