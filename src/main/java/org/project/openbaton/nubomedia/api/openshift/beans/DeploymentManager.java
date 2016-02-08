package org.project.openbaton.nubomedia.api.openshift.beans;

import com.google.gson.Gson;
import org.project.openbaton.nubomedia.api.messages.BuildingStatus;
import org.project.openbaton.nubomedia.api.openshift.MessageBuilderFactory;
import org.project.openbaton.nubomedia.api.openshift.exceptions.DuplicatedException;
import org.project.openbaton.nubomedia.api.openshift.exceptions.UnauthorizedException;
import org.project.openbaton.nubomedia.api.openshift.json.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
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

    public ResponseEntity<String> makeDeployment(String baseURL, String appName, String dockerRepo, int[] ports,String[] protocols,int repnumbers,String namespace,HttpHeaders authHeader) throws DuplicatedException, UnauthorizedException {

        logger.debug("params arg: " + appName + " " + dockerRepo + " " + ports + " " + protocols + " " + repnumbers);
        DeploymentConfig message = MessageBuilderFactory.getDeployMessage(appName, dockerRepo, ports, protocols, repnumbers);
        logger.debug(mapper.toJson(message, DeploymentConfig.class));
        String URL = baseURL + namespace + suffix;
        HttpEntity<String> deployEntity = new HttpEntity<String>(mapper.toJson(message,DeploymentConfig.class),authHeader);
        ResponseEntity<String> response = template.exchange(URL, HttpMethod.POST,deployEntity,String.class);
        logger.debug("Deployment response: " + response);

        if(response.getStatusCode().equals(HttpStatus.CONFLICT)){
            throw new DuplicatedException("Application with " + appName + " is already present");
        }

        if(response.getStatusCode().equals(HttpStatus.UNAUTHORIZED)) {

            throw new UnauthorizedException("Invalid or expired token");
        }

        return response;
    }

    public HttpStatus deleteDeployment(String baseURL, String appName, String namespace, HttpHeaders authHeader) throws UnauthorizedException {

        logger.debug("Deleting deployment config " + appName + " of project " + namespace);
        String URL = baseURL + namespace + suffix + appName + "-dc";
        HttpEntity<String> deleteEntity = new HttpEntity<>(authHeader);
        ResponseEntity<String> res = template.exchange(URL,HttpMethod.DELETE,deleteEntity,String.class);

        if (res.getStatusCode() != HttpStatus.OK){
            logger.debug(res.toString());
        }

        if(res.getStatusCode().equals(HttpStatus.UNAUTHORIZED)) {

            throw new UnauthorizedException("Invalid or expired token");
        }

        return res.getStatusCode();
    }

    public HttpStatus deletePodsRC(String kubernetesBaseURL, String appName, String namespace, HttpHeaders authHeader) throws UnauthorizedException {

        HttpEntity<String> requestEntity = new HttpEntity<>(authHeader);
        String rcURL = kubernetesBaseURL + namespace + rcSuffix + appName + "-dc-1";

        if(!this.existRC(requestEntity,rcURL)){
            return HttpStatus.OK;
        }

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
        else if (deleteEntity.getStatusCode().equals(HttpStatus.NOT_FOUND)){ //means that you are deleting an application before the build was complete
            logger.debug("Status code " + deleteEntity.getStatusCode());
            return HttpStatus.OK;
        }

        if(deleteEntity.getStatusCode().equals(HttpStatus.UNAUTHORIZED)) {

            throw new UnauthorizedException("Invalid or expired token");
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

    public String getPodLogs(String kubernetesBaseURL, String namespace, String appName, HttpEntity<String> requestEntity) throws UnauthorizedException {
        String podsURL = kubernetesBaseURL + namespace + podSuffix;
        String targetPod = null;
        Pods podList = this.getPodsList(podsURL,requestEntity);
        logger.debug("POD LIST IS " + podList.toString());

        for(String pod : podList.getPodNames()){
            logger.debug("CURRENT POD IS " + pod);
            if (pod.contains(appName)) {
                if (!pod.contains("bc-1-build") || !pod.contains("dc-1-deploy")) {
                    targetPod = pod;
                    logger.debug("Target pod is " + targetPod);
                }
            }
        }

        String targetUrl = podsURL + targetPod + "/log";
        ResponseEntity<String> logEntity = template.exchange(targetUrl,HttpMethod.GET,requestEntity,String.class);

        if(!logEntity.getStatusCode().is2xxSuccessful()) logger.debug("FAILED TO RETRIEVE LOGS " + logEntity.getBody());

        if(logEntity.getStatusCode().equals(HttpStatus.UNAUTHORIZED)) {

            throw new UnauthorizedException("Invalid or expired token");
        }

        logger.debug("LOG IS " + logEntity.getBody());
        return logEntity.getBody();

    }

    public HorizontalPodAutoscaler createHpa (String paasUrl, String appName, String namespace, int replicasNumber, int targetPerc, HttpHeaders authHeader) throws UnauthorizedException, DuplicatedException {

        String url = paasUrl + "/apis/extensions/v1beta1/namespaces/" + namespace + "/horizontalpodautoscalers";
        HorizontalPodAutoscaler body = MessageBuilderFactory.getHpa(appName,replicasNumber,targetPerc);
        HttpEntity<String> hpaEntity = new HttpEntity<>(mapper.toJson(body,HorizontalPodAutoscaler.class),authHeader);
        ResponseEntity<String> createHpa = template.exchange(url,HttpMethod.POST,hpaEntity,String.class);

        if(createHpa.getStatusCode().equals(HttpStatus.CONFLICT)){
            throw new DuplicatedException("Application with " + appName + " is already present");
        }

        if(createHpa.getStatusCode().equals(HttpStatus.UNAUTHORIZED)) {

            throw new UnauthorizedException("Invalid or expired token");
        }

        return mapper.fromJson(createHpa.getBody(),HorizontalPodAutoscaler.class);
    }

    private Pods getPodsList(String podsURL, HttpEntity<String> requestEntity) {

        ResponseEntity<String> pods = template.exchange(podsURL,HttpMethod.GET,requestEntity,String.class);
        logger.debug(pods.getBody());
        return mapper.fromJson(pods.getBody(),Pods.class);
    }

    private boolean existRC(HttpEntity<String> reqEntity, String rcURL) throws UnauthorizedException {

        try {
            ResponseEntity<String> rcEntity = template.exchange(rcURL, HttpMethod.GET, reqEntity, String.class);
            if (rcEntity.getStatusCode() == HttpStatus.UNAUTHORIZED) throw new UnauthorizedException("Invalid Token");
            return true;
        }
        catch (HttpClientErrorException e){
            return false;
        }
    }

}
