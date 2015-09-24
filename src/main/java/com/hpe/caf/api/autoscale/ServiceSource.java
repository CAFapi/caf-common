package com.hpe.caf.api.autoscale;


import com.hpe.caf.api.HealthReporter;

import java.util.Set;


/**
 * A ServiceSource is responsible for finding and returning services
 * that an autoscaler can handle and scale.
 * @since 5.0
 */
public abstract class ServiceSource implements HealthReporter
{
    /**
     * @return services that the autoscaler should validate and scale
     * @throws ScalerException if the services cannot be acquired
     * @since 6.0
     */
    public abstract Set<ScalingConfiguration> getServices()
        throws ScalerException;
}
