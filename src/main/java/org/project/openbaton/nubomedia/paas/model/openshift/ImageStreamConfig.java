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

package org.project.openbaton.nubomedia.paas.model.openshift;

/**
 * Created by maa on 25/09/2015.
 */
public class ImageStreamConfig {

  private final String kind = "ImageStream";
  private final String apiVersion = "v1";

  private Metadata metadata;

  private ImageStreamSpecification spec;
  private ImageStreamStatus status;

  public static class ImageStreamSpecification {
    //TODO: add imagestream spec

    public ImageStreamSpecification() {}
  }

  public static class ImageStreamStatus {
    String dockerImageRepository;

    public ImageStreamStatus(String dockerImageRepository) {
      this.dockerImageRepository = dockerImageRepository;
    }

    public ImageStreamStatus() {}

    public String getDockerImageRepository() {
      return dockerImageRepository;
    }

    public void setDockerImageRepository(String dockerImageRepository) {
      this.dockerImageRepository = dockerImageRepository;
    }
  }

  public ImageStreamConfig(
      Metadata metadata, ImageStreamSpecification spec, ImageStreamStatus status) {
    this.metadata = metadata;
    this.spec = spec;
    this.status = status;
  }

  public ImageStreamConfig() {}

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

  public ImageStreamSpecification getSpec() {
    return spec;
  }

  public void setSpec(ImageStreamSpecification spec) {
    this.spec = spec;
  }

  public ImageStreamStatus getStatus() {
    return status;
  }

  public void setStatus(ImageStreamStatus status) {
    this.status = status;
  }
}
