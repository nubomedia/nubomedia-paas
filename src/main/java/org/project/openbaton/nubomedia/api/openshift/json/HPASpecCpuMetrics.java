package org.project.openbaton.nubomedia.api.openshift.json;

/**
 * Created by maa on 07.02.16.
 */
public class HPASpecCpuMetrics implements HPASpecMetrics{

    private int targetPercentage;

    public HPASpecCpuMetrics(int targetPercentage) {
        this.targetPercentage = targetPercentage;
    }

    public HPASpecCpuMetrics() {
    }

    public int getTargetPercentage() {
        return targetPercentage;
    }

    public void setTargetPercentage(int targetPercentage) {
        this.targetPercentage = targetPercentage;
    }
}
