package com.hp.caf.config.file;


import com.hp.caf.api.BootstrapConfiguration;
import com.hp.caf.api.Cipher;
import com.hp.caf.api.Codec;
import com.hp.caf.api.ConfigurationException;
import com.hp.caf.api.ConfigurationSource;
import com.hp.caf.api.HealthResult;
import com.hp.caf.api.Name;
import com.hp.caf.api.ServicePath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;


/**
 * This is a ConfigurationProvider that reads from a local disk file.
 * If the bootstrap parameter config.path is set, it will read the config
 * files from the specified directory, otherwise it will use the working dir.
 */
public class FileConfigurationSource extends ConfigurationSource
{
    public static final String CONFIG_PATH = "config.path";
    public static final String FILE_EXTENSION = ".conf";
    private Path configPath;
    private static final Logger LOG = LoggerFactory.getLogger(FileConfigurationSource.class);


    /**
     * {@inheritDoc}
     */
    public FileConfigurationSource(final BootstrapConfiguration bootstrapProvider, final Cipher cipher, final ServicePath servicePath, final Codec codec)
            throws ConfigurationException
    {
        super(bootstrapProvider, cipher, servicePath, codec);
        if ( bootstrapProvider.isConfigurationPresent(CONFIG_PATH) ) {
            try {
                configPath = FileSystems.getDefault().getPath(bootstrapProvider.getConfiguration(CONFIG_PATH));
            } catch (InvalidPathException e) {
                throw new ConfigurationException("Invalid configuration path", e);
            }
        }
        LOG.debug("Initialised");
    }


    @Override
    public void shutdown()
    {
        // nothing to do
    }


    @Override
    public HealthResult healthCheck()
    {
        return HealthResult.RESULT_HEALTHY;
    }


    @Override
    protected InputStream getConfigurationStream(final Class configClass, final Name relativePath)
            throws ConfigurationException
    {
        String configFile = nameToFile(configClass, relativePath);
        Path p;
        if ( configPath != null ) {
            p = configPath.resolve(configFile);
        } else {
            p = Paths.get(configFile);
        }
        LOG.debug("Getting configuration for {} from {}", configClass.getSimpleName(), p);
        try {
            return Files.newInputStream(p);
        } catch (IOException e) {
            throw new ConfigurationException("Cannot read config file: " + configFile, e);
        }
    }


    /**
     * Convert a (partial) ServicePath into a file name to access. The file names are
     * in the format "config_group_subgroup_appid_ConfigurationClass.conf".
     * @param configClass the configuration class to try and acquire
     * @param servicePath the partial or complete ServicePath in Name format
     * @return the constructed file name to try and access
     */
    private String nameToFile(final Class configClass, final Name servicePath)
    {
        StringBuilder builder = new StringBuilder("config");
        for(String component : servicePath) {
            builder.append("_").append(component);
        }
        builder.append("_").append(configClass.getSimpleName()).append(FILE_EXTENSION);
        return builder.toString();
    }
}
