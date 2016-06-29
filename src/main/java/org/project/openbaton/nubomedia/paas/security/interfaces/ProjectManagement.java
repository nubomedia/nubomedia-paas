package org.project.openbaton.nubomedia.paas.security.interfaces;

import org.project.openbaton.nubomedia.paas.exceptions.NotAllowedException;
import org.project.openbaton.nubomedia.paas.exceptions.NotFoundException;
import org.project.openbaton.nubomedia.paas.exceptions.openshift.UnauthorizedException;
import org.project.openbaton.nubomedia.paas.model.persistence.security.Project;

/**
 * Created by lto on 24/05/16.
 */
public interface ProjectManagement {
    /**
     *
     * @param project
     */
    Project add(Project project);

    /**
     *
     * @param project
     */
    void delete(Project project) throws NotAllowedException, UnauthorizedException;

    /**
     *
     * @param new_project
     */
    Project update(Project new_project);

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

    Iterable<Project> queryForUser();

    boolean exist(String project);
}
