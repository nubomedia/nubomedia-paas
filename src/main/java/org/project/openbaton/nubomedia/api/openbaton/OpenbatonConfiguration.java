package org.project.openbaton.nubomedia.api.openbaton;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.openbaton.catalogue.mano.descriptor.NetworkServiceDescriptor;
import org.openbaton.catalogue.nfvo.VimInstance;

import java.io.FileNotFoundException;
import java.io.FileReader;

/**
 * Created by maa on 13.10.15.
 */
public class OpenbatonConfiguration {


    public static VimInstance getVimInstance(){
        VimInstance vim = new VimInstance();
        try{
            Gson mapper = new GsonBuilder().create();
            FileReader jsonVIM = new FileReader("resource/vim-instance-nubomedia-internal.json");
            vim = mapper.fromJson(jsonVIM,VimInstance.class);
        } catch (FileNotFoundException e){
            e.printStackTrace();
        }

        return vim;
    }

    public static NetworkServiceDescriptor getNSD(){

        NetworkServiceDescriptor nsd = new NetworkServiceDescriptor();
        try {
            Gson mapper = new GsonBuilder().create();
            FileReader jsonNSD = new FileReader("resource/nsd-internal.json");
            nsd = mapper.fromJson(jsonNSD,NetworkServiceDescriptor.class);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return nsd;
    }

}
