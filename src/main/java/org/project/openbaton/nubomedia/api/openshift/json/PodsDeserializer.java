package org.project.openbaton.nubomedia.api.openshift.json;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by maa on 29.09.15.
 */
public class PodsDeserializer implements JsonDeserializer<Pods>{
    @Override
    public Pods deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        List<String> podsList = new ArrayList<>();
        JsonParser parser = new JsonParser();
        JsonObject pods = parser.parse(json.toString()).getAsJsonObject();

        JsonArray items = pods.getAsJsonArray("items");

        for(JsonElement podElement: items){

            JsonObject metadata = podElement.getAsJsonObject().get("metadata").getAsJsonObject();

            podsList.add(metadata.get("name").getAsString());

        }

        return new Pods(podsList);
    }
}
