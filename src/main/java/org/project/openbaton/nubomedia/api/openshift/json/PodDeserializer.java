package org.project.openbaton.nubomedia.api.openshift.json;

import com.google.gson.*;

import java.lang.reflect.Type;

/**
 * Created by maa on 15.10.15.
 */
public class PodDeserializer implements JsonDeserializer<Pod> {
    @Override
    public Pod deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        JsonObject obj = json.getAsJsonObject();

        String phase = "", hostIP = "", podIP = "";
        Metadata metadata = context.deserialize(obj.get("metadata"),Metadata.class);

        if(obj.has("status")){
            JsonObject status = obj.get("status").getAsJsonObject();
            phase = status.get("phase").getAsString();
            hostIP = status.get("hostIP").getAsString();
            podIP = status.get("podIP").getAsString();
        }

        return new Pod(metadata,new Pod.PodStatus(phase,hostIP,podIP));
    }
}
