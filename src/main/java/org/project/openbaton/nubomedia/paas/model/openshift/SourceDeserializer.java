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
