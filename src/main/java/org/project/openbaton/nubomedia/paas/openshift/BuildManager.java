package org.project.openbaton.nubomedia.paas.openshift;

import org.project.openbaton.nubomedia.paas.messages.BuildingStatus;
import org.project.openbaton.nubomedia.paas.exceptions.openshift.DuplicatedException;
import org.project.openbaton.nubomedia.paas.exceptions.openshift.UnauthorizedException;
import org.project.openbaton.nubomedia.paas.model.openshift.RouteConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * Created by maa on 07.10.15.
 */

@Service
public class BuildManager {

    @Autowired private BuildConfigManager builderManager;
    @Autowired private BuildStatusManager statusManager;
    private Logger logger;

    @PostConstruct
    public void init(){
        this.logger = LoggerFactory.getLogger(this.getClass());
    }

    public ResponseEntity<String> createBuild(String baseURL, String appName, String namespace, String gitURL, String dockerRepo, HttpHeaders authHeader, String secretName, String mediaServerGID, String mediaServerIP, String mediaServerPort, String cloudRepositoryIp, String cloudRepoPort, String cdnServerIp, RouteConfig routeConfig) throws DuplicatedException, UnauthorizedException {
        logger.info("Creating buildconfig for " + appName + " in project " + namespace + " from gitURL " + gitURL + " with secret " + secretName);
        return builderManager.createBuildConfig(baseURL,appName,namespace,dockerRepo,gitURL,authHeader,secretName,mediaServerGID, mediaServerIP, mediaServerPort, cloudRepositoryIp, cloudRepoPort, cdnServerIp, routeConfig);
    }

    public HttpStatus deleteBuild(String baseURL, String appName,String namespace, HttpHeaders authHeader) throws UnauthorizedException {
        logger.info("Deleting buildconfig for " + appName + " in project " + namespace);
        HttpStatus res = builderManager.deleteBuildConfig(baseURL, appName, namespace, authHeader);

        if(!res.is2xxSuccessful()){ logger.debug("Error deleting bc");}

        return statusManager.deleteBuilds(baseURL,appName,namespace,authHeader);
    }

    public BuildingStatus getApplicationStatus(String baseURL, String appName,String namespace, HttpHeaders authHeader) throws UnauthorizedException {
        BuildingStatus status = statusManager.getBuildStatus(baseURL,appName,namespace,authHeader);
        logger.info("Status:" + status);
        return status;
    }

    public String getBuildLogs(String baseURL, String appName,String namespace, HttpHeaders authHeader) throws UnauthorizedException {
        String logs = statusManager.retrieveLogs(baseURL,appName,namespace,authHeader);
        logger.info("Build for " + appName + "logs are " + logs);
        return logs;
    }


}
