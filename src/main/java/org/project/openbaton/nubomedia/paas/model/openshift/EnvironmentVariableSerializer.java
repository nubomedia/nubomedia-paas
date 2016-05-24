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

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

/**
 * Created by maa on 26.01.16.
 */
public class EnvironmentVariableSerializer extends TypeAdapter<EnviromentVariable> {

    @Override
    public void write(JsonWriter out, EnviromentVariable value) throws IOException {
        if (value.getValue() == null){
            return;
        }
        else{
            out.beginObject();
            out.name("name");
            out.value(value.getName());
            out.name("value");
            out.value(value.getValue());
            out.endObject();
        }
    }

    @Override
    public EnviromentVariable read(JsonReader in) throws IOException {

        EnviromentVariable env = new EnviromentVariable();

        in.beginObject();
        while (in.hasNext()){
            if (in.nextName().equals("name")){
                env.setName(in.nextString());
            }
            if (in.nextName().equals("value")){
                env.setValue(in.nextString());
            }
        }
        in.endObject();

        return env;
    }
}
