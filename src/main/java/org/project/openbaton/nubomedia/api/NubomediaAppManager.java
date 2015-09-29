package org.project.openbaton.nubomedia.api;

import org.project.openbaton.nubomedia.api.messages.NubomediaCreateRequest;
import org.project.openbaton.nubomedia.api.messages.NubomediaCreateResponse;
import org.project.openbaton.nubomedia.api.persistence.Application;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by maa on 28.09.15.
 */

@RestController
@RequestMapping("/api/v1/nubomedia/paas")
public class NubomediaAppManager {

    private Map<Integer, Application> applications;
    private int id;
    private Logger logger;

    @Autowired
    private OpenshiftRestRequest osmanager;

    @Autowired
    private OpenbatonManager obmanager;
//    private int id;

    @PostConstruct
    private void init() {
        this.applications = new HashMap<Integer, Application>();
        this.id = 0;
        this.logger = LoggerFactory.getLogger(this.getClass());
    }

    @RequestMapping(value = "/create",  method = RequestMethod.POST)
    public @ResponseBody NubomediaCreateResponse createApp(@RequestBody NubomediaCreateRequest request) {

        NubomediaCreateResponse res = new NubomediaCreateResponse();

        logger.debug("request params " + request.getAppName() + " " + request.getGitURL() + " " + request.getProjectName() + " " + request.getPorts() + " " + request.getProtocols() + " " + request.getReplicasNumber());


        //Openbaton MediaServer Request

        //Openshift Application Creation
        String route = osmanager.buildApplication(request.getAppName(), request.getProjectName(), request.getGitURL(), request.getPorts(), request.getTargetPorts(), request.getProtocols(), request.getReplicasNumber());
        this.id++;

        Application persistApp = new Application(request.getAppName(), request.getProjectName(), request.getFlavor(), route);
        applications.put(id, persistApp);

        res.setRoute(route);
        res.setId(id);
        return res;
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public @ResponseBody String deleteApp(@PathVariable("id") int id) {

        logger.debug("id " + id);

        Application app = applications.get(id);
        return osmanager.deleteApplication(app.getAppName(), app.getProjectName());
    }
}

