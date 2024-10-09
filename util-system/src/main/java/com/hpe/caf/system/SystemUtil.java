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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
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
     * Retrieves a value either directly from an environment variable or from the contents of a file
     * specified by another environment variable.
     *
     * This method first checks for a direct value using the provided {@code envKey}. If that's not found,
     * it looks for a file path using {@code envFileKey} and reads the contents of that file.
     *
     * @param envKey     The environment variable key to check for a direct value. Must not be null.
     * @param envFileKey The environment variable key that contains a file path whose contents should be
     *                   read if {@code envKey} is not found. Must not be null.
     *
     * @return The value from the environment variable, the contents of the specified file (trimmed),
     *         or {@code null} if neither value is found.
     *
     * @throws IOException If there's an error reading the file specified by {@code envFileKey}
     * @throws NullPointerException If either {@code envKey} or {@code envFileKey} is null
     */
    public static String getEnvOrFileContentFromEnv(final String envKey, final String envFileKey) throws IOException
    {
        Objects.requireNonNull(envKey, "envKey");
        Objects.requireNonNull(envFileKey, "envFileKey");
        final String value = System.getenv(envKey);
        if (value != null) {
            LOG.debug("Found value for key '{}' in environment variables", envKey);
            return value;
        } else {
            return getFileContentFromEnv(envFileKey);
        }
    }

    /**
     * Reads and returns the contents of a file whose path is specified by an environment variable.
     *
     * This method looks up an environment variable using the provided {@code key}, treats its value
     * as a file path, and returns the contents of that file. The file contents are trimmed of leading
     * and trailing whitespace.
     *
     * @param key The environment variable key containing the file path. Must not be null.
     *
     * @return The trimmed contents of the file specified by the environment variable,
     *         or {@code null} if the environment variable is not found
     *
     * @throws IOException If there's an error reading the file
     * @throws NullPointerException If {@code key} is null
     */
    public static String getFileContentFromEnv(final String key) throws IOException
    {
        Objects.requireNonNull(key, "key");
        final String value = System.getenv(key);
        if (value != null) {
            LOG.debug("Found value for key '{}' in environment variables, attempting to read file content...", key);
            final String fileContents = Files.readString(Paths.get(value)).trim();
            LOG.debug("Successfully read file content of key '{}'", key);
            return fileContents;
        } else {
            LOG.debug("No value found for key '{}' in environment variables", key);
            return null;
        }
    }
}
