/*
 * Copyright (c) 2016 Fraunhofer FOKUS
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *                http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.project.openbaton.nubomedia.paas.api;

import org.project.openbaton.nubomedia.paas.exceptions.NotAllowedException;
import org.project.openbaton.nubomedia.paas.exceptions.NotFoundException;
import org.project.openbaton.nubomedia.paas.model.persistence.security.Project;
import org.project.openbaton.nubomedia.paas.security.interfaces.ProjectManagement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by lto on 25/05/16.
 */
@RestController
@RequestMapping("/api/v1/projects")
public class RestProject {
    private static final Logger log = LoggerFactory.getLogger(RestProject.class);

    @Autowired
    private ProjectManagement projectManagement;

    /**
     * Adds a new Project to the Projects repository
     *
     * @param project
     * @return project
     */
    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Project create(@RequestBody @Valid Project project) {
        log.info("Adding Project: " + project.getName());
        return projectManagement.add(project);
    }

    /**
     * Removes the Project from the Projects repository
     *
     * @param id : the id of project to be removed
     */

    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") String id) throws NotAllowedException, NotFoundException {
        log.debug("removing Project with id " + id);
        projectManagement.delete(projectManagement.query(id));
    }

    /**
     * Removes all Projects with specified ids from the repository
     *
     * @param ids the list of Projects that will be removed
     */
    @RequestMapping(value = "/multipledelete", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void multipleDelete(@RequestBody @Valid List<String> ids) throws NotFoundException, NotAllowedException {
        for (String id : ids)
            projectManagement.delete(projectManagement.query(id));
    }

    /**
     * Returns the list of the Projects available
     *
     * @return List<Project>: The list of Projects available
     */
    @RequestMapping(method = RequestMethod.GET)
    public Iterable<Project> findAll() {
        log.debug("Find all Projects");
        return projectManagement.queryForUser();
    }

    /**
     * Returns the Project selected by id
     *
     * @param id : The id of the Project
     * @return Project: The Project selected
     */
    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public Project findById(@PathVariable("id") String id) throws NotFoundException {
        log.debug("find Project with id " + id);
        Project project = projectManagement.query(id);
        log.trace("Found Project: " + project);
        return project;
    }

    /**
     * Updates the Project
     *
     * @param new_project : The Project to be updated
     * @return Project The Project updated
     */

    @RequestMapping(value = "{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Project update(@RequestBody @Valid Project new_project) {
        return projectManagement.update(new_project);
    }
}
