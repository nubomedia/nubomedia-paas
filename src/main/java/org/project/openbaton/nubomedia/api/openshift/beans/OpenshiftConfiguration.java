package org.project.openbaton.nubomedia.api.openshift.beans;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.project.openbaton.nubomedia.api.openshift.ConfigReader;
import org.project.openbaton.nubomedia.api.openshift.json.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Properties;

/**
 * Created by maa on 07.10.15.
 */
@Configuration
public class OpenshiftConfiguration {

    @Bean
    public RestTemplate getRestTemplate(){
        RestTemplate res = new RestTemplate();
        res.setErrorHandler(new OpenshiftErrorHandler());
        return res;
    }

    @Bean
    public Gson getMapper(){
        return new GsonBuilder().registerTypeAdapter(Metadata.class,new MetadataTypeAdapter())
                .registerTypeAdapter(Output.class,new OutputTypeAdapter())
                .registerTypeAdapter(SecretConfig.class,new SecretDeserializer())
                .registerTypeAdapter(Source.class,new SourceTypeAdapter())
                .registerTypeAdapter(Status.class,new StatusDeserializer())
                .registerTypeAdapter(Pods.class, new PodsDeserializer()).create();
    }

}
