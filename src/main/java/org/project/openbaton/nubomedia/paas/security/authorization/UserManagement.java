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
import org.project.openbaton.nubomedia.paas.exceptions.openshift.UnauthorizedException;
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
import org.springframework.security.oauth2.common.exceptions.UnauthorizedUserException;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;

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
  public User getCurrentUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String currentUserName = authentication.getName();
    return query(currentUserName);
  }

  @Override
  public User add(User user) throws BadRequestException, NotFoundException, ForbiddenException {
    log.debug("Adding new user: " + user);
    checkIntegrity(user);
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
  public void delete(User user) throws ForbiddenException {
    log.debug("Deleting user: " + user);
    if (user.getUsername().equals("admin"))
      throw new ForbiddenException("Forbidden to delete user admin");
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
    log.debug("Listing users");
    return userRepository.findAll();
  }

  @Override
  public User query(String id) {
    log.debug("Get user: " + id);
    return userRepository.findOne(id);
  }

  @Override
  public User queryByName(String username) {
    log.debug("Get user: " + username);
    return userRepository.findFirstByUsername(username);
  }

  @Override
  public void changePassword(String oldPwd, String newPwd) throws UnauthorizedUserException {
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
    if (!user.getUsername().equals("admin")) {
      if (user.getEmail() == null || user.getEmail().equals("")) {
        throw new BadRequestException("Email must be provided");
      }
      String EMAIL_PATTERN =
          "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
      Pattern pattern = Pattern.compile(EMAIL_PATTERN);
      if (!pattern.matcher(user.getEmail()).matches())
        throw new BadRequestException("Email is not well formatted");
    }
    boolean adminIntegrity = false;
    for (Role role : user.getRoles()) {
      if (role.getProject() == null) {
        throw new BadRequestException("Project must be provided");
      }
      if (projectManagement.queryByName(role.getProject()) == null) {
        throw new NotFoundException("Not found project " + role.getProject());
      }
      if (role.getRole() == null) {
        throw new BadRequestException("Role must be provided");
      }
      if (role.getProject().equals("admin")
          && role.getRole().ordinal() == Role.RoleEnum.ADMIN.ordinal()) {
        adminIntegrity = true;
      }
    }
    if (user.getUsername().equals("admin")) {
      if (!adminIntegrity)
        throw new ForbiddenException("Forbidden to change Role admin:ADMIN for user admin");
    }
  }
}
