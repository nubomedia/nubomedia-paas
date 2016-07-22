package org.project.openbaton.nubomedia.paas.security.authorization;

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

/**
 * Created by lto on 25/02/16.
 */
@Service
public class UserManagement
    implements org.project.openbaton.nubomedia.paas.security.interfaces.UserManagement {

  @Autowired private UserRepository userRepository;

  @Autowired
  @Qualifier("customUserDetailsService")
  private UserDetailsManager userDetailsManager;

  private Logger log = LoggerFactory.getLogger(this.getClass());

  @Override
  public User getCurrentUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String currentUserName = authentication.getName();
    return queryDB(currentUserName);
  }

  @Override
  public User add(User user) {

    checkCurrentUserNubomediaAdmin(getCurrentUser());

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

  private void checkCurrentUserNubomediaAdmin(User currentUser) {
    if (currentUser.getRoles().iterator().next().getRole().ordinal()
        != Role.RoleEnum.NUBOMEDIA_ADMIN.ordinal())
      throw new UnauthorizedUserException(
          "Sorry only NUBOMEDIA_ADMIN can add/delete/update/query Users");
  }

  @Override
  public void delete(User user) {
    checkCurrentUserNubomediaAdmin(getCurrentUser());
    for (Role role : user.getRoles()) {
      if (role.getRole().ordinal() == Role.RoleEnum.NUBOMEDIA_ADMIN.ordinal()) {
        throw new UnsupportedOperationException("You can't delete the NUBOMEDIA_ADMIN");
      }
    }
    userDetailsManager.deleteUser(user.getUsername());
    userRepository.delete(user);
  }

  @Override
  public User update(User new_user) {

    checkCurrentUserNubomediaAdmin(getCurrentUser());
    String[] roles = new String[new_user.getRoles().size()];

    Role[] objects = new_user.getRoles().toArray(new Role[0]);
    for (int i = 0; i < new_user.getRoles().size(); i++) {
      roles[i] = objects[i].getRole() + ":" + objects[i].getProject();
    }

    org.springframework.security.core.userdetails.User userToUpdate =
        new org.springframework.security.core.userdetails.User(
            new_user.getUsername(),
            BCrypt.hashpw(new_user.getPassword(), BCrypt.gensalt(12)),
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
    checkCurrentUserNubomediaAdmin(getCurrentUser());
    return userRepository.findAll();
  }

  @Override
  public User query(String username) {
    checkCurrentUserNubomediaAdmin(getCurrentUser());
    log.trace("Looking for user: " + username);
    return userRepository.findFirstByUsername(username);
  }

  @Override
  public User queryDB(String username) {
    return userRepository.findFirstByUsername(username);
  }
}
