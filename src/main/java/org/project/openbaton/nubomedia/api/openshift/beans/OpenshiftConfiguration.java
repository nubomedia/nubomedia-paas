package org.project.openbaton.nubomedia.api.openshift.beans;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.project.openbaton.nubomedia.api.openshift.json.*;
import org.slf4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by maa on 07.10.15.
 */
@Configuration
public class OpenshiftConfiguration {

    @Bean
    public RestTemplate getRestTemplate(){
        return new RestTemplate();
    }

    @Bean
    public Gson getMapper(){
        return new GsonBuilder().registerTypeAdapter(Metadata.class, new MetadataTypeAdapter())
                .registerTypeAdapter(Output.class,new OutputTypeAdapter())
                .registerTypeAdapter(SecretConfig.class,new SecretDeserializer())
                .registerTypeAdapter(Source.class,new SourceDeserializer())
                .registerTypeAdapter(Status.class,new StatusDeserializer())
                .registerTypeAdapter(Pod.class,new PodDeserializer())
                .registerTypeAdapter(Pods.class, new PodsDeserializer())
                .registerTypeAdapter(EnviromentVariable.class,new EnvironmentVariableSerializer())
                .disableHtmlEscaping().create();
    }
}
