package org.project.openbaton.nubomedia.paas.model.openshift;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

/**
 * Created by maa on 26.01.16.
 */
public class EnvironmentVariableSerializer extends TypeAdapter<EnviromentVariable> {

    @Override
    public void write(JsonWriter out, EnviromentVariable value) throws IOException {
        if (value.getValue() == null){
            return;
        }
        else{
            out.beginObject();
            out.name("name");
            out.value(value.getName());
            out.name("value");
            out.value(value.getValue());
            out.endObject();
        }
    }

    @Override
    public EnviromentVariable read(JsonReader in) throws IOException {

        EnviromentVariable env = new EnviromentVariable();

        in.beginObject();
        while (in.hasNext()){
            if (in.nextName().equals("name")){
                env.setName(in.nextString());
            }
            if (in.nextName().equals("value")){
                env.setValue(in.nextString());
            }
        }
        in.endObject();

        return env;
    }
}
