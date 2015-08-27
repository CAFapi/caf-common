package com.hp.caf.api;


/**
 * Simple boilerplate for providing an ElectionFactory.
 */
public interface ElectionFactoryProvider
{
    /**
     * Acquire a new ElectionFactory instance.
     * @param configurationSource used to configure the ElectionFactory
     * @return a new ElectionFactory instance
     * @throws ElectionException if the ElectionFactory could not be created
     */
    ElectionFactory getElectionManager(final ConfigurationSource configurationSource)
        throws ElectionException;
}
