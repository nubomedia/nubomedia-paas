/*
 * Copyright (c) 2015-2016 Fraunhofer FOKUS
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

package org.project.openbaton.nubomedia.paas.core.openshift.builders;

import org.project.openbaton.nubomedia.paas.model.openshift.Metadata;
import org.project.openbaton.nubomedia.paas.model.openshift.ProjectRequest;

/**
 * Created by maa on 26.01.16.
 */
public class ProjectRequestBuilder {

    private String name;
    private String description;

    public ProjectRequestBuilder(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public ProjectRequest buildMessage(){
        Metadata metadata = new Metadata();
        metadata.setName(name);
        return new ProjectRequest(metadata, name+"-project", description);
    }
}
