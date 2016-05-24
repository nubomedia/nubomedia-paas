package org.project.openbaton.nubomedia.paas.model.persistence;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by maa on 21.10.15.
 */
public interface ApplicationRepository extends CrudRepository<Application, String> {

    List<Application> findByAppName(String appName);
    Application findFirstByAppID(String id);
}
