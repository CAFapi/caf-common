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
package com.hpe.caf.api;

import com.hpe.caf.util.ModuleProvider;

/**
 * Implements a default mechanism for selecting a Decoder to use when interpreting configuration files.
 */
public class CafConfigurationDecoderProvider implements ConfigurationDecoderProvider
{
    /**
     * Returns the Decoder that should be used to interpret the configuration files.
     *
     * @param bootstrap used to provide basic, initial startup configuration
     * @param defaultDecoder the Decoder to use if one is not configured
     * @return the Decoder that should be used to interpret the configuration files
     */
    @Override
    public Decoder getDecoder(final BootstrapConfiguration bootstrap, final Decoder defaultDecoder)
    {
        final String DECODER_CONFIG_KEY = "CAF_CONFIG_DECODER";

        final String decoder;
        try {
            // Return the specified default Decoder if none has been configured
            if (!bootstrap.isConfigurationPresent(DECODER_CONFIG_KEY)) {
                return defaultDecoder;
            }

            // Lookup the Decoder to use
            decoder = bootstrap.getConfiguration(DECODER_CONFIG_KEY);

        } catch (final ConfigurationException ex) {
            // Throw a RuntimeException since this shouldn't happen
            // (since isConfigurationPresent() has already been called)
            throw new RuntimeException(ex);
        }

        try {
            // Retrieve the Decoder using the ModuleProvider
            return ModuleProvider.getInstance().getModule(Decoder.class, decoder);
        } catch (final NullPointerException ex) {
            throw new RuntimeException("Unable to get Decoder using " + DECODER_CONFIG_KEY + " value: " + decoder, ex);
        }
    }
}
