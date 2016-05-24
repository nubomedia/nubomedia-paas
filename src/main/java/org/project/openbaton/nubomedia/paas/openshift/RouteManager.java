package org.project.openbaton.nubomedia.paas.openshift;

import com.google.gson.Gson;
import org.project.openbaton.nubomedia.paas.exceptions.openshift.DuplicatedException;
import org.project.openbaton.nubomedia.paas.exceptions.openshift.UnauthorizedException;
import org.project.openbaton.nubomedia.paas.model.openshift.RouteConfig;
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
public class RouteManager {

    @Autowired private RestTemplate template;
    @Autowired private Gson mapper;
    private Logger logger;
    private String suffix;

    @PostConstruct
    private void init(){
        this.logger = LoggerFactory.getLogger(this.getClass());
        this.suffix = "/routes/";
    }

    public ResponseEntity<String> makeRoute(String baseURL, String appID, String name, String namespace, String domainName, HttpHeaders authHeader, RouteConfig routeConfig) throws DuplicatedException, UnauthorizedException {
        //RouteConfig message = MessageBuilderFactory.getRouteMessage(name, appID, domainName);

        //logger.debug("Route message " + mapper.toJson(message,RouteConfig.class));
        logger.debug("Route message " + mapper.toJson(routeConfig,RouteConfig.class));

        String URL = baseURL + namespace + suffix;
        //HttpEntity<String> routeEntity = new HttpEntity<String>(mapper.toJson(message, RouteConfig.class), authHeader);
        HttpEntity<String> routeEntity = new HttpEntity<String>(mapper.toJson(routeConfig, RouteConfig.class), authHeader);
        ResponseEntity response = template.exchange(URL, HttpMethod.POST, routeEntity, String.class);

        if(response.getStatusCode().equals(HttpStatus.CONFLICT)){
            throw new DuplicatedException("Application with " + name + " is already present");
        }

        if(response.getStatusCode().equals(HttpStatus.UNAUTHORIZED)) {

            throw new UnauthorizedException("Invalid or expired token");
        }

        logger.debug("ROUTE BODY IS " + response.getBody());

        return response;
    }

    public HttpStatus deleteRoute(String baseURL, String name, String namespace, HttpHeaders authHeader) throws UnauthorizedException {
        String URL = baseURL + namespace + suffix + name + "-route";
        HttpEntity<String> deleteEntity = new HttpEntity<>(authHeader);
        ResponseEntity<String> deleteResponse = template.exchange(URL,HttpMethod.DELETE,deleteEntity,String.class);

        if(deleteResponse.getStatusCode() != HttpStatus.OK) logger.debug("Error deleting route " + name + "-route response " + deleteResponse.toString());

        if(deleteResponse.getStatusCode().equals(HttpStatus.UNAUTHORIZED)) {

            throw new UnauthorizedException("Invalid or expired token");
        }

        return deleteResponse.getStatusCode();
    }

}
