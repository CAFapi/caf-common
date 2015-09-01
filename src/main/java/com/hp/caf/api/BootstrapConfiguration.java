package com.hp.caf.api;


/**
 * A bootstrap configuration is a method of providing basic, initial startup configuration.
 * This is more crude than a full ConfigurationProvider, and only supports trivial key/value
 * lookups.
 */
public abstract class BootstrapConfiguration
{
    /**
     * Application identifier configuration parameter key.
     * This must be in the format /a/b.
     */
    public static final String CONFIG_APP_NAME = "caf.appname";


    /**
     * Determine if a configuration parameter has been set or not.
     * @param key the config parameter to check
     * @return whether the config parameter is set or not
     */
    public abstract boolean isConfigurationPresent(final String key);


    /**
     * Return the value of the requested configuration parameter.
     * @param key the config parameter to lookup
     * @return the value of the config parameter
     * @throws ConfigurationException if the configuration parameter is not set
     */
    public abstract String getConfiguration(final String key)
        throws ConfigurationException;


    /**
     * Return the integer representation of the requested configuration parameter.
     * @param key the config parameter to lookup
     * @return the value of the config parameter as an integer
     * @throws ConfigurationException if the configuration parameter is not set
     */
    public abstract int getConfigurationInteger(final String key)
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
    public abstract int getConfigurationInteger(final String key, final int min, final int max)
        throws ConfigurationException;


    /**
     * Return the boolean representation of the requested configuration parameter.
     * @param key the config parameter to lookup
     * @return the value of the config parameter as an integer
     * @throws ConfigurationException if the configuration parameter is not set
     */
    public abstract boolean getConfigurationBoolean(final String key)
        throws ConfigurationException;


    /**
     * @return an object representing the fully qualified service path of this instance
     * @throws ConfigurationException if the ServicePath cannot be acquired
     */
    public abstract ServicePath getServicePath()
        throws ConfigurationException;
}
