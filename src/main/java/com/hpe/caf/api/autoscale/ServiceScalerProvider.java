package com.hpe.caf.api.autoscale;


import com.hpe.caf.api.ConfigurationSource;


/**
 * Provides a method for acquiring a ServiceScaler. Implementations must have a no-arg constructor.
 * @since 5.0
 */
public interface ServiceScalerProvider
{
    /**
     * Get a ServiceScaler implementation.
     * @param configurationSource used for configuring a ServiceScaler
     * @return a ServiceScaler implementation
     * @throws ScalerException if the ServiceScaler could not be created
     */
    ServiceScaler getServiceScaler(final ConfigurationSource configurationSource)
        throws ScalerException;
}
