package org.project.openbaton.nubomedia.paas.core;


import com.google.gson.Gson;
import org.project.openbaton.nubomedia.paas.core.openshift.*;
import org.project.openbaton.nubomedia.paas.utils.OpenshiftProperties;
import org.project.openbaton.nubomedia.paas.messages.BuildingStatus;
import org.project.openbaton.nubomedia.paas.core.openshift.builders.MessageBuilderFactory;
import org.project.openbaton.nubomedia.paas.exceptions.openshift.DuplicatedException;
import org.project.openbaton.nubomedia.paas.exceptions.openshift.UnauthorizedException;
import org.project.openbaton.nubomedia.paas.model.openshift.ImageStreamConfig;
import org.project.openbaton.nubomedia.paas.model.openshift.RouteConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.List;


/**
 * Created by maa on 27/09/2015.
 */

@Service
public class OpenshiftManager {

    @Autowired private Gson mapper;
    @Autowired private SecretManager secretManager;
    @Autowired private ImageStreamManager imageStreamManager;
    @Autowired private BuildManager buildManager;
    @Autowired private DeploymentManager deploymentManager;
    @Autowired private ServiceManager serviceManager;
    @Autowired private RouteManager routeManager;
    @Autowired private AuthenticationManager authManager;
    @ Autowired private OpenshiftProperties properties;
    private Logger logger;
    private String openshiftBaseURL;
    private String kubernetesBaseURL;


    @PostConstruct
    private void init() throws IOException {
        this.logger = LoggerFactory.getLogger(this.getClass());
        this.openshiftBaseURL = properties.getBaseURL() + "/oapi/v1/namespaces/";
        this.kubernetesBaseURL = properties.getBaseURL() + "/api/v1/namespaces/";
    }

    public String authenticate(String username, String password) throws UnauthorizedException {

        return this.authManager.authenticate(properties.getBaseURL(),username,password);

    }

    public String buildApplication(String token, String appID, String appName, String namespace,String gitURL,int[] ports,int[] targetPorts,String[] protocols, int replicasnumber, String secretName, String mediaServerGID, String vnfmIp, String vnfmPort, String cloudRepositoryIp, String cloudRepositoryPort, String cdnServerIp) throws DuplicatedException, UnauthorizedException {

        HttpHeaders creationHeader = new HttpHeaders();
        creationHeader.add("Authorization","Bearer " + token);
        creationHeader.add("Content-type","application/json");

        logger.info("Starting build app " + appName + " in project " + namespace);

        ResponseEntity<String> appBuilEntity = imageStreamManager.makeImageStream(openshiftBaseURL, appName, namespace, creationHeader);
        if(!appBuilEntity.getStatusCode().is2xxSuccessful()){
            logger.debug("Failed creation of imagestream " + appBuilEntity.toString());
            return appBuilEntity.getBody();
        }

        ImageStreamConfig isConfig = mapper.fromJson(appBuilEntity.getBody(),ImageStreamConfig.class);

        RouteConfig routeConfig = MessageBuilderFactory.getRouteMessage(appName, appID, properties.getDomainName());

        appBuilEntity = buildManager.createBuild(openshiftBaseURL, appName, namespace, gitURL, isConfig.getStatus().getDockerImageRepository(), creationHeader, secretName, mediaServerGID,vnfmIp,vnfmPort, cloudRepositoryIp, cloudRepositoryPort,cdnServerIp, routeConfig);
        if(!appBuilEntity.getStatusCode().is2xxSuccessful()){
            logger.debug("Failed creation of buildconfig " + appBuilEntity.toString());
            return appBuilEntity.getBody();
        }

        appBuilEntity = deploymentManager.makeDeployment(openshiftBaseURL, appName, isConfig.getStatus().getDockerImageRepository(), targetPorts, protocols, replicasnumber, namespace, creationHeader);
        if(!appBuilEntity.getStatusCode().is2xxSuccessful()){
            logger.debug("Failed creation of deploymentconfig " + appBuilEntity.toString());
            return appBuilEntity.getBody();
        }

        appBuilEntity = serviceManager.makeService(kubernetesBaseURL, appName, namespace, ports, targetPorts, protocols, creationHeader);
        if(!appBuilEntity.getStatusCode().is2xxSuccessful()){
            logger.debug("Failed creation of service " + appBuilEntity.toString());
            return appBuilEntity.getBody();
        }

        appBuilEntity = routeManager.makeRoute(openshiftBaseURL, appID, appName, namespace, properties.getDomainName(), creationHeader, routeConfig);
        if(!appBuilEntity.getStatusCode().is2xxSuccessful()){
            logger.debug("Failed creation of route " + appBuilEntity.toString());
            return appBuilEntity.getBody();
        }

        RouteConfig route = mapper.fromJson(appBuilEntity.getBody(),RouteConfig.class);

        return route.getSpec().getHost();

    }

