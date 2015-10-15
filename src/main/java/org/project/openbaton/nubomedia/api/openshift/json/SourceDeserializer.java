package org.project.openbaton.nubomedia.api.openshift.json;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * Created by maa on 05.10.15.
 */
public class SourceDeserializer implements JsonDeserializer<Source> {

    @Override
    public Source deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        JsonObject obj = json.getAsJsonObject();
        Source.SourceSecret secret = null;
        Source.Git gitRepo = null;
        String type = "";

        if (obj.has("sourceSecret")){

            JsonObject sourceSecret = obj.getAsJsonObject("sourceSecret");
            secret = new Source.SourceSecret(sourceSecret.get("name").getAsString());

        }

        if(obj.has("git")){

            JsonObject gitSource = obj.getAsJsonObject("git");
            gitRepo = new Source.Git(gitSource.get("uri").getAsString());

        }

        if(obj.has("type")){

            type = obj.get("type").getAsString();

        }

        return new Source(type,gitRepo,secret);
    }
}
