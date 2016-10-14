/*
 *
 *  * (C) Copyright 2016 NUBOMEDIA (http://www.nubomedia.eu)
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *   http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *  *
 *
 */

package org.project.openbaton.nubomedia.paas.model.openshift;

import com.google.gson.*;

import java.lang.reflect.Type;

/**
 * Created by maa on 06.10.15.
 */
public class SecretDeserializer implements JsonDeserializer<SecretConfig> {
  @Override
  public SecretConfig deserialize(
      JsonElement json, Type typeOfT, JsonDeserializationContext context)
      throws JsonParseException {

    //TODO if will be available add other kind of secrets
    JsonObject jsonAsObj = json.getAsJsonObject();
    JsonObject metadata = jsonAsObj.get("metadata").getAsJsonObject();
    Metadata metadata1 = new Metadata(metadata.get("name").getAsString(), "", "");
    JsonObject data = jsonAsObj.get("data").getAsJsonObject();
    SshGitKeySecret secret = new SshGitKeySecret(data.get("ssh-privatekey").getAsString());

    return new SecretConfig(metadata1, secret);
  }
}
