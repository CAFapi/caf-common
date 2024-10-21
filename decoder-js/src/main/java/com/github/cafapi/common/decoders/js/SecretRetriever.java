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
package com.github.cafapi.common.decoders.js;

import java.io.IOException;

import com.github.cafapi.common.util.secret.SecretUtil;

/**
 * Provides access to secrets requested during decoding.
 */
public class SecretRetriever
{
    /**
     * Gets the value to use for the specified secret.
     * @param key Name of secret.
     * @return Value of secret specified.
     * @throws IOException If there is an error reading the file when using the _FILE variant
     * @throws NullPointerException If the key parameter is null
     */
    public static String getSecret(final String key) throws IOException {
        return SecretUtil.getSecret(key);
    }
}
