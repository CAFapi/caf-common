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
package com.hpe.caf.secret;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class SecretUtil
{
    private static final Logger LOG = LoggerFactory.getLogger(com.hpe.caf.secret.SecretUtil.class);
    private static final String FILE_POSTFIX = "_FILE";
    private static final String CAF_READ_SECRETS_FROM_ENV = "CAF_READ_SECRETS_FROM_ENV";
    private static final String CAF_READ_SECRETS_FROM_FILE = "CAF_READ_SECRETS_FROM_FILE";

    private SecretUtil()
    {
    }

    /**
     * Retrieves a secret value from various sources based on configuration:
     * <ol>
     *   <li>Environment variables (if CAF_READ_SECRETS_FROM_ENV is true or not set)</li>
     *   <li>File content (if CAF_READ_SECRETS_FROM_FILE is true and path specified by environment variable with "_FILE" suffix)</li>
     * </ol>
     *
     * For example, for a key "DATABASE_PASSWORD":
     * <ul>
     *   <li>First checks environment variable (if enabled): DATABASE_PASSWORD</li>
     *   <li>Then checks file path in environment variable (if enabled): DATABASE_PASSWORD_FILE</li>
     * </ul>
     *
     * @param key The base key to look up the secret value. Must not be null.
     * @return The secret value if found in any of the enabled sources, or null if not found.
     *         If found in a file, the content is trimmed of leading and trailing whitespace.
     * @throws IOException If there is an error reading the file when using the _FILE variant
     * @throws NullPointerException If the key parameter is null
     */
    public static String getSecret(final String key) throws IOException
    {
        Objects.requireNonNull(key, "key");

        // Check if reading from environment is enabled (defaults to true)
        final boolean readFromEnv = Boolean.parseBoolean(System.getenv().getOrDefault(CAF_READ_SECRETS_FROM_ENV, "true"));
        if (readFromEnv) {
            // Try environment variable
            final String envValue = getFromEnvironment(key);
            if (envValue != null) {
                return envValue;
            }
        }

        // Check if reading from file via environment is enabled (defaults to false)
        final boolean readFromFileViaEnv = Boolean.parseBoolean(System.getenv().getOrDefault(CAF_READ_SECRETS_FROM_FILE, "false"));
        if (readFromFileViaEnv) {
            // Try file reference (via environment variable)
            final String fileValue = getFromFileViaEnvironment(key);
            if (fileValue != null) {
                return fileValue;
            }
        }

        LOG.debug("No value found for key '{}' in any enabled location", key);
        return null;
    }

    /**
     * Retrieves a secret value from various sources based on configuration:
     * <ol>
     *   <li>Environment variables (if CAF_READ_SECRETS_FROM_ENV is true or not set)</li>
     *   <li>File content (if CAF_READ_SECRETS_FROM_FILE is true and path specified by environment variable with "_FILE" suffix)</li>
     * </ol>
     *
     * For example, for a key "DATABASE_PASSWORD":
     * <ul>
     *   <li>First checks environment variable (if enabled): DATABASE_PASSWORD</li>
     *   <li>Then checks file path in environment variable (if enabled): DATABASE_PASSWORD_FILE</li>
     * </ul>
     *
     * @param key The base key to look up the secret value. Must not be null.
     * @param defaultValue The value to return if no secret is found. Must not be null.
     * @return The secret value if found in any of the enabled sources, or defaultValue if not found.
     *         If found in a file, the content is trimmed of leading and trailing whitespace.
     * @throws IOException If there is an error reading the file when using the _FILE variant
     * @throws NullPointerException If either the key or defaultValue parameters are null
     */
    public static String getSecret(final String key, final String defaultValue) throws IOException
    {
        Objects.requireNonNull(defaultValue, "defaultValue");
        final String value = getSecret(key);
        if (value != null) {
            return value;
        } else {
            LOG.debug("Returning default value for key '{}'", key);
            return defaultValue;
        }
    }

    private static String getFromEnvironment(final String key)
    {
        final String value = System.getenv(key);
        if (value != null) {
            LOG.debug("Found value for key '{}' in environment variables", key);
            return value;
        }
        return null;
    }

    private static String getFromFileViaEnvironment(final String key) throws IOException
    {
        final String keyWithFilePostfix = key + FILE_POSTFIX;
        final String filePath = System.getenv(keyWithFilePostfix);
        if (filePath != null) {
            LOG.debug("Found value for key '{}' in environment variables, reading file content...", keyWithFilePostfix);
            final String fileContent = Files.readString(Paths.get(filePath)).trim();
            LOG.debug("Successfully read file content for key '{}'", keyWithFilePostfix);
            return fileContent;
        }
        return null;
    }
}