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

import org.mockito.Mockito;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Unit tests for CafConfigurationDecoderProvider
 */
public class CafConfigurationDecoderProviderTest
{
    /**
     * Testing behavior when no config key is set.
     *
     * @throws ConfigurationException
     */
    @Test
    public void defaultReturnedWhenNoKeySetTest() throws ConfigurationException
    {
        final BootstrapConfiguration mockedBConfiguration = Mockito.mock(BootstrapConfiguration.class);
        Mockito.when(mockedBConfiguration.isConfigurationPresent(Mockito.anyString())).thenReturn(false);

        final CafConfigurationDecoderProvider provider = new CafConfigurationDecoderProvider();
        final Decoder mockedDecoder = Mockito.mock(Decoder.class);
        final Decoder resultDecoder = provider.getDecoder(mockedBConfiguration, mockedDecoder);
        Assert.assertEquals(resultDecoder, mockedDecoder, "Expecting provided default decoder provided to have been returned.");
    }

    /**
     * Testing that expected decoder is returned based on value of config key.
     *
     * @throws ConfigurationException
     */
    @Test
    public void returnsSetDecoderTest() throws ConfigurationException
    {
        final String DECODER_CONFIG_KEY = "CAF_CONFIG_DECODER";

        final BootstrapConfiguration mockedBConfiguration = Mockito.mock(BootstrapConfiguration.class);
        Mockito.when(mockedBConfiguration.isConfigurationPresent(DECODER_CONFIG_KEY)).thenReturn(true);
        Mockito.when(mockedBConfiguration.getConfiguration(DECODER_CONFIG_KEY)).thenReturn(TestDecoder_1.class.getSimpleName());

        final Decoder mockedDecoder = Mockito.mock(Decoder.class);
        final CafConfigurationDecoderProvider provider = new CafConfigurationDecoderProvider();

        Decoder resultDecoder = provider.getDecoder(mockedBConfiguration, mockedDecoder);
        Assert.assertNotEquals(resultDecoder, mockedDecoder, "Default decoder should not have been returned.");
        Assert.assertTrue(resultDecoder instanceof TestDecoder_1, "Returned decoder should be the type expected");

        //check code isn't just returning the first implementation found of Decoder by requesting a different
        //decoder also on the classpath
        Mockito.when(mockedBConfiguration.getConfiguration(DECODER_CONFIG_KEY)).thenReturn(TestDecoder_2.class.getSimpleName());
        resultDecoder = provider.getDecoder(mockedBConfiguration, mockedDecoder);
        Assert.assertNotEquals(resultDecoder, mockedDecoder, "Default decoder should not have been returned.");
        Assert.assertTrue(resultDecoder instanceof TestDecoder_2, "Returned decoder should be the type expected");
    }

    /**
     * Testing behavior when config key decoder cannot be found.
     *
     * @throws ConfigurationException
     */
    @Test
    public void cannotFindDecoderTest() throws ConfigurationException
    {
        final String DECODER_CONFIG_KEY = "CAF_CONFIG_DECODER";

        // This mocked decoder doesn't have a META-INF services entry so isn't going to be found by service loader.
        final Decoder unfindableDecoder = Mockito.mock(Decoder.class);

        final BootstrapConfiguration mockedBConfiguration = Mockito.mock(BootstrapConfiguration.class);
        Mockito.when(mockedBConfiguration.isConfigurationPresent(DECODER_CONFIG_KEY)).thenReturn(true);
        Mockito.when(mockedBConfiguration.getConfiguration(DECODER_CONFIG_KEY)).thenReturn(unfindableDecoder.getClass().getSimpleName());

        final Decoder mockedDecoder = Mockito.mock(Decoder.class);
        final CafConfigurationDecoderProvider provider = new CafConfigurationDecoderProvider();

        try {
            provider.getDecoder(mockedBConfiguration, mockedDecoder);
        } catch (final RuntimeException ex) {
            Assert.assertEquals(
                ex.getMessage(),
                "Unable to get Decoder using " + DECODER_CONFIG_KEY + " value: " + mockedDecoder.getClass().getSimpleName());
        }
    }
}
