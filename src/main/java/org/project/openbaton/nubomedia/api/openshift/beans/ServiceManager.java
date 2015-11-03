package org.project.openbaton.nubomedia.api.openshift.beans;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.project.openbaton.nubomedia.api.openshift.MessageBuilderFactory;
import org.project.openbaton.nubomedia.api.openshift.exceptions.DuplicatedException;
import org.project.openbaton.nubomedia.api.openshift.json.Metadata;
import org.project.openbaton.nubomedia.api.openshift.json.MetadataTypeAdapter;
import org.project.openbaton.nubomedia.api.openshift.json.ServiceConfig;
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
public class ServiceManager {

    @Autowired private RestTemplate template;
    @Autowired private Gson mapper;
    private Logger logger;
    private String suffix;

    @PostConstruct
    private void init(){
        this.logger = LoggerFactory.getLogger(this.getClass());
        this.suffix = "/services/";
    }


    public ResponseEntity<String> makeService(String kubernetesBaseURL, String appName,String namespace,int[] ports,int[] targetPorts,String[] protocols,HttpHeaders authHeader) throws DuplicatedException {
        ServiceConfig message = MessageBuilderFactory.getServiceMessage(appName, ports, targetPorts, protocols);

        logger.debug("Writing service creation " + mapper.toJson(message,ServiceConfig.class));

        String URL = kubernetesBaseURL + namespace + suffix;
        HttpEntity<String> serviceEntity = new HttpEntity<String>(mapper.toJson(message,ServiceConfig.class),authHeader);
        ResponseEntity response = template.exchange(URL, HttpMethod.POST,serviceEntity,String.class);

        if(response.getStatusCode().equals(HttpStatus.CONFLICT)){
            throw new DuplicatedException("Application with " + appName + " is already present");
        }

        return response;
    }

    public HttpStatus deleteService(String kubernetesBaseURL, String appName, String namespace,HttpHeaders authHeader){
        String URL = kubernetesBaseURL + namespace + suffix + appName + "-svc";
        HttpEntity<String> deleteEntity = new HttpEntity<>(authHeader);
        ResponseEntity<String> deleteResponse = template.exchange(URL,HttpMethod.DELETE,deleteEntity,String.class);

        if(deleteResponse.getStatusCode() != HttpStatus.OK) logger.debug("Error deleting service " + appName + "-svc response " + deleteResponse.toString());

        return deleteResponse.getStatusCode();
    }

}
