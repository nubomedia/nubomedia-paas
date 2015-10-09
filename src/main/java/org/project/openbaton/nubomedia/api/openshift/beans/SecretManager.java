package org.project.openbaton.nubomedia.api.openshift.beans;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.project.openbaton.nubomedia.api.PropertyReader;
import org.project.openbaton.nubomedia.api.openshift.MessageBuilderFactory;
import org.project.openbaton.nubomedia.api.openshift.json.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;

/**
 * Created by maa on 06.10.15.
 */

@Service
public class SecretManager {

    @Autowired private Gson mapper;
    @Autowired private RestTemplate template;
    @Autowired private PropertyReader properties;
    private Logger logger;
    private String baseURL;
    private String secretSuffix;
    private String saSuffix;

    @PostConstruct
    private void init(){
        this.logger = LoggerFactory.getLogger(this.getClass());
        this.baseURL = properties.getProperties().getProperty("baseURL") + "/api/v1/namespaces/";
        this.secretSuffix = "/secrets";
        this.saSuffix = "/serviceaccounts/builder";
    }

    public String createSecret(String namespace, String privateKey, HttpHeaders authHeader){

        SecretConfig message = MessageBuilderFactory.getSecretMessage(namespace, privateKey);
        logger.debug("message " + mapper.toJson(message,SecretConfig.class));
        HttpEntity<String> secretEntity = new HttpEntity<>(mapper.toJson(message,SecretConfig.class),authHeader);
        ResponseEntity<String> responseEntity = template.exchange(this.baseURL + namespace + secretSuffix, HttpMethod.POST, secretEntity, String.class);

        logger.debug("response entity " + responseEntity.toString());
        SecretConfig response = mapper.fromJson(responseEntity.toString(), SecretConfig.class);
        String secretName = response.getMetadata().getName();

        responseEntity = this.updateBuilder(secretName,authHeader,namespace,true);
        if(responseEntity.getStatusCode() != HttpStatus.OK) logger.debug("Error updating builder account " + response.toString());

        return secretName;

    }

    public HttpStatus deleteSecret(String secretName, String namespace, HttpHeaders authHeader){
        HttpEntity<String> deleteEntity = new HttpEntity<>("",authHeader);
        ResponseEntity<String> entity = template.exchange(baseURL + namespace + secretSuffix + "/" + secretName, HttpMethod.DELETE, deleteEntity, String.class);

        if(entity.getStatusCode() != HttpStatus.OK) logger.debug("Error deleting secret " + secretName + " response " + entity.toString());

        entity = this.updateBuilder(secretName,authHeader,namespace,false);

        if(entity.getStatusCode() != HttpStatus.OK) logger.debug("Error updating builder account " + entity.toString());

        return entity.getStatusCode();
    }

    private ResponseEntity<String> updateBuilder(String secretName, HttpHeaders authHeader, String namespace, boolean add){
        String URL =  this.baseURL + namespace + saSuffix;
        HttpEntity<String> builderEntity = new HttpEntity<>(authHeader);
        ResponseEntity<String> serviceAccount = template.exchange(URL,HttpMethod.GET,builderEntity,String.class);

        logger.debug("Builder account " + serviceAccount.getBody());

        ServiceAccount builder = mapper.fromJson(serviceAccount.getBody(), ServiceAccount.class);
        List<SecretID> secrets = Arrays.asList(builder.getSecrets());

        if (add){
            secrets.add(new SecretID(secretName));
        }
        else{
            SecretID tobeRemoved = new SecretID(secretName);
            secrets.remove(tobeRemoved);
        }

        builder.setSecrets((SecretID[]) secrets.toArray());
        builderEntity = new HttpEntity<>(mapper.toJson(builder,ServiceAccount.class),authHeader);
        serviceAccount = template.exchange(URL,HttpMethod.PUT,builderEntity,String.class);

        logger.debug("New builder account " + serviceAccount.getBody());

        return serviceAccount;
    }

}
