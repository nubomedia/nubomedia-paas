package org.project.openbaton.nubomedia.api.openshift.beans;

import com.google.gson.Gson;
import org.project.openbaton.nubomedia.api.openshift.builders.MessageBuilderFactory;
import org.project.openbaton.nubomedia.api.openshift.exceptions.UnauthorizedException;
import org.project.openbaton.nubomedia.api.openshift.json.Project;
import org.project.openbaton.nubomedia.api.openshift.json.ProjectRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;

/**
 * Created by maa on 26.01.16.
 */
//BETA!!
@Service
public class ProjectManager {

    @Autowired private RestTemplate template;
    @Autowired private Gson mapper;
    private Logger logger;
    private String suffix;

    @PostConstruct
    private void init(){
        this.logger = LoggerFactory.getLogger(this.getClass());
        this.suffix = "/projectrequest";
    }

    public Project createProject(String authToken, String baseURL, String name) throws UnauthorizedException {
        logger.debug("Creating ProjectRequest for user " + name);
        String url = baseURL + suffix;
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization","Bearer " + authToken);
        ProjectRequest message = MessageBuilderFactory.getProjectRequest(name);
        logger.debug("This is the message " + mapper.toJson(message,ProjectRequest.class));
        HttpEntity<String> projectEntity = new HttpEntity<>(mapper.toJson(message,ProjectRequest.class),headers);
        ResponseEntity<String> projectResponse = template.exchange(url, HttpMethod.POST,projectEntity,String.class);

        if (projectResponse.getStatusCode() != HttpStatus.OK){
            logger.debug("Received not success response " + projectResponse.getBody());
        }
        if(projectResponse.getStatusCode().equals(HttpStatus.UNAUTHORIZED)){
            throw new UnauthorizedException("Get 401 response from ProjectRequest");
        }
        return mapper.fromJson(projectResponse.getBody(),Project.class);
    }

}
