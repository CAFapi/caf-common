package com.hpe.caf.api;


import com.hpe.caf.naming.ServicePath;


/**
 * A bootstrap configuration is a method of providing basic, initial startup configuration.
 * This is more crude than a full ConfigurationProvider, and only supports trivial key/value
 * lookups.
 * @since 9.0
 */
public interface BootstrapConfiguration
{
    /**
     * Application identifier configuration parameter key.
     * This must be in the format /a/b.
     */
    String CONFIG_APP_NAME = "CAF_APPNAME";
    /**
     * Old application identifier configuration parameter key.
     */
    @Deprecated
    String OLD_CONFIG_APP_NAME = "caf.appname";


    /**
     * Determine if a configuration parameter has been set or not.
     * @param key the config parameter to check
     * @return whether the config parameter is set or not
     */
    boolean isConfigurationPresent(String key);


    /**
     * Return the value of the requested configuration parameter.
     * @param key the config parameter to lookup
     * @return the value of the config parameter
     * @throws ConfigurationException if the configuration parameter is not set
     */
    String getConfiguration(String key)
        throws ConfigurationException;


    /**
     * Return the integer representation of the requested configuration parameter.
     * @param key the config parameter to lookup
     * @return the value of the config parameter as an integer
     * @throws ConfigurationException if the configuration parameter is not set
     */
    int getConfigurationInteger(String key)
        throws ConfigurationException;


    /**
     * Return the integer representation of the requested configuration parameter, between
     * the minimum and maximum integers specified.
     * @param key the config parameter to lookup
     * @param min the lower bound of the integer to be returned
     * @param max the upper bound of the integer to be returned
     * @return the value of the config parameter as an integer, between the limits specified
     * @throws ConfigurationException if the configuration parameter is not set
     */
    int getConfigurationInteger(String key, int min, int max)
        throws ConfigurationException;


    /**
     * Return the boolean representation of the requested configuration parameter.
     * @param key the config parameter to lookup
     * @return the value of the config parameter as an integer
     * @throws ConfigurationException if the configuration parameter is not set
     */
    boolean getConfigurationBoolean(String key)
        throws ConfigurationException;


    /**
     * @return an object representing the fully qualified service path of this instance
     * @throws ConfigurationException if the ServicePath cannot be acquired
     */
    ServicePath getServicePath()
        throws ConfigurationException;
}
