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

package org.project.openbaton.nubomedia.paas.repository.service;

import org.project.openbaton.nubomedia.paas.model.persistence.Application;
import org.project.openbaton.nubomedia.paas.model.persistence.SupportingService;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by maa on 21.10.15.
 */
public interface ServiceRepository
    extends CrudRepository<SupportingService, String>, ServiceRepositoryCustom {

  List<SupportingService> findByName(String name);

  List<SupportingService> findByProjectId(String projectId);

  SupportingService findFirstById(String id);
}
