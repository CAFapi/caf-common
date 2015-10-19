package com.hpe.caf.api;


public interface ManagedConfigurationSource extends HealthReporter, ConfigurationMetricsReporter, ConfigurationSource
{
    /**
     * Perform necessary shutdown operations.
     */
    void shutdown();


    /**
     * {@inheritDoc}
     */
    @Override
    int getConfigurationRequests();


    /**
     * {@inheritDoc}
     */
    @Override
    int getConfigurationErrors();
}
