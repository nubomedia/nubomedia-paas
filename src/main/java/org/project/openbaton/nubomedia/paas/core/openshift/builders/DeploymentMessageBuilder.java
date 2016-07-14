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

package org.project.openbaton.nubomedia.paas.core.openshift.builders;

import org.project.openbaton.nubomedia.paas.model.openshift.*;

/**
 * Created by maa on 25/09/2015.
 */
public class DeploymentMessageBuilder {

  private String name;
  private Container[] containers;
  private int replicaNumber;
  private ImageChangeTrigger[] triggers;
  private String strategyType;

  public DeploymentMessageBuilder(
      String name,
      Container[] containers,
      int replicaNumber,
      ImageChangeTrigger[] triggers,
      String strategyType) {
    this.name = name;
    this.containers = containers;
    this.replicaNumber = replicaNumber;
    this.triggers = triggers;
    this.strategyType = strategyType;
  }

  public DeploymentConfig buildMessage() {

    Metadata metadata = new Metadata(name + "-dc", "", "");
    MetadataDeploy metadeploy = new MetadataDeploy(new MetadataDeploy.Labels(name));
    Selector selector = new Selector(name);
    DeploymentConfigSpec.Strategy strategy = new DeploymentConfigSpec.Strategy(strategyType);

    DeploymentConfigSpec spec =
        new DeploymentConfigSpec(
            new Template(metadeploy, new SpecDeploy(containers)),
            replicaNumber,
            selector,
            triggers,
            strategy);

    return new DeploymentConfig(metadata, spec);
  }
}
