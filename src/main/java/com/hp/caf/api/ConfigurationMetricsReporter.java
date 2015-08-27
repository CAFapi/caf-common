package com.hp.caf.api;


/**
 * Provides metrics for a ConfigurationProvider.
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
