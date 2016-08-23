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

import org.project.openbaton.nubomedia.paas.exceptions.BadRequestException;
import org.project.openbaton.nubomedia.paas.exceptions.ForbiddenException;
import org.project.openbaton.nubomedia.paas.exceptions.NotFoundException;
import org.project.openbaton.nubomedia.paas.model.persistence.security.Project;
import org.project.openbaton.nubomedia.paas.repository.security.UserRepository;
import org.project.openbaton.nubomedia.paas.model.persistence.security.Role;
import org.project.openbaton.nubomedia.paas.model.persistence.security.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Created by lto on 25/02/16.
 */
@Service
public class UserManagement
    implements org.project.openbaton.nubomedia.paas.security.interfaces.UserManagement {

  @Autowired private UserRepository userRepository;

  @Autowired private ProjectManagement projectManagement;

  @Autowired
  @Qualifier("customUserDetailsService")
  private UserDetailsManager userDetailsManager;

  private Logger log = LoggerFactory.getLogger(this.getClass());

  @Override
  public User add(User user) throws BadRequestException, NotFoundException, ForbiddenException {
    log.debug("Adding new user: " + user);

    if (userRepository.findFirstByUsername(user.getUsername()) != null)
      throw new BadRequestException("Username exists already");

    checkIntegrity(user);

    for (Role role : user.getRoles()) {
      projectManagement.addUser(role.getProject(), user.getUsername(), role.getRole());
    }

    String[] roles = new String[user.getRoles().size()];

    Role[] objects = user.getRoles().toArray(new Role[0]);
    for (int i = 0; i < user.getRoles().size(); i++) {
      roles[i] = objects[i].getRole() + ":" + objects[i].getProject();
    }

    org.springframework.security.core.userdetails.User userToAdd =
        new org.springframework.security.core.userdetails.User(
            user.getUsername(),
            BCrypt.hashpw(user.getPassword(), BCrypt.gensalt(12)),
            user.isEnabled(),
            true,
            true,
            true,
            AuthorityUtils.createAuthorityList(roles));
    user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt(12)));
    userDetailsManager.createUser(userToAdd);
    return userRepository.save(user);
  }

  @Override
  public void delete(User user) throws ForbiddenException, NotFoundException {
    log.debug("Deleting user: " + user);
    if (user.getUsername().equals("admin"))
      throw new ForbiddenException("Forbidden to delete user admin");
    for (Role role : user.getRoles()) {
      projectManagement.removeUser(role.getProject(), user.getUsername());
    }
    userDetailsManager.deleteUser(user.getUsername());
    userRepository.delete(user);
  }

  @Override
  public User update(User new_user)
      throws ForbiddenException, BadRequestException, NotFoundException {
    log.debug("Updating user:" + new_user);
    User user = query(new_user.getId());
    if (!user.getUsername().equals(new_user.getUsername()))
      throw new ForbiddenException("Forbidden to change the username");
    new_user.setPassword(user.getPassword());

    checkIntegrity(new_user);

    for (Role role : user.getRoles()) {
      boolean found = false;
      for (Role roleToUpdate : new_user.getRoles()) {
        if (role.getProject().equals(roleToUpdate.getProject())) {
          if (role.getRole().ordinal() != roleToUpdate.getRole().ordinal()) {
            projectManagement.updateUser(role.getProject(), user.getUsername(), role.getRole());
            break;
          } else {
            break;
          }
        }
      }
      if (!found) {
        projectManagement.removeUser(role.getProject(), user.getUsername());
      }
    }

    for (Role roleToUpdate : new_user.getRoles()) {
      boolean found = false;
      for (Role role : user.getRoles()) {
        if (roleToUpdate.getProject().equals(role.getProject())) {
          found = true;
          break;
        }
      }
      if (!found) {
        projectManagement.addUser(
            roleToUpdate.getProject(), user.getUsername(), roleToUpdate.getRole());
      }
    }

    String[] roles = new String[new_user.getRoles().size()];

    Role[] objects = new_user.getRoles().toArray(new Role[0]);
    for (int i = 0; i < new_user.getRoles().size(); i++) {
      roles[i] = objects[i].getRole() + ":" + objects[i].getProject();
    }

    org.springframework.security.core.userdetails.User userToUpdate =
        new org.springframework.security.core.userdetails.User(
            new_user.getUsername(),
            new_user.getPassword(),
            new_user.isEnabled(),
            true,
            true,
            true,
            AuthorityUtils.createAuthorityList(roles));
    userDetailsManager.updateUser(userToUpdate);
    return userRepository.save(new_user);
  }

  @Override
  public Iterable<User> query() {
    log.trace("Listing users");
    return userRepository.findAll();
  }

  @Override
  public User query(String id) {
    log.trace("Get user: " + id);
    return userRepository.findOne(id);
  }

  @Override
  public User queryByName(String username) throws NotFoundException {
    log.trace("Get user: " + username);
    User user = userRepository.findFirstByUsername(username);
    if (user == null) throw new NotFoundException("Not found user " + username);
    return user;
  }

  @Override
  public User addRole(String username, String project_name, Role.RoleEnum role)
      throws NotFoundException, BadRequestException, ForbiddenException {
    log.trace("Adding role " + role + " to user " + username);
    User user = userRepository.findFirstByUsername(username);
    if (user == null) throw new NotFoundException("Not found user " + username);
    Role new_role = new Role();
    new_role.setProject(project_name);
    new_role.setRole(role);
    user.getRoles().add(new_role);
    checkIntegrity(user);
    userRepository.save(user);
    return user;
  }

  @Override
  public User removeRole(String username, String project)
      throws BadRequestException, NotFoundException, ForbiddenException {
    log.trace("Removing role " + project + " to user " + username);
    User user = userRepository.findFirstByUsername(username);
    if (user == null) throw new NotFoundException("Not found user " + username);
    Set<Role> rolesToRemove = new HashSet<Role>();
    for (Role role : user.getRoles()) {
      if (role.getProject().equals(project)) {
        rolesToRemove.add(role);
      }
    }
    user.getRoles().removeAll(rolesToRemove);
    checkIntegrity(user);
    userRepository.save(user);
    return user;
  }

  @Override
  public User updateRole(String username, String project_name, Role.RoleEnum role)
      throws BadRequestException, NotFoundException, ForbiddenException {
    log.trace("Updating role " + role + " of user " + username);
    User user = userRepository.findFirstByUsername(username);
    if (user == null) throw new NotFoundException("Not found user " + username);
    for (Role existing_role : user.getRoles()) {
      if (existing_role.getProject().equals(project_name)) {
        existing_role.setRole(role);
      }
    }
    ;
    checkIntegrity(user);
    userRepository.save(user);
    return user;
  }

  @Override
  public void changePassword(String oldPwd, String newPwd) {
    log.debug("Got old password: " + oldPwd);
    userDetailsManager.changePassword(oldPwd, newPwd);
  }

  public void checkIntegrity(User user)
      throws BadRequestException, NotFoundException, ForbiddenException {
    if (user.getUsername() == null || user.getUsername().equals("")) {
      throw new BadRequestException("Username must be provided");
    }
    if (user.getPassword() == null || user.getPassword().equals("")) {
      throw new BadRequestException("Password must be provided");
    }
    //    if (!user.getUsername().equals("admin")) {
    //      if (user.getEmail() == null || user.getEmail().equals("")) {
    //        throw new BadRequestException("Email must be provided");
    //      }
    if (user.getEmail() != null && !user.getEmail().equals("")) {
      String EMAIL_PATTERN =
          "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
      Pattern pattern = Pattern.compile(EMAIL_PATTERN);
      if (!pattern.matcher(user.getEmail()).matches())
        throw new BadRequestException("Email is not well formatted");
    }
    //    }
    boolean adminIntegrity = false;
    Set<String> assignedProjects = new HashSet<>();
    for (Role role : user.getRoles()) {
      if (role.getProject() == null || role.getProject().equals("")) {
        throw new BadRequestException("Project must be provided");
      }
      if (role.getRole() == null || role.getRole().equals("")) {
        throw new BadRequestException("Role must be provided");
      }
      Project project = projectManagement.queryByName(role.getProject());
      if (project == null) throw new BadRequestException("Not found project " + role.getProject());
      if (!assignedProjects.contains(role.getProject())) {
        assignedProjects.add(role.getProject());
      } else {
        throw new BadRequestException("Only one role per project");
      }
      if (role.getProject().equals("admin")
          && role.getRole().ordinal() == Role.RoleEnum.ADMIN.ordinal()) {
        adminIntegrity = true;
      }
    }
    if (user.getUsername().equals("admin")) {
      if (!adminIntegrity)
        throw new ForbiddenException("Forbidden to change Role admin:ADMIN for user admin");
      if (!user.isEnabled()) throw new ForbiddenException("Forbidden to disable admin user");
    }
  }
}
