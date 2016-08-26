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

package org.project.openbaton.nubomedia.paas.api;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.project.openbaton.nubomedia.paas.exceptions.BadRequestException;
import org.project.openbaton.nubomedia.paas.exceptions.ForbiddenException;
import org.project.openbaton.nubomedia.paas.exceptions.NotFoundException;
import org.project.openbaton.nubomedia.paas.exceptions.openshift.UnauthorizedException;
import org.project.openbaton.nubomedia.paas.model.persistence.security.Role;
import org.project.openbaton.nubomedia.paas.model.persistence.security.User;
import org.project.openbaton.nubomedia.paas.security.interfaces.UserManagement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.common.exceptions.UnauthorizedUserException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class RestUsers {

  private Logger log = LoggerFactory.getLogger(this.getClass());

  @Autowired private UserManagement userManagement;

  @Autowired private Gson gson;

  /**
   * Adds a new User to the Users repository
   *
   * @param new_user
   * @return user
   */
  @RequestMapping(
    method = RequestMethod.POST,
    consumes = MediaType.APPLICATION_JSON_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE
  )
  @ResponseStatus(HttpStatus.CREATED)
  public User create(@RequestBody @Valid User new_user)
      throws ForbiddenException, BadRequestException, NotFoundException, UnauthorizedException {
    log.info("Adding user: " + new_user.getUsername());
    User user = null;
    if (isAdmin()) {
      user = userManagement.add(new_user);
      user.setPassword(null);
    } else {
      throw new ForbiddenException("Forbidden to create a new user");
    }
    return user;
  }

  /**
   * Removes the User from the Users repository
   *
   * @param username : the username of user to be removed
   */
  @RequestMapping(value = "{username}", method = RequestMethod.DELETE)
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(@PathVariable("username") String username)
      throws ForbiddenException, BadRequestException, NotFoundException {
    log.debug("removing User with username " + username);
    if (isAdmin()) {
      if (!getCurrentUser().getUsername().equals(username)) {
        User user = userManagement.queryByName(username);
        if (user == null) throw new BadRequestException("Not found user " + username);
        userManagement.delete(user);
      } else {
        throw new ForbiddenException("You can't delete yourself. Please ask another admin.");
      }
    } else {
      throw new ForbiddenException("Forbidden to delete a user");
    }
  }

  /**
   * Removes all the Users with specified ids from the repository
   *
   * @param ids the list of Usernames that will be removed
   */
  @RequestMapping(
    value = "/multipledelete",
    method = RequestMethod.POST,
    consumes = MediaType.APPLICATION_JSON_VALUE
  )
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void multipleDelete(@RequestBody @Valid List<String> ids)
      throws ForbiddenException, BadRequestException, NotFoundException {
    if (isAdmin()) {
      for (String id : ids) {
        if (!getCurrentUser().getId().equals(id)) {
          userManagement.delete(userManagement.query(id));
        } else {
          throw new ForbiddenException("You can't delete yourself. Please ask another admin.");
        }
      }
    } else {
      throw new ForbiddenException("Forbidden to delete a user");
    }
    for (String username : ids) userManagement.delete(userManagement.query(username));
  }

  /**
   * Returns the list of the Users available
   *
   * @return List<User>: The list of Users available
   */
  @RequestMapping(method = RequestMethod.GET)
  public Iterable<User> findAll()
      throws ForbiddenException, UnauthorizedException, NotFoundException {
    log.debug("Find all Users");
    Iterable<User> users = null;
    if (isAdmin()) {
      users = userManagement.query();
      for (User user : users) user.setPassword(null);
      return users;
    } else {
      throw new ForbiddenException("Forbidden to list all users");
    }
  }

  /**
   * Returns the User selected by username
   *
   * @param username : The username of the User
   * @return User: The User selected
   */
  @RequestMapping(value = "{username}", method = RequestMethod.GET)
  public User find(@PathVariable("username") String username)
      throws ForbiddenException, UnauthorizedException, NotFoundException {
    log.debug("find User with username " + username);
    if (isAdmin() || getCurrentUser().equals(username)) {
      User user = userManagement.queryByName(username);
      log.trace("Found User: " + user);
      user.setPassword(null);
      return user;
    } else {
      throw new ForbiddenException("Forbidden to request this user");
    }
  }

  @RequestMapping(value = "current", method = RequestMethod.GET)
  public User findCurrentUser()
      throws ForbiddenException, UnauthorizedException, NotFoundException {
    User user = getCurrentUser();
    user.setPassword(null);
    log.trace("Found User: " + user);
    return user;
  }

  /**
   * Updates the User
   *
   * @param new_user : The User to be updated
   * @return User The User updated
   */
  @RequestMapping(
    value = "{username}",
    method = RequestMethod.PUT,
    consumes = MediaType.APPLICATION_JSON_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE
  )
  @ResponseStatus(HttpStatus.OK)
  public User update(@PathVariable("username") String username, @RequestBody @Valid User new_user)
      throws ForbiddenException, BadRequestException, NotFoundException {
    log.debug("Updating User " + username);
    User userToUpdate = userManagement.queryByName(username);
    if (userToUpdate == null) {
      throw new BadRequestException("Not found user " + username);
    }
    if (!userToUpdate.getId().equals(new_user.getId())) {
      throw new BadRequestException("User and user to update are not the same users");
    }
    User user = null;
    if (isAdmin()) {
      user = userManagement.update(new_user);
      user.setPassword(null);
    } else {
      throw new ForbiddenException("Forbidden to update a user");
    }
    return user;
  }

  @RequestMapping(
    value = "changepwd",
    method = RequestMethod.PUT,
    consumes = MediaType.APPLICATION_JSON_VALUE
  )
  @ResponseStatus(HttpStatus.ACCEPTED)
  public void changePassword(@RequestBody /*@Valid*/ JsonObject newPwd)
      throws UnauthorizedUserException {
    log.debug("Changing password");
    JsonObject jsonObject = gson.fromJson(newPwd, JsonObject.class);
    userManagement.changePassword(
        jsonObject.get("old_pwd").getAsString(), jsonObject.get("new_pwd").getAsString());
  }

  private User getCurrentUser() throws NotFoundException {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null) return null;
    String currentUserName = authentication.getName();
    return userManagement.queryByName(currentUserName);
  }

  public boolean isAdmin() throws ForbiddenException, NotFoundException {
    User currentUser = getCurrentUser();
    log.trace("Check user if admin: " + currentUser.getUsername());
    for (Role role : currentUser.getRoles()) {
      if (role.getRole().ordinal() == Role.RoleEnum.ADMIN.ordinal()) {
        return true;
      }
    }
    return false;
  }

  /**
   * Updates the User
   *
   * @param username : The User to reset the password
   * @return User The User updated
   */
  @RequestMapping(
    value = "{username}/reset",
    method = RequestMethod.PUT,
    consumes = MediaType.APPLICATION_JSON_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE
  )
  @ResponseStatus(HttpStatus.OK)
  public User resetPassword(
      @PathVariable("username") String username, @RequestBody JsonObject password)
      throws ForbiddenException, BadRequestException, NotFoundException {
    log.debug("Reseting password for User " + username);
    User userToReset = userManagement.queryByName(username);
    if (userToReset == null) {
      throw new BadRequestException("Not found user " + username);
    }
    User user = null;
    if (isAdmin()) {
      user = userManagement.resetPassword(userToReset, password.get("password").getAsString());
      user.setPassword(null);
    } else {
      throw new ForbiddenException("Forbidden to update a user");
    }
    return user;
  }
}
