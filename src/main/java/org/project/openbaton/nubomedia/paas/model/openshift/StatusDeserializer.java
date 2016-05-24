/*
 * Copyright (c) 2015-2016 Fraunhofer FOKUS
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.project.openbaton.nubomedia.paas.model.openshift;

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
