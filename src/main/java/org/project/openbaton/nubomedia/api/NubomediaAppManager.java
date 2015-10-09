package org.project.openbaton.nubomedia.api;

import org.project.openbaton.nubomedia.api.messages.*;
import org.project.openbaton.nubomedia.api.persistence.Application;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


/**
 * Created by maa on 28.09.15.
 */

@RestController
@RequestMapping("/api/v1/nubomedia/paas")
public class NubomediaAppManager {

    private Map<UUID, Application> applications;
    private Logger logger;

    @Autowired
    private OpenshiftManager osmanager;

    @Autowired
    private OpenbatonManager obmanager;

    @PostConstruct
    private void init() {
        System.setProperty("javax.net.ssl.trustStore", "resource/openshift-keystore");
        this.applications = new HashMap<UUID, Application>();
        this.logger = LoggerFactory.getLogger(this.getClass());
    }

    @RequestMapping(value = "/app",  method = RequestMethod.POST)
    public @ResponseBody NubomediaCreateAppResponse createApp(@RequestBody NubomediaCreateAppRequest request) {

        NubomediaCreateAppResponse res = new NubomediaCreateAppResponse();
        UUID appID = UUID.randomUUID();

        logger.debug("request params " + request.getAppName() + " " + request.getGitURL() + " " + request.getProjectName() + " " + request.getPorts() + " " + request.getProtocols() + " " + request.getReplicasNumber());


        //Openbaton MediaServer Request
        String mediaServerGID = obmanager.getMediaServerGroupID();

        //Openshift Application Creation
        String route = osmanager.buildApplication(request.getAppName() + appID, request.getProjectName(), request.getGitURL(), request.getPorts(), request.getTargetPorts(), request.getProtocols(), request.getReplicasNumber(), request.getSecretName(),mediaServerGID); //to be fixed with secret creation

        Application persistApp = new Application(appID,request.getFlavor(),request.getAppName(),request.getProjectName(),route,mediaServerGID,request.getGitURL(),request.getTargetPorts(),request.getPorts(),request.getProtocols(),request.getReplicasNumber(),request.getSecretName());
        applications.put(appID, persistApp);

        res.setRoute(route);
        res.setId(appID);
        return res;
    }

    @RequestMapping(value = "/app/{id}", method =  RequestMethod.GET)
    public @ResponseBody Application getApp(@PathVariable("id") UUID id){

        logger.info("Request status for " + id + " app");

        return applications.get(id);

    }

    @RequestMapping(value = "/app", method = RequestMethod.GET)
    public @ResponseBody Map<UUID, Application> getApps(){
        return this.applications;
    }

    @RequestMapping(value = "/app/{id}", method = RequestMethod.DELETE)
    public @ResponseBody NubomediaDeleteAppResponse deleteApp(@PathVariable("id") UUID id) {

        logger.debug("id " + id);

        Application app = applications.get(id);
        applications.remove(id);

        HttpStatus resDelete = osmanager.deleteApplication(app.getAppName(), app.getProjectName());

        return new NubomediaDeleteAppResponse(id,app.getAppName(),app.getProjectName(),resDelete.value());
    }

    @RequestMapping(value = "/secret", method = RequestMethod.POST)
    public @ResponseBody String createSecret(@RequestBody NubomediaCreateSecretRequest ncsr){
        logger.debug("Creating new secret for " + ncsr.getProjectName() + " with key " + ncsr.getPrivateKey());
        return osmanager.createSecret(ncsr.getPrivateKey(), ncsr.getProjectName());
    }

    @RequestMapping(value = "/secret/{projectName}/{secretName}", method = RequestMethod.DELETE)
    public @ResponseBody NubomediaDeleteSecretResponse deleteSecret (@PathVariable("secretName") String secretName, @PathVariable("projectName") String projectName){
        HttpStatus deleteStatus = osmanager.deleteSecret(secretName, projectName);
        return new NubomediaDeleteSecretResponse(secretName,projectName,deleteStatus.value());
    }
}

