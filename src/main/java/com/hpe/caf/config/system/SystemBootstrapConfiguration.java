package com.hpe.caf.config.system;


import com.hpe.caf.api.BootstrapConfiguration;
import com.hpe.caf.api.ConfigurationException;
import com.hpe.caf.naming.ServicePath;

import javax.naming.InvalidNameException;
import java.util.Objects;


/**
 * Provides bootstrap configuration from Java system properties and environment variables.
 * Note that system properties will always override if present.
 */
public class SystemBootstrapConfiguration implements BootstrapConfiguration
{
    public static final String ENV_MARATHON_APP_ID = "MARATHON_APP_ID";


    @Override
    public boolean isConfigurationPresent(final String key)
    {
        return getProp(key) != null;
    }


    @Override
    public String getConfiguration(final String key)
            throws ConfigurationException
    {
        String ret = getProp(key);
        if ( ret == null ) {
            throw new ConfigurationException("Configuration parameter not found: " + key);
        }
        return ret;
    }


    @Override
    public int getConfigurationInteger(final String key)
            throws ConfigurationException
    {
        try {
            return Integer.parseInt(getConfiguration(key));
        } catch (NumberFormatException e) {
            throw new ConfigurationException("Configuration value is not an integer: " + key, e);
        }
    }


    @Override
    public int getConfigurationInteger(final String key, final int min, final int max)
            throws ConfigurationException
    {
        return Math.max(min, Math.min(max, getConfigurationInteger(key)));
    }


    @Override
    public boolean getConfigurationBoolean(final String key)
            throws ConfigurationException
    {
        return Boolean.parseBoolean(getConfiguration(key));
    }


    @Override
    public ServicePath getServicePath()
            throws ConfigurationException
    {
        ServicePath path;
        try {
            if ( isConfigurationPresent(BootstrapConfiguration.CONFIG_APP_NAME) ) {
                path = new ServicePath(getConfiguration(BootstrapConfiguration.CONFIG_APP_NAME));
            } else if ( isConfigurationPresent(BootstrapConfiguration.OLD_CONFIG_APP_NAME) ) {
                path = new ServicePath(getConfiguration(BootstrapConfiguration.OLD_CONFIG_APP_NAME));
            } else {
                path = new ServicePath(getConfiguration(ENV_MARATHON_APP_ID));
            }
        } catch (InvalidNameException e) {
            throw new ConfigurationException("Cannot get service path", e);
        }
        return path;
    }


    private String getProp(final String key)
    {
        Objects.requireNonNull(key);
        return System.getProperty(key, System.getenv(key));
    }
}
