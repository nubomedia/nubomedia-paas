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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.common.exceptions.UnauthorizedUserException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
  public Project add(Project project) {
    return projectRepository.save(project);
  }

  @Override
  public void delete(Project project)
      throws NotFoundException, UnauthorizedException, ForbiddenException, BadRequestException {
    Project projectToDelete = projectRepository.findFirstById(project.getId());
    if (project.getName().equals("admin"))
      throw new ForbiddenException("Forbidden to delete the project admin");
    appManager.deleteApps(project.getId());
    for (User userTmp : userManagement.query()) {
      Set<Role> rolesToRemove = new HashSet<>();
      for (Role roleTmp : userTmp.getRoles()) {
        if (roleTmp.getProject().equals(project.getName())) {
          rolesToRemove.add(roleTmp);
        }
      }
      if (rolesToRemove.size() > 0) {
        userTmp.getRoles().removeAll(rolesToRemove);
        userManagement.update(userTmp);
      }
    }
    projectRepository.delete(projectToDelete);
  }

  @Override
  public Project update(Project new_project) throws NotFoundException, ForbiddenException {
    Project project = projectRepository.findFirstById(new_project.getId());
    if (project.getName().equals("admin") && !project.getName().equals(new_project.getName())) {
      throw new ForbiddenException("Forbidden to change the name of the project admin");
    }
    if (project == null) {
      throw new NotFoundException("Not found project " + new_project.getId());
    }
    return projectRepository.save(new_project);
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
  public boolean exist(String id) {
    return projectRepository.exists(id);
  }
}
