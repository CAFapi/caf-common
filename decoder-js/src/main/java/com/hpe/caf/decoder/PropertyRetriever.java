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
package com.hpe.caf.decoder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Provides access to properties requested during decoding.
 */
public class PropertyRetriever {

    private static final Logger LOG = LoggerFactory.getLogger(PropertyRetriever.class);

    /**
     * Gets the value to use for the specified property.
     * @param key Name of property.
     * @return Value of property specified.
     */
    public static String getenv(String key){
        return System.getenv(key);
    }

    /**
     * Gets the contents of the file pointed to by the specified environment variable.
     * @param key Name of environment variable containing the path of the file to read from.
     * @return Contents of the file pointed to by the specified environment variable, or null if the specified environment variable is
     * null or empty.
     * @throws IOException If the file could not be read for any reason.
     */
    public static String getenvfile(String key) throws IOException {
        final String env = getenv(key);
        if (env != null && !env.isEmpty()) {
            final String fileContents = Files.readString(Paths.get(env)).trim();
            LOG.debug("Successfully read file contents of {}={}", key, env);
            return fileContents;
        } else {
            LOG.debug("The {} environment variable is null or empty, returning null", key);
            return null;
        }
    }
}
