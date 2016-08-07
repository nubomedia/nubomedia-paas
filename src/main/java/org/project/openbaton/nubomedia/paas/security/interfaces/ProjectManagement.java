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

package org.project.openbaton.nubomedia.paas.security.interfaces;

import org.project.openbaton.nubomedia.paas.exceptions.BadRequestException;
import org.project.openbaton.nubomedia.paas.exceptions.ForbiddenException;
import org.project.openbaton.nubomedia.paas.exceptions.NotFoundException;
import org.project.openbaton.nubomedia.paas.exceptions.openshift.UnauthorizedException;
import org.project.openbaton.nubomedia.paas.model.persistence.security.Project;
import org.project.openbaton.nubomedia.paas.model.persistence.security.Role;
import org.project.openbaton.nubomedia.paas.model.persistence.security.User;

import java.util.Map;

/**
 * Created by lto on 24/05/16.
 */
public interface ProjectManagement {
  Project save(Project project);

  /**
   *
   * @param project
   */
  Project add(Project project) throws BadRequestException, NotFoundException, ForbiddenException;

  /**
   *
   * @param project
   */
  void delete(Project project)
      throws NotFoundException, UnauthorizedException, ForbiddenException, BadRequestException;

  /**
   *
   * @param new_project
   */
  Project update(Project new_project)
      throws ForbiddenException, NotFoundException, BadRequestException;

  /**
   */
  Iterable<Project> query();

  /**
   *
   * @param id
   */
  Project query(String id) throws NotFoundException;

  /**
   *
   * @param name
   * @return
   */
  Project queryByName(String name);

  Iterable<Project> query(User user) throws NotFoundException;

  Project addUser(String project_name, String username, Role.RoleEnum role)
      throws NotFoundException;

  Project removeUser(String project_name, String username) throws NotFoundException;

  Project updateUser(String project_name, String username, Role.RoleEnum role)
      throws NotFoundException;

  boolean exist(String project);
}
