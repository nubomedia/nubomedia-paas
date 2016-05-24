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
 * Created by maa on 07.02.16.
 */
public class HorizontalPodAutoscaler {

    private final String kind = "HorizontalPodAutoscaler";
    private final String apiVersion = "extensions/v1beta1"; //TODO Change when are official
    private Metadata metadata;
    private HPASpec spec;

    public HorizontalPodAutoscaler(Metadata metadata, HPASpec spec) {
        this.metadata = metadata;
        this.spec = spec;
    }

    public HorizontalPodAutoscaler() {
    }

    public String getKind() {
        return kind;
    }

    public String getApiVersion() {
        return apiVersion;
    }

    public Metadata getMetadata() {
        return metadata;
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }

    public HPASpec getSpec() {
        return spec;
    }

    public void setSpec(HPASpec spec) {
        this.spec = spec;
    }
}
