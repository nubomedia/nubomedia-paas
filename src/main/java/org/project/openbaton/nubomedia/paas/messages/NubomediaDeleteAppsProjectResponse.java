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

package org.project.openbaton.nubomedia.paas.messages;

import java.util.List;

/**
 * Created by maa on 09.10.15.
 */
public class NubomediaDeleteAppsProjectResponse {

  private String projectId;
  private String message;
  private int code;
  private List<NubomediaDeleteAppResponse> responses;

  public NubomediaDeleteAppsProjectResponse(
      String projectId, String message, List<NubomediaDeleteAppResponse> responses, int code) {
    this.projectId = projectId;
    this.message = message;
    this.responses = responses;
    this.code = code;
  }

  public String getProjectId() {
    return projectId;
  }

  public void setProjectId(String projectId) {
    this.projectId = projectId;
  }

  public int getCode() {
    return code;
  }

  public void setCode(int code) {
    this.code = code;
  }

  public List<NubomediaDeleteAppResponse> getResponses() {
    return responses;
  }

  public void setResponses(List<NubomediaDeleteAppResponse> responses) {
    this.responses = responses;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }
}
