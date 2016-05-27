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
 * Created by maa on 08.10.15.
 */
public class OutputTypeAdapter extends TypeAdapter<Output> {
    @Override
    public void write(JsonWriter out, Output value) throws IOException {

        out.beginObject();

        if(value.getTo() != null){
            out.name("to");
            out.beginObject();
            out.name("kind");
            out.value(value.getTo().getKind());
            out.name("name");
            out.value(value.getTo().getName());
            out.endObject();
        }

        if(value.getPushSecret() != null){

            out.name("pushSecret");
            out.beginObject();
            out.name("name");
            out.value(value.getPushSecret().getName());
            out.endObject();

        }

        out.endObject();

    }

    @Override
    public Output read(JsonReader in) throws IOException {

        BuildElements be = null;
        SecretID secId = null;

        in.beginObject();
        while (in.hasNext()){
            if(in.nextName().equals("to")){
                be = readBuildElement(in);
            }
            if(in.nextName().equals("pushSecret"))
                secId = this.readSecID(in);
        }
        in.endObject();

        return new Output(be,secId);
    }

    private SecretID readSecID(JsonReader in) throws IOException {

        SecretID res = new SecretID();

        in.beginObject();
        while (in.hasNext()){
            if(in.nextName().equals("name"))
                res.setName(in.nextString());
        }
        in.endObject();

        return res;
    }

    private BuildElements readBuildElement(JsonReader in) throws IOException {

        BuildElements be = new BuildElements();
        in.beginObject();

        while (in.hasNext()){
            if (in.nextName().equals("kind"))
                be.setKind(in.nextString());
            if (in.nextName().equals("name"))
                be.setName(in.nextString());
        }

        in.endObject();
        return be;
    }
}
