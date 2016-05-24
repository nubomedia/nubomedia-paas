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
 * Created by maa on 29.09.15.
 */
public class Pods {

    private List<String> podNames;

    public Pods(List<String> podNames) {
        this.podNames = podNames;
    }

    public Pods() {
    }

    public List<String> getPodNames() {
        return podNames;
    }

    public void setPodNames(List<String> podNames) {
        this.podNames = podNames;
    }

    @Override
    public String toString() {
        return "Pods{" +
                "podNames=" + podNames +
                '}';
    }
}
