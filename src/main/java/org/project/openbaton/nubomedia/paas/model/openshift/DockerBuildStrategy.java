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

import org.project.openbaton.nubomedia.paas.model.persistence.EnvironmentVariable;

/**
 * Created by maa on 25/09/2015.
 */
public class DockerBuildStrategy implements BuildStrategy {

  private final String type = "Docker";
  private DockerStrategy dockerStrategy;

  public static class DockerStrategy {
    EnvironmentVariable[] env;
    BuildElements from;

    public DockerStrategy(EnvironmentVariable[] variables, BuildElements from) {
      this.env = variables;
      this.from = from;
    }

    public DockerStrategy() {}

    public BuildElements getFrom() {
      return from;
    }

    public void setFrom(BuildElements from) {
      this.from = from;
    }

    public EnvironmentVariable[] getVariables() {
      return env;
    }

    public void setVariables(EnvironmentVariable[] variables) {
      this.env = variables;
    }
  }

  public DockerBuildStrategy(DockerStrategy dockerStrategy) {
    this.dockerStrategy = dockerStrategy;
  }

  public DockerBuildStrategy() {}

  public String getType() {
    return type;
  }

  public DockerStrategy getDockerStrategy() {
    return dockerStrategy;
  }

  public void setDockerStrategy(DockerStrategy dockerStrategy) {
    this.dockerStrategy = dockerStrategy;
  }
}
