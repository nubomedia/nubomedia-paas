package org.project.openbaton.nubomedia.api.openbaton;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.openbaton.catalogue.mano.descriptor.NetworkServiceDescriptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileNotFoundException;
import java.io.FileReader;

/**
 * Created by maa on 13.10.15.
 */
@Configuration
public class OpenbatonConfiguration {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Bean
    public NetworkServiceDescriptor getNSD(){

        NetworkServiceDescriptor nsd = new NetworkServiceDescriptor();
        try {
            Gson mapper = new GsonBuilder().create();
            FileReader jsonNSD = new FileReader("resource/nsd.json");
            nsd = mapper.fromJson(jsonNSD,NetworkServiceDescriptor.class);

            logger.debug("readed nsd " + nsd.toString());

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return nsd;
    }

}
