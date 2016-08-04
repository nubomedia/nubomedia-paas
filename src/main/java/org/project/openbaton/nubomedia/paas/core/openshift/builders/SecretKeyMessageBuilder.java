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

package org.project.openbaton.nubomedia.paas.core.openshift.builders;

import org.project.openbaton.nubomedia.paas.model.openshift.Metadata;
import org.project.openbaton.nubomedia.paas.model.openshift.SecretConfig;
import org.project.openbaton.nubomedia.paas.model.openshift.SshGitKeySecret;

import java.util.UUID;

/**
 * Created by maa on 05.10.15.
 */
public class SecretKeyMessageBuilder {

  private String name;
  private String sshPrivateKey;

  public SecretKeyMessageBuilder(String name, String sshPrivateKey) {
    this.name = name;
    this.sshPrivateKey = sshPrivateKey;
  }

  public SecretConfig buildMessage() {
    UUID uuid = UUID.randomUUID();
    Metadata secretMeta =
        new Metadata(name + Long.toString(uuid.getLeastSignificantBits(), 64) + "-secret", "", "");
    SshGitKeySecret secret = new SshGitKeySecret(sshPrivateKey);
    return new SecretConfig(secretMeta, secret);
  }
}
