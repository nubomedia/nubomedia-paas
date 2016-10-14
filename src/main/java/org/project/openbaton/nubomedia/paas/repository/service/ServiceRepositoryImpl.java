/*
 *
 *  * (C) Copyright 2016 NUBOMEDIA (http://www.nubomedia.eu)
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *   http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *  *
 *
 */

package org.project.openbaton.nubomedia.paas.repository.service;

import org.project.openbaton.nubomedia.paas.model.persistence.Application;
import org.project.openbaton.nubomedia.paas.model.persistence.SupportingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by mob on 03.09.15.
 */
@Transactional(readOnly = true)
public class ServiceRepositoryImpl implements ServiceRepositoryCustom {

  @Autowired private ServiceRepository serviceRepository;

  @Override
  public SupportingService findFirstByServiceIdAndProjectId(String id, String projectId) {
    for (SupportingService service : serviceRepository.findAll()) {
      if (service.getId().equals(id) && service.getProjectId().equals(projectId)) {
        return service;
      }
    }
    return null;
  }
}
