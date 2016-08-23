/*
 *
 *  * Copyright (c) 2016 Open Baton
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *     http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package org.project.openbaton.nubomedia.paas.model.openshift;

import com.google.gson.annotations.SerializedName;

/**
 * Created by maa on 01.10.15.
 */
public class SecretConfig {

  private final String kind = "Secret";
  private final String apiVersion = "v1";
  private Metadata metadata;

  @SerializedName(value = "data")
  private SecretType secretType;

  public SecretConfig(Metadata metadata, SecretType secretType) {
    this.metadata = metadata;
    this.secretType = secretType;
  }

  public SecretConfig() {}

  public String getKind() {
    return kind;
  }

  public String getApiVersion() {
    return apiVersion;
  }

  public Metadata getMetadata() {
    return metadata;
  }

  public void setMetadata(Metadata metadata) {
    this.metadata = metadata;
  }

  public SecretType getSecretType() {
    return secretType;
  }

  public void setSecretType(SecretType secretType) {
    this.secretType = secretType;
  }
}
