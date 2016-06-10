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

package org.project.openbaton.nubomedia.paas.model.persistence.openbaton;

/**
 * Created by maa on 22.01.16.
 */
public enum Flavor {

    SMALL("d1.small"),
    MEDIUM("d1.medium"),
    LARGE("d1.large");

    private final String value;

    Flavor(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
