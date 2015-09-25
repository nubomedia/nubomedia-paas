package org.project.openbaton.nubomedia.api.openshift;


import com.mashape.unirest.http.HttpMethod;
import org.project.openbaton.nubomedia.api.openshift.json.MessageConfig;


public class OpenshiftRestRequest {

    private String baseURL;
    private String token; //TODO: add username and password authentication
    private String namespace;
    private String gitURL;

    public OpenshiftRestRequest(String ip, String port, String token,String namespace){
        this.baseURL = "https://" + ip + ":" + port;
        this.token = token;
        this.namespace = namespace;
    }

    public String doRequest(HttpMethod method, MessageConfig message){

    }

}
