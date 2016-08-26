/*
 *
 *  * Copyright (c) 2016 Open Baton
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *     http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package org.project.openbaton.nubomedia.paas.core.openshift.builders;

import org.project.openbaton.nubomedia.paas.model.openshift.HPASpec;
import org.project.openbaton.nubomedia.paas.model.openshift.HPASpecCpuMetrics;
import org.project.openbaton.nubomedia.paas.model.openshift.HorizontalPodAutoscaler;
import org.project.openbaton.nubomedia.paas.model.openshift.Metadata;

/**
 * Created by maa on 07.02.16.
 */
public class HorizontalPodAutoscalerMessageBuilder {

  private String osName;
  private int minReplicas;
  private int maxReplicas;
  private int cpuUtilization;

  public HorizontalPodAutoscalerMessageBuilder(
      String osName, int minReplicas, int maxReplicas, int cpuUtilization) {
    this.osName = osName;
    this.minReplicas = minReplicas;
    this.maxReplicas = maxReplicas;
    this.cpuUtilization = cpuUtilization;
  }

  public HorizontalPodAutoscaler buildMessage() {

    Metadata metadata = new Metadata(osName + "-hpa", null, null);
    HPASpecCpuMetrics metrics = new HPASpecCpuMetrics(cpuUtilization);
    HPASpec.HPASpecScaleRef scaleRef = new HPASpec.HPASpecScaleRef(osName + "-dc");
    HPASpec spec = new HPASpec(scaleRef, minReplicas, maxReplicas, metrics);

    return new HorizontalPodAutoscaler(metadata, spec);
  }
}
