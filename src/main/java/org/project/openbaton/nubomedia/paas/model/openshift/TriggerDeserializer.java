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
