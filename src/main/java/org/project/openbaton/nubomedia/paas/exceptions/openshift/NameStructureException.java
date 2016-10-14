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

package org.project.openbaton.nubomedia.paas.exceptions.openshift;

/**
 * Created by maa on 04.11.15.
 */
public class NameStructureException extends Exception { //I can't believe that has to be created

  public NameStructureException() {
    super();
  }

  public NameStructureException(Throwable throwable) {
    super(throwable);
  }

  public NameStructureException(String message) {
    super(message);
  }

  public NameStructureException(String message, Throwable throwable) {
    super(message, throwable);
  }
}
