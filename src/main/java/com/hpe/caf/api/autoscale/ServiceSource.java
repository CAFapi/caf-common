package com.hpe.caf.api.autoscale;


import com.hpe.caf.api.HealthReporter;

import java.util.Set;


/**
 * A ServiceSource is responsible for finding and returning services
 * that an autoscaler can handle and scale.
 */
public abstract class ServiceSource implements HealthReporter
{
    /**
     * @return services that the autoscaler should validate and scale
     * @throws ScalerException if the services cannot be acquired
     */
    public abstract Set<ScalingConfiguration> getServices()
        throws ScalerException;
}
