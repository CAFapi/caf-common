package com.hpe.caf.config.rest;


import com.hpe.caf.api.BootstrapConfiguration;
import com.hpe.caf.api.Cipher;
import com.hpe.caf.api.Codec;
import com.hpe.caf.api.ConfigurationException;
import com.hpe.caf.api.ConfigurationSourceProvider;
import com.hpe.caf.api.ManagedConfigurationSource;
import com.hpe.caf.naming.ServicePath;


public class RestConfigurationSourceProvider implements ConfigurationSourceProvider
{
    @Override
    public ManagedConfigurationSource getConfigurationSource(final BootstrapConfiguration bootstrapProvider, final Cipher cipher,
                                                             final ServicePath servicePath, final Codec codec)
        throws ConfigurationException
    {
        return new RestConfigurationSource(bootstrapProvider, cipher, servicePath, codec);
    }
}
