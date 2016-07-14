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
 * Created by maa on 03.02.16.
 */
public class RouteTls {

  private String termination;
  private String key;
  private String certificate;
  private String caCertificate;
  private String destinationCertificate;

  public RouteTls(
      String termination,
      String key,
      String certificate,
      String caCertificate,
      String destinationCertificate) {
    this.termination = termination;
    this.key = key;
    this.certificate = certificate;
    this.caCertificate = caCertificate;
    this.destinationCertificate = destinationCertificate;
  }

  public RouteTls() {}

  public String getTermination() {
    return termination;
  }

  public void setTermination(String termination) {
    this.termination = termination;
  }

  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }

  public String getCertificate() {
    return certificate;
  }

  public void setCertificate(String certificate) {
    this.certificate = certificate;
  }

  public String getCaCertificate() {
    return caCertificate;
  }

  public void setCaCertificate(String caCertificate) {
    this.caCertificate = caCertificate;
  }

  public String getDestinationCertificate() {
    return destinationCertificate;
  }

  public void setDestinationCertificate(String destinationCertificate) {
    this.destinationCertificate = destinationCertificate;
  }
}
