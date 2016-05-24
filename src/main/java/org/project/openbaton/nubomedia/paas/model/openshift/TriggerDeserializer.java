package org.project.openbaton.nubomedia.paas.model.openshift;

import com.google.gson.*;

import java.lang.reflect.Type;

/**
 * Created by maa on 15.10.15.
 */
public class TriggerDeserializer implements JsonDeserializer<Trigger>{
    @Override
    public Trigger deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        JsonObject asObj = json.getAsJsonObject();
        String type = asObj.get("type").getAsString();

        if (type.equals("ConfigChange")){
            return context.deserialize(json,ConfigChangeTrigger.class);
        }
        else{
            return context.deserialize(json,ImageChangeTrigger.class);
        }
    }
}
