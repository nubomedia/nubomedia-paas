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
public class HPASpec {

    public static class HPASpecScaleRef {

        private final String kind = "DeploymentConfig";
        private final String apiVersion = "v1";
        private String name;
        private final String subresource = "scale";

        public HPASpecScaleRef() {
        }

        public HPASpecScaleRef(String name) {

            this.name = name;
        }

        public String getKind() {
            return kind;
        }

        public String getApiVersion() {
            return apiVersion;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSubresource() {
            return subresource;
        }
    }

    private HPASpecScaleRef scaleRef;
    private int minReplicas;
    private int maxReplicas;
    private HPASpecMetrics cpuUtilization;

    public HPASpec(HPASpecScaleRef scaleRef, int minReplicas, int maxReplicas, HPASpecMetrics cpuUtilization) {
        this.scaleRef = scaleRef;
        this.minReplicas = minReplicas;
        this.maxReplicas = maxReplicas;
        this.cpuUtilization = cpuUtilization;
    }

    public HPASpecScaleRef getScaleRef() {
        return scaleRef;
    }

    public void setScaleRef(HPASpecScaleRef scaleRef) {
        this.scaleRef = scaleRef;
    }

    public int getMinReplicas() {
        return minReplicas;
    }

    public void setMinReplicas(int minReplicas) {
        this.minReplicas = minReplicas;
    }

    public int getMaxReplicas() {
        return maxReplicas;
    }

    public void setMaxReplicas(int maxReplicas) {
        this.maxReplicas = maxReplicas;
    }

    public HPASpecMetrics getCpuUtilization() {
        return cpuUtilization;
    }

    public void setCpuUtilization(HPASpecMetrics cpuUtilization) {
        this.cpuUtilization = cpuUtilization;
    }
}
