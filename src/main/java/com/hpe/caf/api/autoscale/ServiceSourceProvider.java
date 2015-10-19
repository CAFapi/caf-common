package com.hpe.caf.api.autoscale;


import com.hpe.caf.api.ConfigurationSource;
import com.hpe.caf.naming.ServicePath;


/**
 * Provides a method for acquiring a ServiceSource. Implemenations must have a no-arg constructor.
 * @since 5.0
 */
public interface ServiceSourceProvider
{
    /**
     * Get a ServiceSource implementation.
     * @param configurationSource used for configuring a ServiceSource
     * @param servicePath unique service identifier for namespacing services returned by a ServiceSource
     * @return a ServiceSource implementation
     * @throws ScalerException if a ServiceSource could not be created
     * @since 6.0
     */
    ServiceSource getServiceSource(final ConfigurationSource configurationSource, final ServicePath servicePath)
            throws ScalerException;
}
