package org.project.openbaton.nubomedia.api.openshift.beans;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.project.openbaton.nubomedia.api.openshift.MessageBuilderFactory;
import org.project.openbaton.nubomedia.api.openshift.exceptions.DuplicatedException;
import org.project.openbaton.nubomedia.api.openshift.exceptions.UnauthorizedException;
import org.project.openbaton.nubomedia.api.openshift.json.ImageStreamConfig;
import org.project.openbaton.nubomedia.api.openshift.json.Metadata;
import org.project.openbaton.nubomedia.api.openshift.json.MetadataTypeAdapter;
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
public class ImageStreamManager {

    @Autowired private RestTemplate template;
    @Autowired private Gson mapper;
    private Logger logger;
    private String suffix;

    @PostConstruct
    private void init(){
        this.logger = LoggerFactory.getLogger(this.getClass());
        this.suffix = "/imagestreams/";
    }

    public ResponseEntity<String> makeImageStream(String baseURL, String appName,String namespace,HttpHeaders authHeader) throws DuplicatedException, UnauthorizedException {
        ImageStreamConfig message = MessageBuilderFactory.getImageStreamMessage(appName);
        logger.debug("Sending message " + mapper.toJson(message,ImageStreamConfig.class));
        String URL = baseURL + namespace + suffix;
        HttpEntity<String> imageStreamEntity = new HttpEntity<String>(mapper.toJson(message,ImageStreamConfig.class),authHeader);
        ResponseEntity<String> response = null;
        try {
            response = template.exchange(URL, HttpMethod.POST, imageStreamEntity, String.class);
            logger.debug("response " + response.getBody());
        }
        catch (HttpClientErrorException e){
            logger.debug(e.getResponseBodyAsString());

            if (e.getStatusCode().equals(HttpStatus.UNPROCESSABLE_ENTITY)){
                e.printStackTrace();
            }
        }

        if(response.getStatusCode().equals(HttpStatus.CONFLICT)){
            throw new DuplicatedException("Application with " + appName + " is already present");
        }

        if(response.getStatusCode().equals(HttpStatus.UNAUTHORIZED)) {

            throw new UnauthorizedException("Invalid or expired token");
        }

        return response;
    }

    public HttpStatus deleteImageStream (String baseURL, String appName, String namespace, HttpHeaders authHeader) throws UnauthorizedException {

        String URL = baseURL + namespace + suffix + appName;
        HttpEntity<String> deleteEntity = new HttpEntity<>(authHeader);
        ResponseEntity<String> deleteResponse = template.exchange(URL,HttpMethod.DELETE,deleteEntity,String.class);

        if(deleteResponse.getStatusCode() != HttpStatus.OK) logger.debug("Error deleting imagestream for " + appName + " in project " + namespace + " response " + deleteEntity.toString());

        if(deleteResponse.getStatusCode().equals(HttpStatus.UNAUTHORIZED)) {

            throw new UnauthorizedException("Invalid or expired token");
        }

        return deleteResponse.getStatusCode();
    }

}
