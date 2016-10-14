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

/**
 * Created by maa on 26.01.16.
 */
public class Project {

  public static class ProjectSpec {

    private String[] finalizers;

    public ProjectSpec(String[] finalizers) {
      this.finalizers = finalizers;
    }

    public ProjectSpec() {}

    public String[] getFinalizers() {
      return finalizers;
    }

    public void setFinalizers(String[] finalizers) {
      this.finalizers = finalizers;
    }
  }

  public static class ProjectStatus {

    private String phase;

    public ProjectStatus(String phase) {
      this.phase = phase;
    }

    public ProjectStatus() {}

    public String getPhase() {
      return phase;
    }

    public void setPhase(String phase) {
      this.phase = phase;
    }
  }

  private final String kind = "Project";
  private final String apiVersion = "v1";
  private Metadata metadata;
  private ProjectSpec spec;
  private ProjectStatus status;

  public Project(Metadata metadata, ProjectSpec spec, ProjectStatus status) {
    this.metadata = metadata;
    this.spec = spec;
    this.status = status;
  }

  public Project() {}

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

  public ProjectSpec getSpec() {
    return spec;
  }

  public void setSpec(ProjectSpec spec) {
    this.spec = spec;
  }

  public ProjectStatus getStatus() {
    return status;
  }

  public void setStatus(ProjectStatus status) {
    this.status = status;
  }
}
