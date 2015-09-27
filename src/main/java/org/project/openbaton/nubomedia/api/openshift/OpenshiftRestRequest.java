package org.project.openbaton.nubomedia.api.openshift;

import com.google.gson.Gson;
import org.project.openbaton.nubomedia.api.openshift.json.config.Config;
import org.project.openbaton.nubomedia.api.openshift.json.request.*;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class OpenshiftRestRequest {

    private String namespace;
    private RestTemplate template;
    private String appName;
    private Config config;
    private String gitURL;
    private Gson mapper;
    private int[] ports;
    private int[] targetPorts;
    private String[] protocols;
    private int replicasnumber;



    public OpenshiftRestRequest(String appName, String projectName,String gitURL,int[] ports,int[] targetPorts,String[] protocols, int replicasnumber){
        System.setProperty("javax.net.ssl.trustStore", "resource/openshift-keystore");
        this.template = new RestTemplate();
        this.config = ConfigReader.readConfig();
        this.appName = appName;
        this.namespace = projectName;
        this.gitURL = gitURL;
        this.mapper = new Gson();
        this.ports = ports;
        this.targetPorts = targetPorts;
        this.protocols = protocols;
        this.replicasnumber = replicasnumber;
    }

    public String buildApplication(){
        String routeURL="";
        String dockerRepo = config.getDockerRepo() + "/" + namespace + "/" + appName + "-stream";

        HttpHeaders header = new HttpHeaders();
        header.add("Authorization","Bearer " + config.getToken());
        header.setContentType(MediaType.APPLICATION_JSON);

        //TODO error and json response management, and rewrite this method
        if(makeImageStream(appName,namespace,header)==201) {
            if (makeBuild(appName, namespace, dockerRepo,header) == 201) {
                if (makeDeployment(appName, dockerRepo, ports, protocols, replicasnumber, namespace, header) == 200) {
                    if (makeService(appName, namespace, ports, targetPorts, protocols, header) == 200) {
                        if (makeRoute(appName, namespace, header) == 200) {
                            routeURL = appName + ".paas.nubomedia.eu";
                        }
                    }
                }
            }
        }
        return routeURL;
    }

    private int makeImageStream(String name,String namespace,HttpHeaders authHeader){
        ImageStreamConfig message = MessageBuilderFactory.getImageStreamMessage(name);
        String URL = config.getBaseURL() + "/oapi/v1/namespaces/" + namespace + "/imagestreams";
        HttpEntity<String> imageStreamEntity = new HttpEntity<String>(mapper.toJson(message,ImageStreamConfig.class),authHeader);
        ResponseEntity response = template.exchange(URL, HttpMethod.POST, imageStreamEntity, String.class);
        System.out.println(response.toString());
        return response.getStatusCode().value();
    }

    private int makeBuild(String name,String namespace,String dockerRepo, HttpHeaders authHeader){

        BuildConfig message = MessageBuilderFactory.getBuilderMessage(name,dockerRepo,gitURL);
        String URL = config.getBaseURL() + "/oapi/v1/namespaces/" + namespace + "/buildconfigs";
        HttpEntity<String> buildEntity = new HttpEntity<String>(mapper.toJson(message,BuildConfig.class),authHeader);
        ResponseEntity response = template.exchange(URL, HttpMethod.POST,buildEntity,String.class);
        System.out.println("Build response: " + response.toString());
        return response.getStatusCode().value();
    }

    private int makeDeployment(String name, String dockerRepo, int[] ports,String[] protocols,int repnumbers,String namespace,HttpHeaders authHeader){
        DeploymentConfig message = MessageBuilderFactory.getDeployMessage(name,dockerRepo,ports,protocols,repnumbers);
        String URL = config.getBaseURL() + "/oapi/v1/namespaces/" + namespace + "/deploymentconfigs";
        HttpEntity<String> deployEntity = new HttpEntity<String>(mapper.toJson(message,DeploymentConfig.class),authHeader);
        ResponseEntity response = template.exchange(URL,HttpMethod.POST,deployEntity,String.class);

        return response.getStatusCode().value();
    }

    private int makeService(String name,String namespace,int[] ports,int[] targetPorts,String[] protocols,HttpHeaders authHeader){
        ServiceConfig message = MessageBuilderFactory.getServiceMessage(name,ports,targetPorts,protocols);
        String URL = config.getBaseURL() + "/api/v1/namespaces" + namespace + "/services";
        HttpEntity<String> serviceEntity = new HttpEntity<String>(mapper.toJson(message,ServiceConfig.class),authHeader);
        ResponseEntity response = template.exchange(URL,HttpMethod.POST,serviceEntity,String.class);

        return response.getStatusCode().value();
    }

    private int makeRoute(String name, String namespace, HttpHeaders authHeader){
        RouteConfig message = MessageBuilderFactory.getRouteMessage(name);
        String URL = config.getBaseURL() + "/oapi/v1/namespaces" + namespace + "routes";
        HttpEntity<String> routeEntity = new HttpEntity<String>(mapper.toJson(message,RouteConfig.class),authHeader);
        ResponseEntity respone = template.exchange(URL,HttpMethod.POST,routeEntity,String.class);

        return respone.getStatusCode().value();
    }

}
