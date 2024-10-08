/*
 * Copyright 2015-2024 Open Text.
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
package com.hpe.caf.system;

import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class SystemUtil
{
    private static final Logger LOG = LoggerFactory.getLogger(SystemUtil.class);

    private SystemUtil()
    {
    }

    /**
     * Retrieves a value from either system environment variables or system properties.
     * First checks the system environment variables, and if not found there,
     * falls back to checking system properties.
     *
     * @param key The name of the environment variable or system property to retrieve
     * @return The value of the environment variable or system property.
     *         Returns null if the key is not found in either location.
     */
    public static String getEnvOrProp(final String key) {
        Objects.requireNonNull(key, "key");
        final String env = System.getenv(key);
        if (env != null) {
            LOG.debug("Found value for key '{}' in environment variables", key);
            return env;
        } else {
            final String property = System.getProperty(key);
            if (property != null) {
                LOG.debug("Found value for key '{}' in system properties", key);
                return property;
            } else {
                LOG.debug("No value found for key '{}' in environment variables or system properties", key);
                return null;
            }
        }
    }

    /**
     * Retrieves a value from either system environment variables or system properties,
     * with a fallback default value if not found in either location.
     * First checks the system environment variables, then system properties,
     * and finally returns the default value if neither contains the key.
     *
     * @param key The name of the environment variable or system property to retrieve
     * @param defaultValue The default value to return if the key is not found
     * @return The value from environment variables, system properties, or the default value.
     *         Never returns null as the default value is used when no value is found.
     */
    public static String getEnvOrProp(final String key, final String defaultValue) {
        Objects.requireNonNull(defaultValue, "defaultValue");
        final String value = getEnvOrProp(key);
        if (value != null) {
            return value;
        } else {
            LOG.debug("Using default value for key '{}'", key);
            return defaultValue;
        }
    }
}
