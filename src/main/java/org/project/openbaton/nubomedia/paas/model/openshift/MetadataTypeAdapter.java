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
 * Created by maa on 01.10.15.
 */
public class MetadataTypeAdapter extends TypeAdapter<Metadata> {

  @Override
  public void write(JsonWriter out, Metadata value) throws IOException {
    out.beginObject();
    if (!value.getName().isEmpty() || value.getName() != null) {
      out.name("name");
      out.value(value.getName());
    }
    if (!value.getSelfLink().isEmpty() || value.getName() != null) {
      out.name("selfLink");
      out.value(value.getSelfLink());
    }
    if (!value.getResourceVersion().isEmpty() || value.getResourceVersion() != null) {
      out.name("resourceVersion");
      out.value(value.getResourceVersion());
    }
    out.endObject();
  }

  @Override
  public Metadata read(JsonReader in) throws IOException {

    String name = "", selfLink = "", resourceVersion = "";
    in.beginObject();

    while (in.hasNext()) {
      String nameObj = in.nextName();

      switch (nameObj) {
        case "name":
          name = in.nextString();
          break;
        case "selfLink":
          selfLink = in.nextString();
          break;
        case "resourceVersion":
          resourceVersion = in.nextString();
          break;
        default:
          in.skipValue();
          break;
      }
    }

    in.endObject();

    return new Metadata(name, selfLink, resourceVersion);
  }
}
