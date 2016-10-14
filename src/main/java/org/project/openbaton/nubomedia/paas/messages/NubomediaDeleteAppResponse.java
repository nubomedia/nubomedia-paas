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

package org.project.openbaton.nubomedia.paas.messages;

/**
 * Created by maa on 09.10.15.
 */
public class NubomediaDeleteAppResponse {

  private String id;
  private String name;
  private String suffix;
  private String nameSpace;
  private int code;

  public NubomediaDeleteAppResponse() {}

  public NubomediaDeleteAppResponse(
      String id, String name, String suffix, String nameSpace, int deleteStatus) {
    this.id = id;
    this.name = name;
    this.suffix = suffix;
    this.nameSpace = nameSpace;
    this.code = deleteStatus;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getSuffix() {
    return suffix;
  }

  public void setSuffix(String suffix) {
    this.suffix = suffix;
  }

  public String getNameSpace() {
    return nameSpace;
  }

  public void setNameSpace(String nameSpace) {
    this.nameSpace = nameSpace;
  }

  public int getCode() {
    return code;
  }

  public void setCode(int code) {
    this.code = code;
  }
}
