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
public class BuildStatus {

  private String phase;
  private String startTimestamp;
  private String completitionTimestamp;
  private double duration;
  private ConfigBuild config;

  public BuildStatus() {}

  public BuildStatus(
      String phase,
      String startTimestamp,
      String completitionTimestamp,
      double duration,
      ConfigBuild config) {
    this.phase = phase;
    this.startTimestamp = startTimestamp;
    this.completitionTimestamp = completitionTimestamp;
    this.duration = duration;
    this.config = config;
  }

  public String getPhase() {
    return phase;
  }

  public void setPhase(String phase) {
    this.phase = phase;
  }

  public String getStartTimestamp() {
    return startTimestamp;
  }

  public void setStartTimestamp(String startTimestamp) {
    this.startTimestamp = startTimestamp;
  }

  public String getCompletitionTimestamp() {
    return completitionTimestamp;
  }

  public void setCompletitionTimestamp(String completitionTimestamp) {
    this.completitionTimestamp = completitionTimestamp;
  }

  public double getDuration() {
    return duration;
  }

  public void setDuration(double duration) {
    this.duration = duration;
  }

  public ConfigBuild getConfig() {
    return config;
  }

  public void setConfig(ConfigBuild config) {
    this.config = config;
  }
}
