package com.hp.caf.config.file;


import com.hp.caf.api.BootstrapConfiguration;
import com.hp.caf.api.Codec;
import com.hp.caf.api.ConfigurationException;
import com.hp.caf.api.ConfigurationSource;
import com.hp.caf.api.Cipher;
import com.hp.caf.api.ConfigurationSourceProvider;
import com.hp.caf.api.ServicePath;


public class FileConfigurationSourceProvider implements ConfigurationSourceProvider
{
    @Override
    public ConfigurationSource getConfigurationSource(final BootstrapConfiguration bootstrapProvider, final Cipher cipher,
            final ServicePath servicePath, final Codec codec)
            throws ConfigurationException
    {
        return new FileConfigurationSource(bootstrapProvider, cipher, servicePath, codec);
    }
}
