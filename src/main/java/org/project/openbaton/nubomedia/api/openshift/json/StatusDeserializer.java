package org.project.openbaton.nubomedia.api.openshift.json;

import com.google.gson.*;

import java.lang.reflect.Type;

/**
 * Created by maa on 27/09/2015.
 */
public class StatusDeserializer implements JsonDeserializer<Status>{
    @Override
    public Status deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        JsonObject obj = json.getAsJsonObject();

        String kind = obj.get("kind").getAsString();
        String message = "";

        if(obj.has("message")){
            message = obj.get("message").getAsString();
        }

        String status = obj.get("status").getAsString();
        int code = obj.get("code").getAsInt();

        return new Status(kind,status,message,code);
    }
}
