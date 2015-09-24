package com.hpe.caf.api;


/**
 * Provides metrics for a ConfigurationSource.
 * @since 1.0
 */
public interface ConfigurationMetricsReporter
{
    /**
     * @return the number of configuration requests handled to date
     */
    int getConfigurationRequests();


    /**
     * @return the number of failures/errors when retrieving configuration to date
     */
    int getConfigurationErrors();
}
