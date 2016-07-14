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
public class RouteSpec {

  public static class Status {
    //TODO: add status information in case of status request
  }

  private String host;
  private BuildElements to;
  private RouteTls tls;
  private Status status;

  public RouteSpec() {}

  public RouteSpec(String host, BuildElements to, RouteTls tls, Status status) {
    this.host = host;
    this.to = to;
    this.tls = tls;
    this.status = status;
  }

  public String getHost() {
    return host;
  }

  public void setHost(String host) {
    this.host = host;
  }

  public BuildElements getTo() {
    return to;
  }

  public void setTo(BuildElements to) {
    this.to = to;
  }

  public RouteTls getTls() {
    return tls;
  }

  public void setTls(RouteTls tls) {
    this.tls = tls;
  }

  public Status getStatus() {
    return status;
  }

  public void setStatus(Status status) {
    this.status = status;
  }
}
