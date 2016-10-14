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

import com.google.gson.annotations.SerializedName;
import org.apache.commons.codec.binary.Base64;

/**
 * Created by maa on 01.10.15.
 *
 * @param ssh-primayKey: is the original ssh-primary key (protected by https connection)
 */
public class SshGitKeySecret implements SecretType {

  @SerializedName(value = "ssh-privatekey")
  private String sshPrivateKey;

  public SshGitKeySecret(String sshPrimarykey) {
    this.sshPrivateKey = Base64.encodeBase64String(sshPrimarykey.getBytes());
  }

  public SshGitKeySecret() {}

  public String getSshPrimarykey() {
    return sshPrivateKey;
  }

  public void setSshPrimarykey(String sshPrimarykey) {
    this.sshPrivateKey = Base64.encodeBase64String(sshPrimarykey.getBytes());
  }
}
