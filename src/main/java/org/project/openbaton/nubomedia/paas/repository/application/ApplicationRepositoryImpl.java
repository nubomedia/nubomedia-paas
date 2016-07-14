/*
 * Copyright (c) 2015 Fraunhofer FOKUS
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.project.openbaton.nubomedia.paas.repository.application;

import org.project.openbaton.nubomedia.paas.model.persistence.Application;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by mob on 03.09.15.
 */
@Transactional(readOnly = true)
public class ApplicationRepositoryImpl implements ApplicationRepositoryCustom {

  @Autowired private ApplicationRepository applicationRepository;

  @Override
  public Application findFirstByAppIdAndProjectId(String id, String projectId) {
    for (Application app : applicationRepository.findAll()) {
      if (app.getId().equals(id) && app.getProjectId().equals(projectId)) {
        return app;
      }
    }
    return null;
  }

  @Override
  public Application findByNsrId(String nsrId) {
    for (Application app : applicationRepository.findAll()) {
      if (app.getMediaServerGroup().getNsrID().equals(nsrId)) {
        return app;
      }
    }
    return null;
  }

  @Override
  public Application findByMSGroupID(String msGroupId) {
    for (Application app : applicationRepository.findAll()) {
      if (app.getMediaServerGroup().getId().equals(msGroupId)) {
        return app;
      }
    }
    return null;
  }
}
