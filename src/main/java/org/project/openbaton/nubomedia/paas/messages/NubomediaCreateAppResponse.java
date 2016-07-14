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

import org.project.openbaton.nubomedia.paas.model.persistence.Application;

/**
 * Created by maa on 28.09.15.
 */
public class NubomediaCreateAppResponse {

  private Application app;
  private int code;

  public NubomediaCreateAppResponse() {}

  public NubomediaCreateAppResponse(Application app, int code) {
    this.app = app;
    this.code = code;
  }

  public Application getApp() {
    return app;
  }

  public void setApp(Application app) {
    this.app = app;
  }

  public int getCode() {
    return code;
  }

  public void setCode(int code) {
    this.code = code;
  }
}