    public HttpStatus deleteApplication(String token, String appName, String namespace) throws UnauthorizedException {

        HttpHeaders deleteHeader = new HttpHeaders();
        deleteHeader.add("Authorization","Bearer " + token);

        HttpStatus res = imageStreamManager.deleteImageStream(openshiftBaseURL, appName, namespace, deleteHeader);
        if (!res.is2xxSuccessful()) return res;

        res = buildManager.deleteBuild(openshiftBaseURL, appName, namespace, deleteHeader);
        if (!res.is2xxSuccessful()) return res;

        res = deploymentManager.deleteDeployment(openshiftBaseURL, appName, namespace, deleteHeader);
        if (!res.is2xxSuccessful()) return res;

        res = deploymentManager.deletePodsRC(kubernetesBaseURL, appName, namespace, deleteHeader);
        if (!res.is2xxSuccessful() && !res.equals(HttpStatus.NOT_FOUND)) return res;
        if (res.equals(HttpStatus.NOT_FOUND)) logger.debug("No Replication controller, build probably failed");

        res = serviceManager.deleteService(kubernetesBaseURL, appName, namespace, deleteHeader);
        if (!res.is2xxSuccessful()) return res;

        res = routeManager.deleteRoute(openshiftBaseURL,appName,namespace,deleteHeader);

        return res;
    }

    public String createSecret (String token, String privateKey, String namespace) throws UnauthorizedException {

        HttpHeaders authHeader = new HttpHeaders();
        authHeader.add("Authorization","Bearer " + token);
        return secretManager.createSecret(kubernetesBaseURL, namespace, privateKey, authHeader);
    }

    public HttpStatus deleteSecret(String token ,String secretName, String namespace) throws UnauthorizedException {
        HttpHeaders authHeader = new HttpHeaders();
        authHeader.add("Authorization","Bearer " + token);

        HttpStatus entity = secretManager.deleteSecret(kubernetesBaseURL, secretName,namespace,authHeader);
        return entity;
    }

    public BuildingStatus getStatus (String token, String appName, String namespace) throws UnauthorizedException {

        BuildingStatus res = BuildingStatus.INITIALISED;
        HttpHeaders authHeader = new HttpHeaders();
        authHeader.add("Authorization","Bearer " + token);

        BuildingStatus status = buildManager.getApplicationStatus(openshiftBaseURL, appName, namespace, authHeader);

        try {
            switch (status) {
                case BUILDING:
                    res = BuildingStatus.BUILDING;
                    break;
                case FAILED:
                    res = BuildingStatus.FAILED;
                    break;
                case BUILD_OK:
                    res = deploymentManager.getDeployStatus(kubernetesBaseURL, appName, namespace, authHeader);
                    break;
                case PAAS_RESOURCE_MISSING:
                    res = BuildingStatus.PAAS_RESOURCE_MISSING;
                    break;
            }
        }
        catch (NullPointerException e){
            res = BuildingStatus.BUILDING;
        }

        return res;
    }

    public String getApplicationLog(String token, String appName, String namespace,String podName) throws UnauthorizedException {

        HttpHeaders authHeader = new HttpHeaders();
        authHeader.add("Authorization", "Bearer " + token);
        HttpEntity<String> requestEntity = new HttpEntity<>(authHeader);

        return deploymentManager.getPodLogs(kubernetesBaseURL,namespace,appName,podName, requestEntity);
    }

    public String getBuildLogs(String token, String appName,String namespace) throws UnauthorizedException {

        HttpHeaders authHeader = new HttpHeaders();
        authHeader.add("Authorization","Bearer " + token);
        return buildManager.getBuildLogs(openshiftBaseURL, appName, namespace, authHeader);
    }

    public List<String> getPodList (String token, String appName, String namespace) throws UnauthorizedException{
        HttpHeaders authHeader = new HttpHeaders();
        authHeader.add("Authorization", "Bearer " + token);
        HttpEntity<String> requestEntity = new HttpEntity<>(authHeader);
        return deploymentManager.getPodNameList(kubernetesBaseURL,namespace,appName,requestEntity);
    }

}
