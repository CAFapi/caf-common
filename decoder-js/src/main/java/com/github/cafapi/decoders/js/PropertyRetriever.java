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
package com.github.cafapi.decoders.js;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Provides access to properties requested during decoding.
 */
public class PropertyRetriever
{
    /**
     * Gets the value to use for the specified property.
     *
     * @param key Name of property.
     * @return Value of property specified.
     */
    public static String getenv(String key)
    {
        return System.getenv(key);
    }

    /**
     * Gets the value associated with the specified key. If an environment variable with the specified key exists then the value returned
     * is the environment variable value. If it does not exist, but an environment variable with the specified key suffixed with "_FILE"
     * exists, then the environment variable value is taken to be a file path, and the value returned is the contents of the specified
     * file.
     *
     * @param key
     * @return
     * @throws IOException
     */
    public static String getenvOrFile(final String key) throws IOException
    {
        final String value = System.getenv(key);

        final String fileKey = key + "_FILE";
        final String fileValue = System.getenv(fileKey);

        if (fileValue == null) {
            return value;
        } else if (value == null) {
            // If the file doesn't exist should we return null rather than throwing an exception???
            // I'd like to try it out with Docker Secrets - the secret not existing.
            // Returning null might be more in line with environment variable behaviour - need to think about it more.
            final Path filePath = Paths.get(fileValue);
            final byte[] fileBytes = Files.readAllBytes(filePath);
            final ByteBuffer byteBuffer = ByteBuffer.wrap(fileBytes);
            final CharsetDecoder utfDecoder = StandardCharsets.UTF_8.newDecoder();
            final CharBuffer charBuffer = utfDecoder.decode(byteBuffer);
            return charBuffer.toString();
        } else {
            throw new RuntimeException(String.format("Only one of %s and %s can be set", key, fileKey));
        }
    }
}
