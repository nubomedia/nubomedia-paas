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

package org.project.openbaton.nubomedia.paas.model.persistence.openbaton;

import org.openbaton.catalogue.util.IdGenerator;

import javax.persistence.*;
import java.util.Set;

/**
 * Created by maa on 21.10.15.
 */
@Entity
public class Host {

  @Id private String id;
  private String hostname;
  private String floatingIp;
  private String status;

  public Host() {}

  @PrePersist
  public void ensureId() {
    id = IdGenerator.createUUID();
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getHostname() {
    return hostname;
  }

  public void setHostname(String hostname) {
    this.hostname = hostname;
  }

  public String getFloatingIp() {
    return floatingIp;
  }

  public void setFloatingIp(String floatingIp) {
    this.floatingIp = floatingIp;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  @Override
  public String toString() {
    return "Host{"
        + "id='"
        + id
        + '\''
        + ", hostname='"
        + hostname
        + '\''
        + ", floatingIp='"
        + floatingIp
        + '\''
        + ", status='"
        + status
        + '\''
        + '}';
  }
}
