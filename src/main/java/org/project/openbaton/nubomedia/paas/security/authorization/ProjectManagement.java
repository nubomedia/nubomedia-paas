package org.project.openbaton.nubomedia.paas.security.authorization;


import org.project.openbaton.nubomedia.paas.exceptions.NotAllowedException;
import org.project.openbaton.nubomedia.paas.exceptions.NotFoundException;
import org.project.openbaton.nubomedia.paas.model.persistence.ProjectRepository;
import org.project.openbaton.nubomedia.paas.model.persistence.security.Project;
import org.project.openbaton.nubomedia.paas.model.persistence.security.Role;
import org.project.openbaton.nubomedia.paas.model.persistence.security.User;
import org.project.openbaton.nubomedia.paas.security.interfaces.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.common.exceptions.UnauthorizedUserException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lto on 24/05/16.
 */
@Service
public class ProjectManagement implements org.project.openbaton.nubomedia.paas.security.interfaces.ProjectManagement {

    @Autowired
    private org.project.openbaton.nubomedia.paas.security.interfaces.UserManagement userManagement;

    @Autowired
    private ProjectRepository projectRepository;

    @Override
    public Project add(Project project) {
        User currentUser = getCurrentUser();
        if (currentUser != null) {
            if (currentUser.getRoles().iterator().next().getRole().ordinal() == Role.RoleEnum.OB_ADMIN.ordinal())
                return projectRepository.save(project);
        }else {
            return projectRepository.save(project);
        }
        throw new UnauthorizedUserException("Sorry only OB_ADMIN can add project");
    }

    @Override
    public void delete(Project project) throws NotAllowedException {
        Project projectToDelete = projectRepository.findFirstById(project.getId());
        User user = getCurrentUser();
        for (Role role : user.getRoles())
            if (role.getProject().equals(projectToDelete.getName())) {
                projectRepository.delete(projectToDelete);
                return;
            }
        throw new UnauthorizedUserException("Project not under the project chosen, are you trying to hack us? Just kidding, it's a bug :)");
    }

    @Override
    public Project update(Project new_project) {
        Project project = projectRepository.findFirstById(new_project.getId());
        User user = getCurrentUser();
        for (Role role : user.getRoles())
            if (role.getProject().equals(project.getName()))
                return projectRepository.save(new_project);
        throw new UnauthorizedUserException("Project not under the project chosen, are you trying to hack us? Just kidding, it's a bug :)");
    }

    @Override
    public Iterable<Project> query() {
        return projectRepository.findAll();
    }

    @Override
    public Project query(String id) throws NotFoundException {
        Project project = projectRepository.findFirstById(id);
        User user = getCurrentUser();
        for (Role role : user.getRoles())
            if (role.getProject().equals(project.getName()))
                return project;
        throw new UnauthorizedUserException("Project not under the project chosen, are you trying to hack us? Just kidding, it's a bug :)");
    }

    @Override
    public Project queryByName(String name) {
        return projectRepository.findFirstByName(name);
    }

    @Override
    public Iterable<Project> queryForUser() {

        List<Project> projects = new ArrayList<>();
        User user = getCurrentUser();
        if (user.getRoles().iterator().next().getRole().ordinal() == Role.RoleEnum.OB_ADMIN.ordinal() || user.getRoles().iterator().next().getRole().ordinal() == Role.RoleEnum.GUEST.ordinal())
            return projectRepository.findAll();
        for (Role role : user.getRoles())
            projects.add(this.queryByName(role.getProject()));

        return projects;
    }

    @Override
    public boolean exist(String id) {
        return projectRepository.exists(id);
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null)
            return null;
        String currentUserName = authentication.getName();
        return userManagement.queryDB(currentUserName);
    }
}
