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

import java.util.List;

/**
 * Created by maa on 01.10.15.
 */
public class ServiceAccount {

  private final String kind = "ServiceAccount";
  private final String apiVersion = "v1";
  private Metadata metadata;
  private List<SecretID> secrets;
  private List<SecretID> imagePullSecrets;

  public ServiceAccount() {}

  public ServiceAccount(
      Metadata metadata, List<SecretID> secrets, List<SecretID> imagePullSecrets) {
    this.metadata = metadata;
    this.secrets = secrets;
    this.imagePullSecrets = imagePullSecrets;
  }

  public String getKind() {
    return kind;
  }

  public String getApiVersion() {
    return apiVersion;
  }

  public List<SecretID> getSecrets() {
    return secrets;
  }

  public void setSecrets(List<SecretID> secrets) {
    this.secrets = secrets;
  }

  public Metadata getMetadata() {
    return metadata;
  }

  public void setMetadata(Metadata metadata) {
    this.metadata = metadata;
  }

  public List<SecretID> getImagePullSecrets() {
    return imagePullSecrets;
  }

  public void setImagePullSecrets(List<SecretID> imagePullSecrets) {
    this.imagePullSecrets = imagePullSecrets;
  }
}
