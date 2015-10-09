package org.project.openbaton.nubomedia.api.openshift.json;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

/**
 * Created by maa on 01.10.15.
 */
public class MetadataTypeAdapter extends TypeAdapter<Metadata> {


    @Override
    public void write(JsonWriter out, Metadata value) throws IOException {
        out.beginObject();
        if(!value.getName().isEmpty() || value.getName() != null) {
            out.name("name");
            out.value(value.getName());
        }
        if(!value.getSelflink().isEmpty() || value.getName() != null) {
            out.name("selfLink");
            out.value(value.getSelflink());
        }
        if(!value.getResourceVersion().isEmpty() || value.getResourceVersion() != null){
            out.name("resourceVersion");
            out.value(value.getResourceVersion());
        }
        out.endObject();
    }

    @Override
    public Metadata read(JsonReader in) throws IOException {

        String name = "",selfLink = "", resourceVersion = "";
        in.beginObject();

        while(in.hasNext()){
            String nameObj = in.nextName();

            if(nameObj.equals("name")){
                name = in.nextString();
            }
            else if(nameObj.equals("selfLink")){
                selfLink = in.nextString();
            }
            else if(nameObj.equals("resourceVersion")){
                resourceVersion = in.nextString();
            }
            else{
                in.skipValue();
            }

        }

        in.endObject();

        return new Metadata(name,selfLink,resourceVersion);
    }
}
