package com.hpe.caf.api.autoscale;


/**
 * A WorkloadAnalyser examines the workload of a service and makes
 * recommendations upon how to scale it at a given time.
 * @since 9.0
 */
public interface WorkloadAnalyser
{
    /**
     * Analyse the workload of a service given information on its
     * current instances, and make a recommendation on how to scale it.
     * @param instanceInfo information on the currently running instances of a service
     * @return a recommendation on how to scale this service (if at all)
     * @throws ScalerException if the workload analysis fails
     */
    ScalingAction analyseWorkload(InstanceInfo instanceInfo)
        throws ScalerException;
}
