/*
 * Copyright 2015-2018 Micro Focus or one of its affiliates.
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
 * Simple boilerplate to return a ConfigurationSource.
 */
public interface ConfigurationSourceProvider
{
    /**
     * Acquire a ConfigurationSource implementation.
     *
     * @param bootstrapProvider used to provide initial configuration of the ConfigurationSource
     * @param cipher used to perform any necessary decryption in the configuration objects
     * @param servicePath used to acquire service-specific configuration
     * @param decoder used to decode data from the source into objects
     * @return a new ConfigurationSource instance
     * @throws ConfigurationException if the ConfigurationSource could not be created
     */
    ManagedConfigurationSource getConfigurationSource(
        BootstrapConfiguration bootstrapProvider,
        Cipher cipher,
        ServicePath servicePath,
        Decoder decoder
    ) throws ConfigurationException;
}
