package org.project.openbaton.nubomedia.api.openshift.beans;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.project.openbaton.nubomedia.api.openshift.json.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;

/**
 * Created by maa on 08.10.15.
 */

@Service
@Scope("session")
public class BuildStatusManager {

    @Autowired private RestTemplate template;
    @Autowired private Gson mapper;
    private Logger logger;
    private String suffix;
    private String logSuffix;

    @PostConstruct
    private void init(){
        this.logger = LoggerFactory.getLogger(this.getClass());
        this.suffix = "/builds/";
        this.logSuffix = "/log";
    }

    //Responses: Complete, Running, Failed
    public BuildStatus getBuildStatus (String baseURL, String appName, String namespace, HttpHeaders authHeader){

        BuildStatus res = null;
        BuildList buildList = this.getBuilds(baseURL,namespace,authHeader);

        for (Build b : buildList.getItems()){
            if (b.getStatus().getConfig().getName().equals(appName + "-bc")){
                res = b.getStatus();
            }
        }

        return res;
    }

    public HttpStatus deleteBuilds(String baseURL, String appName, String namespace, HttpHeaders authHeader){

        Build target = this.retrieveBuild(baseURL,appName,namespace,authHeader);
        String URL = baseURL + namespace + suffix + target.getMetadata().getName();
        HttpEntity<String> deleteEntity = new HttpEntity<>(authHeader);
        logger.debug("Deleting " + appName + " builds of project " + namespace);
        ResponseEntity<String> responseEntity = template.exchange(URL, HttpMethod.DELETE, deleteEntity, String.class);

        if(responseEntity.getStatusCode() != HttpStatus.OK)
            logger.debug(responseEntity.toString());

        return responseEntity.getStatusCode();
    }

    public String retrieveLogs(String baseURL, String appName, String namespace, HttpHeaders authHeader){

        Build target = this.retrieveBuild(baseURL,appName,namespace,authHeader);
        String URL = baseURL + namespace + suffix + target.getMetadata().getName() + logSuffix;
        HttpEntity<String> logEntity = new HttpEntity<>(authHeader);
        ResponseEntity<String> res = template.exchange(URL,HttpMethod.GET,logEntity,String.class);

        if(res.getStatusCode() != HttpStatus.OK){
            logger.debug("Error retrieving logs " + res.getStatusCode() + " response " + res.toString());
        }

        return res.getBody();
    }

    private Build retrieveBuild(String baseURL, String appName, String namespace, HttpHeaders authHeader){

        Build res = null;
        BuildList buildList = this.getBuilds(baseURL,namespace,authHeader);

        for (Build bd : buildList.getItems()){

            if(bd.getStatus().getConfig().getName().equals(appName + "-bc")){
                res = bd;
            }

        }
        return res;
    }

    private BuildList getBuilds(String baseURL, String namespace, HttpHeaders authHeader){
        String URL = baseURL + namespace + suffix;
        HttpEntity<String> buildEntity = new HttpEntity<>(authHeader);
        ResponseEntity<String> builds = template.exchange(URL, HttpMethod.GET, buildEntity, String.class);

        if(builds.getStatusCode() != HttpStatus.OK){ logger.debug("Error retrieving builds " + builds.getStatusCode() + " response " + builds.toString());}

        return mapper.fromJson(builds.getBody(),BuildList.class);
    }

}
