package com.hpe.caf.api;


/**
 * A ConfigurationSource is intended to provide an implementation-agnostic method of
 * retrieving application-specific configuration.
 * @since 9.0
 */
public interface ConfigurationSource
{
    /**
     * Acquire a configuration class.
     * @param configClass the class that represents your configuration
     * @param <T> the class that represents your configuration
     * @return the configuration class requested, if it can be deserialised
     * @throws ConfigurationException if the configuration class cannot be acquired or deserialised
     */
    <T> T getConfiguration(Class<T> configClass)
            throws ConfigurationException;
}
