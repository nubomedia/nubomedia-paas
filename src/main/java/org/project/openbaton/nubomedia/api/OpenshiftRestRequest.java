package org.project.openbaton.nubomedia.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.project.openbaton.nubomedia.api.openshift.ConfigReader;
import org.project.openbaton.nubomedia.api.openshift.MessageBuilderFactory;
import org.project.openbaton.nubomedia.api.openshift.json.request.*;
import org.project.openbaton.nubomedia.api.openshift.json.response.Pods;
import org.project.openbaton.nubomedia.api.openshift.json.response.PodsDeserializer;
import org.project.openbaton.nubomedia.api.openshift.json.response.Status;
import org.project.openbaton.nubomedia.api.openshift.json.response.StatusDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import javax.annotation.PostConstruct;



/**
 * Created by maa on 27/09/2015.
 */

@Service
public class OpenshiftRestRequest {

    private RestTemplate template;
    @Autowired
    private SystemStartup config;
    private Logger log;
    private Gson mapper;

    @PostConstruct
    private void init(){

        System.setProperty("javax.net.ssl.trustStore", "resource/openshift-keystore");
        this.log = LoggerFactory.getLogger(this.getClass());
        this.template = new RestTemplate();
        this.mapper = new GsonBuilder().setPrettyPrinting().registerTypeAdapter(Status.class,new StatusDeserializer()).registerTypeAdapter(Pods.class,new PodsDeserializer()).create();
    }

    public String buildApplication(String appName, String namespace,String gitURL,int[] ports,int[] targetPorts,String[] protocols, int replicasnumber){
        String routeURL="";
        String dockerRepo = config.getProperties().getProperty("dockerRepo") + "/" + namespace + "/" + appName;

        HttpHeaders header = new HttpHeaders();
        header.add("Authorization","Bearer " + config.getProperties().getProperty("token"));
        header.setContentType(MediaType.APPLICATION_JSON);

        //TODO error and json response management, and rewrite this method
//        if(makeImageStream(appName,namespace, header)==201) {
//            if (makeBuild(appName, namespace, dockerRepo, gitURL, header) == 201) {
                if (makeDeployment(appName, dockerRepo, ports, protocols, replicasnumber, namespace, header) == 201) {
                    if (makeService(appName, namespace, ports, targetPorts, protocols, header) == 201) {
                        if (makeRoute(appName, namespace, header) == 201) {
                            routeURL = appName + ".paas.nubomedia.eu";
                        }
                    }
                }
//            }
//        }
        return routeURL;
    }

    private int makeImageStream(String name,String namespace,HttpHeaders authHeader){
        ImageStreamConfig message = MessageBuilderFactory.getImageStreamMessage(name);
        String URL = config.getProperties().getProperty("baseURL") + "/oapi/v1/namespaces/" + namespace + "/imagestreams";
        HttpEntity<String> imageStreamEntity = new HttpEntity<String>(mapper.toJson(message,ImageStreamConfig.class),authHeader);
        ResponseEntity response = template.exchange(URL, HttpMethod.POST, imageStreamEntity, String.class);
        log.debug(response.toString());
        return response.getStatusCode().value();
    }

    private int makeBuild(String name,String namespace,String dockerRepo,String gitURL, HttpHeaders authHeader){

        BuildConfig message = MessageBuilderFactory.getBuilderMessage(name, dockerRepo, gitURL);
        String URL = config.getProperties().getProperty("baseURL") + "/oapi/v1/namespaces/" + namespace + "/buildconfigs";
        HttpEntity<String> buildEntity = new HttpEntity<String>(mapper.toJson(message,BuildConfig.class),authHeader);
        ResponseEntity response = template.exchange(URL, HttpMethod.POST,buildEntity,String.class);
        log.debug("Build response: " + response.toString());
        return response.getStatusCode().value();
    }

    private int makeDeployment(String name, String dockerRepo, int[] ports,String[] protocols,int repnumbers,String namespace,HttpHeaders authHeader){
        log.debug("params arg: " + name + " " + dockerRepo + " " + ports + " " + protocols + " " + repnumbers);
        DeploymentConfig message = MessageBuilderFactory.getDeployMessage(name, dockerRepo, ports, protocols, repnumbers);
        log.debug(mapper.toJson(message, DeploymentConfig.class));
        String URL = config.getProperties().getProperty("baseURL") + "/oapi/v1/namespaces/" + namespace + "/deploymentconfigs";
        HttpEntity<String> deployEntity = new HttpEntity<String>(mapper.toJson(message,DeploymentConfig.class),authHeader);
        ResponseEntity response = template.exchange(URL,HttpMethod.POST,deployEntity,String.class);
        log.debug("Deployment response: " + response);
        return response.getStatusCode().value();
    }

