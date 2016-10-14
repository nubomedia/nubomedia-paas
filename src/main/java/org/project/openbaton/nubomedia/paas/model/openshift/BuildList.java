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
 * Created by maa on 08.10.15.
 */
public class BuildList {

  private final String kind = "BuildList";
  private final String apiVersion = "v1";
  private Metadata metadata;
  private Build[] items;

  public BuildList() {}

  public BuildList(Metadata metadata, Build[] items) {
    this.metadata = metadata;
    this.items = items;
  }

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

  public Build[] getItems() {
    return items;
  }

  public void setItems(Build[] items) {
    this.items = items;
  }
}
