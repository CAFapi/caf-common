package com.hpe.caf.api.autoscale;


import com.hpe.caf.api.ConfigurationSource;


/**
 * Boilerplate for acquiring a WorkloadAnalyserFactory. It is holds the unique name for the WorkloadAnalyser.
 */
public interface WorkloadAnalyserFactoryProvider
{
    /**
     * Return a WorkloadAnalyserFactory.
     * @param configurationSource the configuration, used to setup a WorkloadAnalyserFactory
     * @return an implementation of a WorkloadAnalyserFactory
     * @throws ScalerException if the factory cannot be created
     */
    WorkloadAnalyserFactory getWorkloadAnalyserFactory(final ConfigurationSource configurationSource)
            throws ScalerException;


    /**
     * @return a unique key name for this sort of WorkloadAnalyser the factory produces
     */
    String getWorkloadAnalyserName();
}
