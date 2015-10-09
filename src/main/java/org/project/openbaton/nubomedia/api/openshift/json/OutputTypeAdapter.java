package org.project.openbaton.nubomedia.api.openshift.json;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

/**
 * Created by maa on 08.10.15.
 */
public class OutputTypeAdapter extends TypeAdapter<Output> {
    @Override
    public void write(JsonWriter out, Output value) throws IOException {

        out.beginObject();

        if(value.getBe() != null){
            out.name("to");
            out.beginObject();
            out.name("kind");
            out.value(value.getBe().getKind());
            out.name("name");
            out.value(value.getBe().getName());
            out.endObject();
        }

        if(value.getPushSecret() != null){

            out.name("pushSecret");
            out.beginObject();
            out.name("name");
            out.value(value.getPushSecret().getName());
            out.endObject();

        }

        out.endObject();

    }

    @Override
    public Output read(JsonReader in) throws IOException {

        BuildElements be = null;
        SecretID secId = null;

        in.beginObject();
        while (in.hasNext()){
            if(in.nextName().equals("to")){
                be = readBuildElement(in);
            }
            if(in.nextName().equals("pushSecret"))
                secId = new SecretID(in.nextString());
        }
        in.endObject();

        return new Output(be,secId);
    }

    private BuildElements readBuildElement(JsonReader in) throws IOException {

        BuildElements be = new BuildElements();
        in.beginObject();

        while (in.hasNext()){
            if (in.nextName().equals("kind"))
                be.setKind(in.nextString());
            if (in.nextName().equals("name"))
                be.setName(in.nextString());
        }

        in.endObject();
        return be;
    }
}
