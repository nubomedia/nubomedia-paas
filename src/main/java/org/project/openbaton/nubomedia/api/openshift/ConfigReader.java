package org.project.openbaton.nubomedia.api.openshift;

import com.google.gson.Gson;
import org.project.openbaton.nubomedia.api.openshift.json.config.Config;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by maa on 26/09/2015.
 */
public class ConfigReader {

    public static Config readConfig(){
        Config res = new Config();
        try {
            BufferedReader bf = new BufferedReader(new FileReader("resource/config.json"));//to be removed soon
            String jsonConfig = "",tmp;
            Gson mapper = new Gson();

            while ((tmp =  bf.readLine()) != null){
                jsonConfig += tmp;
            }

            res = mapper.fromJson(jsonConfig,Config.class);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return res;

    }

}
