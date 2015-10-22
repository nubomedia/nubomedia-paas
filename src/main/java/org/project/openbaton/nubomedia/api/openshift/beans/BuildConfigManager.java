package org.project.openbaton.nubomedia.api.openshift.beans;

import com.google.gson.Gson;
import org.project.openbaton.nubomedia.api.openshift.MessageBuilderFactory;
import org.project.openbaton.nubomedia.api.openshift.json.BuildConfig;
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
public class BuildConfigManager {

    @Autowired private RestTemplate template;
    @Autowired private Gson mapper;
    private Logger logger;
    private String suffix;

    @PostConstruct
    public void init(){
        this.logger = LoggerFactory.getLogger(this.getClass());
        this.suffix = "/buildconfigs/";
    }

    public ResponseEntity<String> createBuildConfig (String baseURL, String appName,String namespace,String dockerRepo,String gitURL, HttpHeaders authHeader, String secretName,String mediaServerGID,String mediaServerIP, String mediaServerPort){

        BuildConfig message = MessageBuilderFactory.getBuilderMessage(appName, dockerRepo, gitURL, secretName,mediaServerGID, mediaServerIP, mediaServerPort);
        logger.debug("writing message " + mapper.toJson(message,BuildConfig.class));
        String URL = baseURL + namespace + suffix;
        HttpEntity<String> buildEntity = new HttpEntity<>(mapper.toJson(message, BuildConfig.class), authHeader);
        ResponseEntity response = template.exchange(URL, HttpMethod.POST,buildEntity,String.class);
        logger.debug("Build response: " + response.toString());

        return response;
    }

    public HttpStatus deleteBuildConfig(String baseURL, String appName,String namespace, HttpHeaders authHeader){

        String URL = baseURL + namespace + suffix + appName + "-bc";
        HttpEntity<String> deleteEntity = new HttpEntity<>(authHeader);
        ResponseEntity<String> responseEntity = template.exchange(URL,HttpMethod.DELETE,deleteEntity,String.class);

        return responseEntity.getStatusCode();
    }

}
