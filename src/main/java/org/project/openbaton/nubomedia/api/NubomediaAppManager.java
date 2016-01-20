package org.project.openbaton.nubomedia.api;

import org.openbaton.catalogue.mano.record.VirtualNetworkFunctionRecord;
import org.openbaton.catalogue.nfvo.Action;
import org.openbaton.sdk.api.exception.SDKException;
import org.project.openbaton.nubomedia.api.exceptions.ApplicationNotFoundException;
import org.project.openbaton.nubomedia.api.messages.*;
import org.project.openbaton.nubomedia.api.openbaton.OpenbatonCreateServer;
import org.project.openbaton.nubomedia.api.openbaton.OpenbatonEvent;
import org.project.openbaton.nubomedia.api.openshift.exceptions.DuplicatedException;
import org.project.openbaton.nubomedia.api.openshift.exceptions.NameStructureException;
import org.project.openbaton.nubomedia.api.openshift.exceptions.UnauthorizedException;
import org.project.openbaton.nubomedia.api.persistence.Application;
import org.project.openbaton.nubomedia.api.persistence.ApplicationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.*;


/**
 * Created by maa on 28.09.15.
 */

//TODO remap all response messages with interface and codes

@RestController
@RequestMapping("/api/v1/nubomedia/paas")
public class NubomediaAppManager {

    private static Map<String, OpenbatonCreateServer> deploymentMap = new HashMap<>();
    private Logger logger;
    private SecureRandom appIDGenerator;

    @Autowired private OpenshiftManager osmanager;
    @Autowired private OpenbatonManager obmanager;
    @Autowired private ApplicationRepository appRepo;

    @PostConstruct
    private void init() {
        System.setProperty("javax.net.ssl.trustStore", "/opt/nubomedia/nubomedia-paas/resource/openshift-keystore");
        this.logger = LoggerFactory.getLogger(this.getClass());
        this.appIDGenerator = new SecureRandom();
    }

    @RequestMapping(value = "/app",  method = RequestMethod.POST)
    public @ResponseBody NubomediaCreateAppResponse createApp(@RequestHeader("Auth-Token") String token, @RequestBody NubomediaCreateAppRequest request) throws SDKException, UnauthorizedException, DuplicatedException, NameStructureException {

        if(token == null){
            throw new UnauthorizedException("no auth-token header");
        }

        if(request.getAppName().length() > 18){

            throw new NameStructureException("name is too long");

        }

        if(request.getAppName().contains(".")){

            throw new NameStructureException("name can't contains dots");

        }

        if(!appRepo.findByAppName(request.getAppName()).isEmpty()){
            throw new DuplicatedException("Application with " + request.getAppName() + " already exist");
        }

        List<String> protocols = new ArrayList<>();
        List<Integer> targetPorts = new ArrayList<>();
        List<Integer> ports = new ArrayList<>();

        for (int i = 0; i < request.getPorts().length; i++){

            protocols.add(request.getPorts()[i].getProtocol());
            targetPorts.add(request.getPorts()[i].getTargetPort());
            ports.add(request.getPorts()[i].getPort());

        }

        NubomediaCreateAppResponse res = new NubomediaCreateAppResponse();
        String appID = new BigInteger(130,appIDGenerator).toString(64);
        logger.debug("App ID " + appID + "\n");

        logger.debug("request params " + request.getAppName() + " " + request.getGitURL() + " " + request.getProjectName() + " " + ports + " " + protocols + " " + request.getReplicasNumber());

        //Openbaton MediaServer Request
        logger.info("[PAAS]: EVENT_APP_CREATE " + new Date().getTime());
        OpenbatonCreateServer openbatonCreateServer = obmanager.getMediaServerGroupID(request.getFlavor(),appID);
        openbatonCreateServer.setToken(token);

        deploymentMap.put(appID,openbatonCreateServer);

        Application persistApp = new Application(appID,request.getFlavor(),request.getAppName(),request.getProjectName(),"",openbatonCreateServer.getMediaServerID(), request.getGitURL(), targetPorts, ports, protocols,request.getReplicasNumber(),request.getSecretName(),false);
        appRepo.save(persistApp);

        res.setApp(persistApp);
        res.setCode(200);
        return res;
    }

