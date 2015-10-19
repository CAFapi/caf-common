package com.hpe.caf.api;


import com.hpe.caf.naming.ServicePath;


/**
 * Simple boilerplate to return a ConfigurationSource.
 * @since 9.0
 */
public interface ConfigurationSourceProvider
{
    /**
     * Acquire a ConfigurationSource implementation.
     *
     * @param bootstrapProvider used to provide initial configuration of the ConfigurationSource
     * @param cipher used to perform any necessary decryption in the configuration objects
     * @param servicePath used to acquire service-specific configuration
     * @param codec used to deserialise data from the source into objects
     * @return a new ConfigurationSource instance
     * @throws ConfigurationException if the ConfigurationSource could not be created
     */
    ManagedConfigurationSource getConfigurationSource(BootstrapConfiguration bootstrapProvider, Cipher cipher, ServicePath servicePath, Codec codec)
        throws ConfigurationException;
}
