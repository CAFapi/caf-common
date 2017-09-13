/*
 * Copyright 2015-2017 EntIT Software LLC, a Micro Focus company.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hpe.caf.config.file;

import com.hpe.caf.api.BootstrapConfiguration;
import com.hpe.caf.api.CafConfigurationSource;
import com.hpe.caf.api.Cipher;
import com.hpe.caf.api.ConfigurationException;
import com.hpe.caf.api.Decoder;
import com.hpe.caf.api.FileExtensions;
import com.hpe.caf.api.HealthResult;
import com.hpe.caf.naming.Name;
import com.hpe.caf.naming.ServicePath;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is a ConfigurationProvider that reads from a local disk file. If the bootstrap parameter config.path is set, it will read the
 * config files from the specified directory, otherwise it will use the working dir.
 *
 * If retrieving the configuration class "TestConfiguration", using the ServicePath /a/b, it will expect the file to be called
 * "cfg_a_b_TestConfiguration".
 */
public class FileConfigurationSource extends CafConfigurationSource
{
    public static final String CONFIG_PATH = "CAF_CONFIG_PATH";
    @Deprecated
    public static final String OLD_CONFIG_PATH = "config.path";
    private Path configPath;
    private static final Logger LOG = LoggerFactory.getLogger(FileConfigurationSource.class);
    private static final ArrayList<String> fileNameDelimiters = new ArrayList<>();
    private final String[] fileExtensions;

    static {
        fileNameDelimiters.add("_");
        fileNameDelimiters.add("~");
    }

    /**
     * {@inheritDoc}
     */
    public FileConfigurationSource(
        final BootstrapConfiguration bootstrap,
        final Cipher cipher,
        final ServicePath servicePath,
        final Decoder decoder
    ) throws ConfigurationException
    {
        super(bootstrap, cipher, servicePath, decoder);
        try {
            configPath = FileSystems.getDefault().getPath(getConfigPath(bootstrap));
        } catch (final InvalidPathException e) {
            throw new ConfigurationException("Invalid configuration path", e);
        }
        fileExtensions = getFileExtensions(decoder);
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
        // Try each configuration source filename format delimiter in attempt to load the configuration source
        for (final String fileNameDelimiter : fileNameDelimiters) {
            for (final String fileExtension : fileExtensions) {
                String configFile = nameToFile(configClass, relativePath, fileNameDelimiter, fileExtension);
                Path p;
                if (configPath != null) {
                    p = configPath.resolve(configFile);
                } else {
                    p = Paths.get(configFile);
                }
                LOG.debug("Getting configuration for {} from {}", configClass.getSimpleName(), p);
                // Check if the file exists and try to return it as an input stream
                if (Files.exists(p)) {
                    try {
                        return Files.newInputStream(p);
                    } catch (final IOException ioe) {
                        throw new ConfigurationException("Cannot read config file: " + configFile, ioe);
                    }
                }
            }
        }
        throw new ConfigurationException("Cannot find config file for " + configClass.getSimpleName());
    }

    /**
     * Retrieves the file extensions to use with the specified decoder.
     * <p>
     * If the specified Decoder doesn't have any file extensions explicitly associated with it, then a single empty string is returned.
     */
    private static String[] getFileExtensions(final Decoder decoder)
    {
        final Class<? extends Decoder> decoderClass = decoder.getClass();
        final FileExtensions fileExtensionsAnnotation = decoderClass.getAnnotation(FileExtensions.class);

        return (fileExtensionsAnnotation == null)
            ? new String[]{""}
            : fileExtensionsAnnotation.value();
    }

    /**
     * Convert a (partial) ServicePath into a file name to access. The file names are in the format
     * "cfg_group_subgroup_appid_ConfigurationClass[.ext]".
     *
     * @param configClass the configuration class to try and acquire
     * @param servicePath the partial or complete ServicePath in Name format
     * @param fileNameDelimiter the symbol used to separate cfg, group, subgroup, appid and ConfigurationClass
     * @param extension the file extension, or an empty string if there is no extension
     * @return the constructed file name to try and access
     */
    private String nameToFile(final Class configClass, final Name servicePath, final String fileNameDelimiter, final String extension)
    {
        StringBuilder builder = new StringBuilder("cfg");
        for (final String component : servicePath) {
            builder.append(fileNameDelimiter).append(component);
        }
        builder.append(fileNameDelimiter).append(configClass.getSimpleName());
        if (!extension.isEmpty()) {
            builder.append('.').append(extension);
        }
        return builder.toString();
    }

    private String getConfigPath(final BootstrapConfiguration bootstrap)
        throws ConfigurationException
    {
        String ret;
        if (bootstrap.isConfigurationPresent(CONFIG_PATH)) {
            ret = bootstrap.getConfiguration(CONFIG_PATH);
        } else if (bootstrap.isConfigurationPresent(OLD_CONFIG_PATH)) {
            ret = bootstrap.getConfiguration(OLD_CONFIG_PATH);
        } else {
            throw new ConfigurationException("Configuration parameter " + CONFIG_PATH + " not present");
        }
        return ret;
    }
}
