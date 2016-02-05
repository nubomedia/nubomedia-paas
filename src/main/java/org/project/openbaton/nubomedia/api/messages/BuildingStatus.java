package org.project.openbaton.nubomedia.api.messages;

/**
 * Created by maa on 08.10.15.
 */
public enum BuildingStatus {

    CREATED,
    INITIALIZING,
    INITIALISED,
    DUPLICATED,
    BUILDING,
    BUILD_OK, //internal state not visible from outside, used to redirect requests to deploymentManager
    DEPLOYNG,
    RUNNING,
    FAILED,
    PAAS_RESOURCE_MISSING

}
