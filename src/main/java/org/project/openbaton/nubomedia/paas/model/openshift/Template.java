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

package org.project.openbaton.nubomedia.paas.model.openshift;

/**
 * Created by maa on 25/09/2015.
 */
public class Template {

    private MetadataDeploy metadata;
    private SpecDeploy spec;

    public Template(MetadataDeploy metadata, SpecDeploy spec) {
        this.metadata = metadata;
        this.spec = spec;
    }

    public Template() {
    }

    public MetadataDeploy getMetadata() {
        return metadata;
    }

    public void setMetadata(MetadataDeploy metadata) {
        this.metadata = metadata;
    }

    public SpecDeploy getSpec() {
        return spec;
    }

    public void setSpec(SpecDeploy spec) {
        this.spec = spec;
    }
}
