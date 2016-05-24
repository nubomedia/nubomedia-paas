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

import java.util.List;

/**
 * Created by maa on 01.10.15.
 */
public class ServiceAccountList {

    private final String kind = "ServiceAccountList";
    private final String apiVersion = "v1";
    private Metadata metadata;
    private List<ServiceAccount> items;

    public ServiceAccountList() {
    }

    public ServiceAccountList(Metadata metadata, List<ServiceAccount> items) {
        this.metadata = metadata;
        this.items = items;
    }

    public String getKind() {
        return kind;
    }

    public String getApiVersion() {
        return apiVersion;
    }

    public List<ServiceAccount> getItems() {
        return items;
    }

    public void setItems(List<ServiceAccount> items) {
        this.items = items;
    }

    public Metadata getMetadata() {
        return metadata;
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }
}