    private int makeService(String name,String namespace,int[] ports,int[] targetPorts,String[] protocols,HttpHeaders authHeader){
        ServiceConfig message = MessageBuilderFactory.getServiceMessage(name, ports, targetPorts, protocols);
        String URL = config.getProperties().getProperty("baseURL") + "/api/v1/namespaces/" + namespace + "/services";
        HttpEntity<String> serviceEntity = new HttpEntity<String>(mapper.toJson(message,ServiceConfig.class),authHeader);
        ResponseEntity response = template.exchange(URL,HttpMethod.POST,serviceEntity,String.class);
        log.debug("Service response: " + response);
        return response.getStatusCode().value();
    }

    private int makeRoute(String name, String namespace, HttpHeaders authHeader){
        RouteConfig message = MessageBuilderFactory.getRouteMessage(name);
        String URL = config.getProperties().getProperty("baseURL") + "/oapi/v1/namespaces/" + namespace + "/routes";
        HttpEntity<String> routeEntity = new HttpEntity<String>(mapper.toJson(message,RouteConfig.class),authHeader);
        ResponseEntity response = template.exchange(URL,HttpMethod.POST,routeEntity,String.class);
        log.debug("Deployment response: " + response);
        return response.getStatusCode().value();
    }

    public String deleteApplication(String appName, String namespace){

        HttpHeaders header = new HttpHeaders();
        header.add("Authorization","Bearer " + config.getProperties().getProperty("token"));
        HttpEntity<String> requestEntity = new HttpEntity<String>("",header);
        String openshiftBaseURL = config.getProperties().getProperty("baseURL") + "/oapi/v1/namespaces/" + namespace;
        String kubernetsBaseURL = config.getProperties().getProperty("baseURL") + "/api/v1/namespaces/" + namespace;

//        String imageURL = openshiftBaseURL + "/imagestreams/" + appName;
//        int imageStreamStatus = this.deleteResource(imageURL,deleteEntity);
//
//        String buildConfigURL = openshiftBaseURL + "/buildconfig/" + appName + "-bc";
//        int buildConfigStatus = this.deleteResource(buildConfigURL, deleteEntity);

        String replicationControllerURL = kubernetsBaseURL + "/replicationcontrollers/" + appName + "-dc-1";
        int replicationControllerStatus = this.deleteResource(replicationControllerURL,requestEntity);

        String deploymentConfigURL = openshiftBaseURL + "/deploymentconfigs/" + appName + "-dc";
        int deploymentConfigStatus = this.deleteResource(deploymentConfigURL,requestEntity);

        String podsURL = kubernetsBaseURL + "/pods";
        Pods pods = this.getPodsList(podsURL, requestEntity);
        for (String podName : pods.getPodNames()){
            this.deleteResource(podsURL + "/" + podName, requestEntity);
        }

        String serviceConfigURL = kubernetsBaseURL + "/services/" + appName + "-svc";
        int serviceStatus = this.deleteResource(serviceConfigURL, requestEntity);

        String routeConfigURL = openshiftBaseURL + "/routes/" + appName + "-route";
        int routeStatus = this.deleteResource(routeConfigURL, requestEntity);

        if (/*imageStreamStatus == 200 &&
                buildConfigStatus == 200 &&*/
                replicationControllerStatus == 200 &&
                deploymentConfigStatus == 200 &&
                serviceStatus == 200 &&
                routeStatus == 200){
            return "Application " + appName + " from project " + namespace + " succesfully deleted";
        }

        return "Application not found";

    }

    private Pods getPodsList(String podsURL, HttpEntity<String> requestEntity) {

        ResponseEntity<String> pods = template.exchange(podsURL,HttpMethod.GET,requestEntity,String.class);
        log.debug(pods.getBody());
        return mapper.fromJson(pods.getBody(),Pods.class);
    }

    private int deleteResource(String URL, HttpEntity<String> deleteEntity){
        ResponseEntity<String> responseEntity = template.exchange(URL,HttpMethod.DELETE,deleteEntity,String.class);
        return responseEntity.getStatusCode().value();
    }

}
