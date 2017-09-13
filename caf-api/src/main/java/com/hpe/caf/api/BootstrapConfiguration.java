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
package com.hpe.caf.api;

import com.hpe.caf.naming.ServicePath;

/**
 * A bootstrap configuration is a method of providing basic, initial startup configuration. This is more crude than a full
 * ConfigurationProvider, and only supports trivial key/value lookups.
 */
public interface BootstrapConfiguration
{
    /**
     * Application identifier configuration parameter key. This must be in the format /a/b.
     */
    String CONFIG_APP_NAME = "CAF_APPNAME";
    /**
     * Old application identifier configuration parameter key.
     */
    @Deprecated
    String OLD_CONFIG_APP_NAME = "caf.appname";

    /**
     * Determine if a configuration parameter has been set or not.
     *
     * @param key the config parameter to check
     * @return whether the config parameter is set or not
     */
    boolean isConfigurationPresent(String key);

    /**
     * Return the value of the requested configuration parameter.
     *
     * @param key the config parameter to lookup
     * @return the value of the config parameter
     * @throws ConfigurationException if the configuration parameter is not set
     */
    String getConfiguration(String key)
        throws ConfigurationException;

    /**
     * Return the integer representation of the requested configuration parameter.
     *
     * @param key the config parameter to lookup
     * @return the value of the config parameter as an integer
     * @throws ConfigurationException if the configuration parameter is not set
     */
    int getConfigurationInteger(String key)
        throws ConfigurationException;

    /**
     * Return the integer representation of the requested configuration parameter, between the minimum and maximum integers specified.
     *
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
     *
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
