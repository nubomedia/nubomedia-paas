package org.project.openbaton.nubomedia.api.openshift.json;

import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * Created by maa on 05.10.15.
 */
public class SourceTypeAdapter extends TypeAdapter<Source> {
    @Override
    public void write(JsonWriter out, Source value) throws IOException {

        out.beginObject();

        out.name("type");
        out.value(value.getType());

        out.name("git");
        out.beginObject();
        out.name("uri");
        out.value(value.getGit().getURI());
        out.endObject();


        if (!value.getSourceSecret().getName().equals("")){

            out.name("sourceSecret");
            out.beginObject();
            out.name("name");
            out.value(value.getSourceSecret().getName());
            out.endObject();

        }

        out.endObject();

    }

    @Override
    public Source read(JsonReader in) throws IOException {

        String type = "";
        Source.SourceSecret secret = null;
        Source.Git gitrepo = null;

        while (in.hasNext()){

            if (in.nextName().equals("type")){
                type = in.nextString();
            }

            if (in.nextName().equals("git")){
                gitrepo = readGit(in);
            }

            if (in.nextName().equals("sourceSecret")){
                secret = readSecret(in); //This could be null, but only for reading a value that is never used for buildconfig
            }

        }

        return new Source(type,gitrepo,secret);
    }

    private Source.SourceSecret readSecret(JsonReader in) throws IOException {

        in.beginObject();
        String secret = "";

        while (in.hasNext()){

            if (in.nextName().equals("name")){
                secret = in.nextString();
            }

        }

        return new Source.SourceSecret(secret);
    }

    private Source.Git readGit(JsonReader in) throws IOException {

        in.beginObject();
        String uri = "";

        while(in.hasNext()){
            if(in.nextName().equals("uri")){
                uri = in.nextString();
            }
        }


        return new Source.Git(uri);
    }
}
