/*
 * Copyright (c) 2015-2016 Fraunhofer FOKUS
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.project.openbaton.nubomedia.paas.core.openshift;

import com.google.gson.Gson;
import org.project.openbaton.nubomedia.paas.core.openshift.builders.MessageBuilderFactory;
import org.project.openbaton.nubomedia.paas.exceptions.openshift.UnauthorizedException;
import org.project.openbaton.nubomedia.paas.model.openshift.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by maa on 06.10.15.
 */

@Service
public class SecretManager {

    @Autowired private Gson mapper;
    @Autowired private RestTemplate template;
    private Logger logger;
    private String secretSuffix;
    private String saSuffix;

    @PostConstruct
    private void init(){
        this.logger = LoggerFactory.getLogger(this.getClass());
        this.secretSuffix = "/secrets";
        this.saSuffix = "/serviceaccounts/builder";
    }

    public String createSecret(String baseURL, String namespace, String privateKey, HttpHeaders authHeader) throws UnauthorizedException {

        SecretConfig message = MessageBuilderFactory.getSecretMessage(namespace, privateKey);
        logger.debug("message " + mapper.toJson(message,SecretConfig.class));
        HttpEntity<String> secretEntity = new HttpEntity<>(mapper.toJson(message,SecretConfig.class),authHeader);
        ResponseEntity<String> responseEntity = template.exchange(baseURL + namespace + secretSuffix, HttpMethod.POST, secretEntity, String.class);

        logger.debug("response entity " + responseEntity.toString());
        SecretConfig response = mapper.fromJson(responseEntity.getBody(), SecretConfig.class);
        String secretName = response.getMetadata().getName();

        responseEntity = this.updateBuilder(baseURL, secretName,authHeader,namespace,true);
        if(responseEntity.getStatusCode() != HttpStatus.OK) logger.debug("Error updating builder account " + response.toString());

        if(responseEntity.getStatusCode().equals(HttpStatus.UNAUTHORIZED)) {

            throw new UnauthorizedException("Invalid or expired token");
        }

        return secretName;

    }

    public HttpStatus deleteSecret(String baseURL, String secretName, String namespace, HttpHeaders authHeader) throws UnauthorizedException {
        HttpEntity<String> deleteEntity = new HttpEntity<>("",authHeader);
        ResponseEntity<String> entity = template.exchange(baseURL + namespace + secretSuffix + "/" + secretName, HttpMethod.DELETE, deleteEntity, String.class);

        if(entity.getStatusCode() != HttpStatus.OK) logger.debug("Error deleting secret " + secretName + " response " + entity.toString());

        if(entity.getStatusCode().equals(HttpStatus.UNAUTHORIZED)) {

            throw new UnauthorizedException("Invalid or expired token");
        }

        entity = this.updateBuilder(baseURL, secretName,authHeader,namespace,false);

        if(entity.getStatusCode() != HttpStatus.OK) logger.debug("Error updating builder account " + entity.toString());

        if(entity.getStatusCode().equals(HttpStatus.UNAUTHORIZED)) {

            throw new UnauthorizedException("Invalid or expired token");
        }

        return entity.getStatusCode();
    }

    //BETA!!!!
    public List<String> getSecretList(String baseURL, String namespace, HttpHeaders authHeader) throws UnauthorizedException {

        List<String> res = new ArrayList<>();
        String url = baseURL + namespace + saSuffix;
        HttpEntity<String> builderEntity = new HttpEntity<>(authHeader);
        ResponseEntity<String> serviceAccountBuilder = template.exchange(url, HttpMethod.GET, builderEntity, String.class);

        if(serviceAccountBuilder.getStatusCode() != HttpStatus.OK) logger.debug("Error updating builder account " + serviceAccountBuilder.toString());

        if(serviceAccountBuilder.getStatusCode().equals(HttpStatus.UNAUTHORIZED)) {

            throw new UnauthorizedException("Invalid or expired token");
        }

        ServiceAccount serviceAccount = mapper.fromJson(serviceAccountBuilder.getBody(),ServiceAccount.class);
        for (SecretID secretID : serviceAccount.getSecrets()){
            if(secretID.getName().contains(namespace)){
                res.add(secretID.getName());
            }
        }

        return res;
    }

    private ResponseEntity<String> updateBuilder(String baseURL, String secretName, HttpHeaders authHeader, String namespace, boolean add){
        String URL =  baseURL + namespace + saSuffix;
        HttpEntity<String> builderEntity = new HttpEntity<>(authHeader);
        ResponseEntity<String> serviceAccount = template.exchange(URL, HttpMethod.GET, builderEntity, String.class);

        logger.debug("Builder account " + serviceAccount.getBody());

        ServiceAccount builder = mapper.fromJson(serviceAccount.getBody(), ServiceAccount.class);
        List<SecretID> secrets = builder.getSecrets();
        List<SecretID> newSecrets;

        if (add){
             newSecrets = this.addSecret(secrets, secretName);

        }
        else{
            newSecrets = this.removeSecret(secrets, secretName);
        }

        builder.setSecrets(newSecrets);
        builderEntity = new HttpEntity<>(mapper.toJson(builder,ServiceAccount.class),authHeader);
        serviceAccount = template.exchange(URL,HttpMethod.PUT,builderEntity,String.class);

        logger.debug("New builder account " + serviceAccount.getBody());

        return serviceAccount;
    }

    private List<SecretID> removeSecret(List<SecretID> secrets, String secretName) {

        for(SecretID sec : secrets){
            if(sec.getName().equals(secretName))
                secrets.remove(sec);
        }

        return secrets;
    }

    private List<SecretID> addSecret(List<SecretID> secrets, String secretName) {

        secrets.add(new SecretID(secretName));

        return secrets;
    }

}