    @RequestMapping(value = "/app/{id}", method =  RequestMethod.GET)
    public @ResponseBody Application getApp(@RequestHeader("Auth-token") String token, @PathVariable("id") String id) throws ApplicationNotFoundException, UnauthorizedException {

        logger.info("Request status for " + id + " app");

        if(token == null){
            throw new UnauthorizedException("no auth-token header");
        }

        if(!appRepo.exists(id)){
            throw new ApplicationNotFoundException("Application with ID not found");
        }

        Application app = appRepo.findFirstByAppID(id);
        logger.debug("Retrieving status for " + app.toString());

        switch (app.getStatus()){
            case CREATED:
                app.setStatus(obmanager.getStatus(app.getNsrID()));
                break;
            case INITIALIZING:
                app.setStatus(obmanager.getStatus(app.getNsrID()));
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

        appRepo.save(app);

        return app;

    }

    @RequestMapping(value = "/app/{id}/buildlogs", method = RequestMethod.GET)
    public @ResponseBody NubomediaBuildLogs getBuildLogs(@RequestHeader("Auth-token") String token, @PathVariable("id") String id) throws UnauthorizedException {

        if(token == null){
            throw new UnauthorizedException("no auth-token header");
        }

        NubomediaBuildLogs res = new NubomediaBuildLogs();

        if(!appRepo.exists(id)){
            return null;
        }

        Application app = appRepo.findFirstByAppID(id);

        if(!app.isResourceOK()){

            res.setId(id);
            res.setAppName(app.getAppName());
            res.setProjectName(app.getProjectName());
            res.setLog("Something wrong on retrieving resources");

        }

        if(app.getStatus().equals(BuildingStatus.CREATED) || app.getStatus().equals(BuildingStatus.INITIALIZING)){
            res.setId(id);
            res.setAppName(app.getAppName());
            res.setProjectName(app.getProjectName());
            res.setLog("The application is retrieving resources " + app.getStatus());

            return res;
        }

        res.setId(id);
        res.setAppName(app.getAppName());
        res.setProjectName(app.getProjectName());
        res.setLog(osmanager.getBuildLogs(token,app.getAppName(),app.getProjectName()));
        return res;
    }

    @RequestMapping(value = "/app", method = RequestMethod.GET)
    public @ResponseBody Iterable<Application> getApps(@RequestHeader("Auth-token") String token) throws UnauthorizedException {

        if(token == null){
            throw new UnauthorizedException("no auth-token header");
        }

        return this.appRepo.findAll();
    }

    @RequestMapping(value = "/app/{id}", method = RequestMethod.DELETE)
    public @ResponseBody NubomediaDeleteAppResponse deleteApp(@RequestHeader("Auth-token") String token, @PathVariable("id") String id) throws UnauthorizedException {

        if(token == null){
            throw new UnauthorizedException("no auth-token header");
        }

        logger.debug("id " + id);

        if(!appRepo.exists(id)){
            return new NubomediaDeleteAppResponse(id,"Application not found","null",404);
        }

        Application app = appRepo.findFirstByAppID(id);
        logger.debug("Deleting " + app.toString());

        if (!app.isResourceOK()){

            String name = app.getAppName();
            String projectName = app.getProjectName();
            appRepo.delete(app);
            return new NubomediaDeleteAppResponse(id,name,projectName,200);

        }

        if (app.getStatus().equals(BuildingStatus.CREATED) || app.getStatus().equals(BuildingStatus.INITIALIZING)){

            obmanager.deleteRecord(app.getNsrID());
            return new NubomediaDeleteAppResponse(id,app.getAppName(),app.getProjectName(),200);

        }

        obmanager.deleteRecord(app.getNsrID());
        HttpStatus resDelete = osmanager.deleteApplication(token, app.getAppName(), app.getProjectName());

        appRepo.delete(app);

        return new NubomediaDeleteAppResponse(id,app.getAppName(),app.getProjectName(),resDelete.value());
    }

    @RequestMapping(value = "/secret", method = RequestMethod.POST)
    public @ResponseBody String createSecret(@RequestHeader("Auth-token") String token, @RequestBody NubomediaCreateSecretRequest ncsr) throws UnauthorizedException {

        if(token == null){
            throw new UnauthorizedException("no auth-token header");
        }

        logger.debug("Creating new secret for " + ncsr.getProjectName() + " with key " + ncsr.getPrivateKey());
        return osmanager.createSecret(token, ncsr.getPrivateKey(), ncsr.getProjectName());
    }

    @RequestMapping(value = "/secret/{projectName}/{secretName}", method = RequestMethod.DELETE)
    public @ResponseBody NubomediaDeleteSecretResponse deleteSecret (@RequestHeader("Auth-token") String token, @PathVariable("secretName") String secretName, @PathVariable("projectName") String projectName) throws UnauthorizedException {

        if(token == null){
            throw new UnauthorizedException("no auth-token header");
        }

        HttpStatus deleteStatus = osmanager.deleteSecret(token, secretName, projectName);
        return new NubomediaDeleteSecretResponse(secretName,projectName,deleteStatus.value());
    }

    @RequestMapping(value = "/auth", method = RequestMethod.POST)
    public @ResponseBody NubomediaAuthorizationResponse authorize(@RequestBody NubomediaAuthorizationRequest request) throws UnauthorizedException {

        String token = osmanager.authenticate(request.getUsername(),request.getPassword());
        if (token.equals("Unauthorized")){
            return new NubomediaAuthorizationResponse(token,401);
        }
        else{
            return new NubomediaAuthorizationResponse(token,200);
        }
    }

    @RequestMapping(value = "/openbaton/{id}", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public void startOpenshiftBuild(@RequestBody OpenbatonEvent evt, @PathVariable("id") String id) throws UnauthorizedException{ //TODO fix to throw it before
        logger.debug("starting callback for appId" + id);
        logger.info("Received event " + evt);
        Application app = appRepo.findFirstByAppID(id);
        logger.debug(deploymentMap.toString());
        OpenbatonCreateServer server = deploymentMap.get(id);

        if(evt.getAction().equals(Action.INSTANTIATE_FINISH) && server.getMediaServerID().equals(evt.getPayload().getId())){
            logger.info("[PAAS]: EVENT_FINISH " + new Date().getTime());
            app.setStatus(BuildingStatus.INITIALISED);
            app.setResourceOK(true);
            appRepo.save(app);

            String vnfrID ="";

            for(VirtualNetworkFunctionRecord record : evt.getPayload().getVnfr()){

                if(record.getEndpoint().equals("media-server"))
                    vnfrID = record.getId();

            }

            logger.debug("retrieved session for " + server.getToken());
            String route = null;
            try {
                int[] ports = new int[app.getPorts().size()];
                int[] targetPorts = new int[app.getTargetPorts().size()];

                for(int i = 0; i < ports.length; i++){
                    ports[i] = app.getPorts().get(i);
                    targetPorts[i] = app.getTargetPorts().get(i);
                }

                logger.info("[PAAS]: CREATE_APP_OS " + new Date().getTime());
                route = osmanager.buildApplication(server.getToken(), app.getAppID(),app.getAppName(), app.getProjectName(), app.getGitURL(), ports, targetPorts, app.getProtocols().toArray(new String[0]), app.getReplicasNumber(), app.getSecretName(),vnfrID);
                logger.info("[PAAS]: SCHEDULED_APP_OS " + new Date().getTime());
            } catch (DuplicatedException e) {
                app.setRoute(e.getMessage());
                app.setStatus(BuildingStatus.DUPLICATED);
                appRepo.save(app);
                return;
            }
            obmanager.deleteEvent(server.getEventID());
            app.setRoute(route);
            appRepo.save(app);
            deploymentMap.remove(app.getAppID());
        }
        else if (evt.getAction().equals(Action.ERROR)){

            app.setStatus(BuildingStatus.FAILED);
            appRepo.save(app);

        }

    }

//    @Scheduled(initialDelay = 0,fixedDelay = 200)
//    public void refreshStatus() throws ApplicationNotFoundException, UnauthorizedException {
//
//        for (String id : deploymentMap.keySet()){
//            boolean writed = false;
//            OpenbatonCreateServer ocs = deploymentMap.get(id);
//            Application app = this.getApp(ocs.getToken(),id);
//            if(app.getStatus() == BuildingStatus.RUNNING && !writed){
//                logger.info("[PAAS]: APP_RUNNING " + new Date().getTime());
//                writed = true;
//            }
//        }
//
//    }

}

