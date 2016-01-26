package org.project.openbaton.nubomedia.api.openshift.json;

import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

/**
 * Created by maa on 26.01.16.
 */
public class EnvironmentVariableSerializer implements JsonSerializer<EnviromentVariable> {
    @Override
    public JsonElement serialize(EnviromentVariable src, Type typeOfSrc, JsonSerializationContext context) {

        if (src.getValue() == null){
            return null;
        }
        else {
            return context.serialize(src,typeOfSrc);
        }
    }
}
