package com.hpe.caf.api.autoscale;


import com.hpe.caf.api.HealthReporter;


/**
 * A ServiceScaler is a class that actively triggers or performs the up or
 * down scaling of a service on a platform.
 */
public abstract class ServiceScaler implements HealthReporter
{
    /**
     * Scale up a service.
     * @param service the service to scale up, by reference
     * @param amount the number of instances to scale up by
     * @throws ScalerException if the scaling operation cannot be performed
     */
    public abstract void scaleUp(final String service, final int amount)
        throws ScalerException;


    /**
     * Scale down a service.
     * @param service the service to scale down, by reference
     * @param amount the number of instances to scale down by
     * @throws ScalerException if the scaling operation cannot be performed
     */
    public abstract void scaleDown(final String service, final int amount)
        throws ScalerException;


    /**
     * Get information about the currently running instances of a service.
     * @param service the service to retrieve information on, by reference
     * @return an object containing information about the instances running
     * @throws ScalerException if the information could not be retrieved
     */
    public abstract InstanceInfo getInstanceInfo(final String service)
        throws ScalerException;
}
