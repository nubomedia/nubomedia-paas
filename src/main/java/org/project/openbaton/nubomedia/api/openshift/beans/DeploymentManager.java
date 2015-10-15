package org.project.openbaton.nubomedia.api.openshift.beans;

import com.google.gson.Gson;
import org.project.openbaton.nubomedia.api.messages.BuildingStatus;
import org.project.openbaton.nubomedia.api.openshift.MessageBuilderFactory;
import org.project.openbaton.nubomedia.api.openshift.json.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;

/**
 * Created by maa on 08.10.15.
 */

@Service
public class DeploymentManager {

    @Autowired private RestTemplate template;
    @Autowired private Gson mapper;
    private Logger logger;
    private String suffix;
    private String podSuffix;
    private String rcSuffix;

    @PostConstruct
    private void init(){
        this.logger = LoggerFactory.getLogger(this.getClass());
        this.suffix = "/deploymentconfigs/";
        this.podSuffix = "/pods/";
        this.rcSuffix = "/replicationcontrollers/";
    }

    public ResponseEntity<String> makeDeployment(String baseURL, String appName, String dockerRepo, int[] ports,String[] protocols,int repnumbers,String namespace,HttpHeaders authHeader){

        logger.debug("params arg: " + appName + " " + dockerRepo + " " + ports + " " + protocols + " " + repnumbers);
        DeploymentConfig message = MessageBuilderFactory.getDeployMessage(appName, dockerRepo, ports, protocols, repnumbers);
        logger.debug(mapper.toJson(message, DeploymentConfig.class));
        String URL = baseURL + namespace + suffix;
        HttpEntity<String> deployEntity = new HttpEntity<String>(mapper.toJson(message,DeploymentConfig.class),authHeader);
        ResponseEntity response = template.exchange(URL, HttpMethod.POST,deployEntity,String.class);
        logger.debug("Deployment response: " + response);

        return response;
    }

    public HttpStatus deleteDeployment(String baseURL, String appName, String namespace, HttpHeaders authHeader){

        logger.debug("Deleting deployment config " + appName + " of project " + namespace);
        String URL = baseURL + namespace + suffix + appName + "-dc";
        HttpEntity<String> deleteEntity = new HttpEntity<>(authHeader);
        ResponseEntity<String> res = template.exchange(URL,HttpMethod.DELETE,deleteEntity,String.class);

        if (res.getStatusCode() != HttpStatus.OK){
            logger.debug(res.toString());
        }

        return res.getStatusCode();
    }

    public HttpStatus deletePodsRC(String kubernetesBaseURL, String appName, String namespace, HttpHeaders authHeader){

        HttpEntity<String> requestEntity = new HttpEntity<>(authHeader);
        String rcURL = kubernetesBaseURL + namespace + rcSuffix + appName + "-dc-1";
        logger.debug("deleting replication controller for " + appName + " of project " + namespace);
        ResponseEntity<String> deleteEntity = template.exchange(rcURL,HttpMethod.DELETE,requestEntity,String.class);

        if(deleteEntity.getStatusCode() == HttpStatus.OK){
            String podsURL = kubernetesBaseURL + namespace + podSuffix;
            Pods pods = this.getPodsList(podsURL,requestEntity);
            for (String pod : pods.getPodNames()) {

                if (pod.contains(appName)){
                    deleteEntity = template.exchange(podsURL + pod, HttpMethod.DELETE, requestEntity, String.class);
                    if (deleteEntity.getStatusCode() != HttpStatus.OK) {
                        logger.debug("Error HTTP:" + deleteEntity.getStatusCode() + " for POD " + pod + " response " + deleteEntity.toString());
                        break;
                    }
                }
            }
        }
        else{
            logger.debug(deleteEntity.toString());
        }

        return deleteEntity.getStatusCode();
    }


    public BuildingStatus getDeployStatus(String kubernetesBaseURL, String appName, String namespace, HttpHeaders authHeader){

        BuildingStatus res = BuildingStatus.RUNNING; //if deploy pod will not exist is because the application is already deployed or the build is already failed :P
        String podsURL = kubernetesBaseURL + namespace + podSuffix;
        HttpEntity<String> podEntity = new HttpEntity<>(authHeader);
        Pods podList = this.getPodsList(podsURL,podEntity);

        for(String podName : podList.getPodNames()){
            if(podName.equals(appName + "-dc-1-deploy")){
                ResponseEntity<String> deployPod = template.exchange(podsURL+podName,HttpMethod.GET,podEntity,String.class);
                Pod targetPod = mapper.fromJson(deployPod.getBody(),Pod.class);

                switch (targetPod.getStatus().getPhase()){
                    case "Running":
                        res = BuildingStatus.DEPLOYNG;
                        break;
                    case "Failed":
                        res = BuildingStatus.FAILED;
                        break;
                    case "Complete":
                        res = BuildingStatus.RUNNING;
                        break;
                }

            }
        }

        return res;

    }

    private Pods getPodsList(String podsURL, HttpEntity<String> requestEntity) {

        ResponseEntity<String> pods = template.exchange(podsURL,HttpMethod.GET,requestEntity,String.class);
        logger.debug(pods.getBody());
        return mapper.fromJson(pods.getBody(),Pods.class);
    }

}
