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
package com.github.cafapi.common.config.sources.file;

import com.github.cafapi.common.api.BootstrapConfiguration;
import com.github.cafapi.common.api.Cipher;
import com.github.cafapi.common.api.ConfigurationException;
import com.github.cafapi.common.api.ConfigurationSourceProvider;
import com.github.cafapi.common.api.Decoder;
import com.github.cafapi.common.api.ManagedConfigurationSource;
import com.github.cafapi.common.util.naming.ServicePath;

public class FileConfigurationSourceProvider implements ConfigurationSourceProvider
{
    @Override
    public ManagedConfigurationSource getConfigurationSource(final BootstrapConfiguration bootstrapProvider, final Cipher cipher,
                                                             final ServicePath servicePath, final Decoder decoder)
        throws ConfigurationException
    {
        return new FileConfigurationSource(bootstrapProvider, cipher, servicePath, decoder);
    }
}
