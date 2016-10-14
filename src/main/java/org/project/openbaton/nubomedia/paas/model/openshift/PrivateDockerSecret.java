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

import com.google.gson.JsonElement;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.codec.binary.Base64;

/**
 * Created by maa on 01.10.15.
 */
public class PrivateDockerSecret implements SecretType {

  @SerializedName(value = ".dockercfg")
  private String dockercfg;

  public PrivateDockerSecret() {}

  public PrivateDockerSecret(JsonElement jsondockercfg) {
    this.dockercfg = Base64.encodeBase64String(jsondockercfg.getAsString().getBytes());
  }

  public String getDockercfg() {
    return dockercfg;
  }

  public void setDockercfg(String dockercfg) {
    this.dockercfg = dockercfg;
  }
}
