package org.project.openbaton.nubomedia.api;

import org.project.openbaton.nubomedia.api.messages.*;
import org.project.openbaton.nubomedia.api.openshift.exceptions.UnauthorizedException;
import org.project.openbaton.nubomedia.api.persistence.Application;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


/**
 * Created by maa on 28.09.15.
 */

//TODO remap all response messages with interface and codes

@RestController
@RequestMapping("/api/v1/nubomedia/paas")
public class NubomediaAppManager {

    private Map<String, Application> applications;
    private Logger logger;
    private SecureRandom appIDGenerator;

    @Autowired
    private OpenshiftManager osmanager;

    @Autowired
    private OpenbatonManager obmanager;

    @PostConstruct
    private void init() {
        System.setProperty("javax.net.ssl.trustStore", "resource/openshift-keystore");
        this.applications = new HashMap<String, Application>();
        this.logger = LoggerFactory.getLogger(this.getClass());
        this.appIDGenerator = new SecureRandom();
    }

    @RequestMapping(value = "/app",  method = RequestMethod.POST)
    public @ResponseBody NubomediaCreateAppResponse createApp(@RequestHeader("Auth-Token") String token, @RequestBody NubomediaCreateAppRequest request) {

        //validate token here using oauth token 2.0 validation

        NubomediaCreateAppResponse res = new NubomediaCreateAppResponse();
        String appID = new BigInteger(130,appIDGenerator).toString(64);
        logger.debug("App ID " + appID + "\n");

        logger.debug("request params " + request.getAppName() + " " + request.getGitURL() + " " + request.getProjectName() + " " + request.getPorts() + " " + request.getProtocols() + " " + request.getReplicasNumber());


        //Openbaton MediaServer Request
        String mediaServerGID = obmanager.getMediaServerGroupID(request.getFlavor());

        //Openshift Application Creation
        String route = osmanager.buildApplication(token, appID,request.getAppName(), request.getProjectName(), request.getGitURL(), request.getPorts(), request.getTargetPorts(), request.getProtocols(), request.getReplicasNumber(), request.getSecretName(),mediaServerGID); //to be fixed with secret creation

        Application persistApp = new Application(appID,request.getFlavor(),request.getAppName(),request.getProjectName(),route,mediaServerGID,request.getGitURL(),request.getTargetPorts(),request.getPorts(),request.getProtocols(),request.getReplicasNumber(),request.getSecretName());
        applications.put(appID, persistApp);

        res.setRoute(route);
        res.setId(appID);
        res.setCode(200);
        res.setAppName(request.getAppName() + appID);
        return res;
    }

    @RequestMapping(value = "/app/{id}", method =  RequestMethod.GET)
    public @ResponseBody Application getApp(@RequestHeader("Auth-token") String token, @PathVariable("id") String id){

        //validate token here...

        logger.info("Request status for " + id + " app");

        if(!applications.containsKey(id)){
            return null;
        }

        Application app = applications.get(id);

        switch (app.getStatus()){
            case CREATED:
                app.setStatus(obmanager.getStatus(app.getGroupID()));
                break;
            case INITIALIZING:
                app.setStatus(obmanager.getStatus(app.getGroupID()));
                break;
            case INITIALISED:
                app.setStatus(osmanager.getStatus(token, app.getAppName(),app.getProjectName()));
                break;
            case BUILDING:
                app.setStatus(osmanager.getStatus(token, app.getAppName(),app.getProjectName()));
                break;
            case DEPLOYNG:
                app.setStatus(osmanager.getStatus(token, app.getAppName(),app.getProjectName()));
                break;
            case FAILED:
                app.setStatus(osmanager.getStatus(token, app.getAppName(),app.getProjectName()));
                break;
            case RUNNING:
                app.setStatus(osmanager.getStatus(token, app.getAppName(),app.getProjectName()));
                break;
        }

        return app;

    }

    @RequestMapping(value = "/app/{id}/buildlogs", method = RequestMethod.GET)
    public @ResponseBody NubomediaBuildLogs getBuildLogs(@RequestHeader("Auth-token") String token, @PathVariable("id") String id){

        NubomediaBuildLogs res = new NubomediaBuildLogs();

        if(!applications.containsKey(id)){
            return null;
        }
        Application app = applications.get(id);
        res.setId(id);
        res.setAppName(app.getAppName());
        res.setProjectName(app.getProjectName());
        res.setLog(osmanager.getBuildLogs(token,app.getAppName(),app.getProjectName()));
        return res;
    }

    @RequestMapping(value = "/app", method = RequestMethod.GET)
    public @ResponseBody Map<String, Application> getApps(){
        return this.applications;
    }

    @RequestMapping(value = "/app/{id}", method = RequestMethod.DELETE)
    public @ResponseBody NubomediaDeleteAppResponse deleteApp(@RequestHeader("Auth-token") String token, @PathVariable("id") String id) {

        logger.debug("id " + id);

        if(!applications.containsKey(id)){
            return new NubomediaDeleteAppResponse(id,"Application not found","null",404);
        }

        Application app = applications.get(id);
        applications.remove(id);

        obmanager.deleteRecord(app.getGroupID());
        HttpStatus resDelete = osmanager.deleteApplication(token, app.getAppName(), app.getProjectName());

        return new NubomediaDeleteAppResponse(id,app.getAppName(),app.getProjectName(),resDelete.value());
    }

    @RequestMapping(value = "/secret", method = RequestMethod.POST)
    public @ResponseBody String createSecret(@RequestHeader("Auth-token") String token, @RequestBody NubomediaCreateSecretRequest ncsr){
        logger.debug("Creating new secret for " + ncsr.getProjectName() + " with key " + ncsr.getPrivateKey());
        return osmanager.createSecret(token, ncsr.getPrivateKey(), ncsr.getProjectName());
    }

    @RequestMapping(value = "/secret/{projectName}/{secretName}", method = RequestMethod.DELETE)
    public @ResponseBody NubomediaDeleteSecretResponse deleteSecret (@RequestHeader("Auth-token") String token, @PathVariable("secretName") String secretName, @PathVariable("projectName") String projectName){
        HttpStatus deleteStatus = osmanager.deleteSecret(token, secretName, projectName);
        return new NubomediaDeleteSecretResponse(secretName,projectName,deleteStatus.value());
    }

    @RequestMapping(value = "/auth", method = RequestMethod.POST)
    public @ResponseBody NubomediaAuthorizationResponse authorize(@RequestBody NubomediaAuthorizationRequest request){

        String token = osmanager.authenticate(request.getUsername(),request.getPassword());
        if (token.equals("Unauthorized")){
            return new NubomediaAuthorizationResponse(token,401);
        }
        else{
            return new NubomediaAuthorizationResponse(token,200);
        }
    }

}

