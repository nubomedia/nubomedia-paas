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

package org.project.openbaton.nubomedia.paas.core.openshift.builders;

import org.project.openbaton.nubomedia.paas.model.openshift.*;

/**
 * Created by maa on 25/09/2015.
 */
public class BuildMessageBuilder {

  private String osName;
  private BuildStrategy bs;
  private BuildElements be;
  private String gitURL;
  private ConfigChangeTrigger[] triggers;
  private Source.SourceSecret secret;
  private String namespace;

  public BuildMessageBuilder(
      String osName,
      String namespace,
      BuildStrategy bs,
      BuildElements be,
      String gitURL,
      ConfigChangeTrigger[] triggers,
      Source.SourceSecret secret) {
    this.osName = osName;
    this.namespace = namespace;
    this.bs = bs;
    this.be = be;
    this.gitURL = gitURL;
    this.triggers = triggers;
    this.secret = secret;
  }

  public BuildConfig buildMessage() {

    Source src = new Source("Git", new Source.Git(gitURL), secret);
    Output output = new Output(be, null);
    BuildConfig.Spec spec = new BuildConfig.Spec(triggers, src, bs, output, new Resources());

    return new BuildConfig(new Metadata(osName + "-bc", "", "", namespace), spec);
  }
}
