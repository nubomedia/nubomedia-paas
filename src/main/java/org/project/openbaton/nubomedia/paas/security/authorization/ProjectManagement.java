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

package org.project.openbaton.nubomedia.paas.security.authorization;

import org.omg.CosNaming.NamingContextPackage.NotFound;
import org.project.openbaton.nubomedia.paas.core.AppManager;
import org.project.openbaton.nubomedia.paas.exceptions.BadRequestException;
import org.project.openbaton.nubomedia.paas.exceptions.ForbiddenException;
import org.project.openbaton.nubomedia.paas.exceptions.NotFoundException;
import org.project.openbaton.nubomedia.paas.exceptions.openshift.UnauthorizedException;
import org.project.openbaton.nubomedia.paas.model.persistence.security.Project;
import org.project.openbaton.nubomedia.paas.model.persistence.security.Role;
import org.project.openbaton.nubomedia.paas.model.persistence.security.User;
import org.project.openbaton.nubomedia.paas.repository.security.ProjectRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by lto on 24/05/16.
 */
@Service
public class ProjectManagement
    implements org.project.openbaton.nubomedia.paas.security.interfaces.ProjectManagement {

  @Autowired private UserManagement userManagement;

  @Autowired private AppManager appManager;

  @Autowired private ProjectRepository projectRepository;

  private Logger log = LoggerFactory.getLogger(this.getClass());

  @Override
  public Project save(Project project) {
    return projectRepository.save(project);
  }

  @Override
  public Project add(Project project)
      throws BadRequestException, NotFoundException, ForbiddenException {
    log.trace("Adding new project " + project.getName());
    if (project.getUsers() == null) {
      project.setUsers(new HashMap<String, Role.RoleEnum>());
    }
    project = projectRepository.save(project);
    for (String username : project.getUsers().keySet()) {
      userManagement.addRole(username, project.getName(), project.getUsers().get(username));
    }
    return project;
  }

  @Override
  public void delete(Project project)
      throws NotFoundException, UnauthorizedException, ForbiddenException, BadRequestException {
    Project projectToDelete = projectRepository.findFirstById(project.getId());
    if (project.getName().equals("admin"))
      throw new ForbiddenException("Forbidden to delete the project admin");
    appManager.deleteApps(project.getId());
    for (User userTmp : userManagement.query()) {
      for (Role roleTmp : userTmp.getRoles()) {
        if (roleTmp.getProject().equals(project.getName())) {
          userManagement.removeRole(userTmp.getUsername(), projectToDelete.getName());
        }
      }
    }
    projectRepository.delete(projectToDelete);
  }

  @Override
  public Project update(Project new_project)
      throws NotFoundException, ForbiddenException, BadRequestException {
    Project project = projectRepository.findFirstById(new_project.getId());
    if (!project.getName().equals(new_project.getName())) {
      throw new ForbiddenException("Forbidden to change the project name");
    }
    if (project == null) {
      throw new NotFoundException("Not found project " + new_project.getId());
    }
    project.setDescription(new_project.getDescription());
    for (String username : project.getUsers().keySet()) {
      if (new_project.getUsers().containsKey(username)) {
        if (project.getUsers().get(username).ordinal()
            != new_project.getUsers().get(username).ordinal()) {
          userManagement.updateRole(
              username, project.getName(), new_project.getUsers().get(username));
        }
      } else {
        userManagement.removeRole(username, project.getName());
      }
    }
    for (String username : new_project.getUsers().keySet()) {
      if (!project.getUsers().containsKey(username)) {
        userManagement.addRole(username, username, new_project.getUsers().get(username));
      }
    }
    return projectRepository.save(project);
  }

  @Override
  public Iterable<Project> query() {
    return projectRepository.findAll();
  }

  @Override
  public Project query(String id) {
    Project project = projectRepository.findFirstById(id);
    return project;
  }

  @Override
  public Project queryByName(String name) {
    return projectRepository.findFirstByName(name);
  }

  @Override
  public Iterable<Project> query(User user) {
    List<Project> projects = new ArrayList<>();
    for (Role role : user.getRoles()) projects.add(this.queryByName(role.getProject()));
    return projects;
  }

  @Override
  public Project addUser(String project_name, String username, Role.RoleEnum role)
      throws NotFoundException {
    log.debug("Adding role " + username + ":" + role + " to project " + project_name);
    Project project = projectRepository.findFirstByName(project_name);
    if (project == null) throw new NotFoundException("Not found project " + username);
    project.getUsers().put(username, role);
    return projectRepository.save(project);
  }

  @Override
  public Project removeUser(String project_name, String username) throws NotFoundException {
    log.debug("Removing role " + username + " from project " + project_name);
    Project project = projectRepository.findFirstByName(project_name);
    if (project == null) throw new NotFoundException("Not found project " + username);
    project.getUsers().remove(username);
    return projectRepository.save(project);
  }

  @Override
  public Project updateUser(String project_name, String username, Role.RoleEnum role)
      throws NotFoundException {
    log.debug("Updating role " + username + ":" + role + " of project " + project_name);
    Project project = projectRepository.findFirstByName(project_name);
    if (project == null) throw new NotFoundException("Not found project " + username);
    project.getUsers().put(username, role);
    return projectRepository.save(project);
  }

  @Override
  public boolean exist(String id) {
    return projectRepository.exists(id);
  }
}
