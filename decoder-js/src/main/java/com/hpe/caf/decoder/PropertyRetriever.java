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

import com.hpe.caf.system.SystemUtil;

/**
 * Provides access to properties requested during decoding.
 */
public class PropertyRetriever {
    /**
     * Gets the value to use for the specified environment variable.
     * @param key Name of environment variable.
     * @return Value of environment variable specified.
     */
    public static String getenv(String key){
        return System.getenv(key);
    }

    /**
     * Gets the value to use for the specified environment variable or property.
     * @param key Name of environment variable or property.
     * @param propPrefix The prefix to use when looking up the system property. If null, the key is used as-is.
     * @return Value of environment variable or property specified.
     */
    public static String getenvorprop(String key, String propPrefix){
        return SystemUtil.getEnvOrProp(key, propPrefix);
    }
}
