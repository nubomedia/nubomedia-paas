/*
 *
 *  * Copyright (c) 2016 Open Baton
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *     http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package org.project.openbaton.nubomedia.paas.model.openshift;

/**
 * Created by maa on 25/09/2015. S2i JSon message
 */
public class Source {

  private String type;
  private Git git; //TODO add other sources and not only GIT Repositories (if is possible)
  private SourceSecret sourceSecret;

  public static class SourceSecret {
    String name;

    public SourceSecret(String name) {
      this.name = name;
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }
  }

  public static class Git {
    String uri;

    public Git(String URI) {
      this.uri = URI;
    }

    public Git() {}

    public String getURI() {
      return uri;
    }

    public void setURI(String URI) {
      this.uri = URI;
    }
  }

  public Source(String type, Git git, SourceSecret sourceSecret) {
    this.type = type;
    this.git = git;
    this.sourceSecret = sourceSecret;
  }

  public Source() {}

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public Git getGit() {
    return git;
  }

  public void setGit(Git git) {
    this.git = git;
  }

  public SourceSecret getSourceSecret() {
    return sourceSecret;
  }

  public void setSourceSecret(SourceSecret sourceSecret) {
    this.sourceSecret = sourceSecret;
  }
}
