/*
 * Copyright 2015-2021 Micro Focus or one of its affiliates.
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
package com.hpe.caf.config.rest;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hpe.caf.api.BootstrapConfiguration;
import com.hpe.caf.api.Codec;
import com.hpe.caf.api.ConfigurationException;
import com.hpe.caf.api.ConfigurationSource;
import com.hpe.caf.api.HealthStatus;
import com.hpe.caf.api.ManagedConfigurationSource;
import com.hpe.caf.cipher.NullCipher;
import com.hpe.caf.codec.JsonCodec;
import com.hpe.caf.naming.ServicePath;
import io.dropwizard.testing.junit.DropwizardClientRule;
import org.hibernate.validator.constraints.NotBlank;
import org.junit.ClassRule;
import org.junit.Test;
import org.mockito.Mockito;

import javax.naming.InvalidNameException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import static org.junit.Assert.assertEquals;

public class RestConfigurationSourceTest
{
    /**
     * This sets up a skeleton Dropwizard app for the purposes of this test alone.
     */
    @ClassRule
    public static final DropwizardClientRule drop = new DropwizardClientRule(new TestConfigResource(), new AppWideTestConfigResource());
    private static final String DEFAULT_TEST_STRING = "test123";
    private static final String ALTERNATE_TEST_STRING = "test456";
    private static final String APP = "testApp";
    private static final String WORKER = "testWorker";
    private static final String INVALID_WORKER = "invalidTestWorker";
    private final Codec codec = new JsonCodec();

    public ServicePath getId(final String worker)
        throws InvalidNameException
    {
        return new ServicePath("testApp/" + worker);
    }

    /**
     * Util method for getting a BootstrapConfiguration with some defaults.
     *
     * @return a BootstrapConfiguratio nfor use with the RestConfigurationProvider with everything but the REST endpoint set
     */
    private BootstrapConfiguration getBootstrap()
    {
        BootstrapConfiguration ret = Mockito.mock(BootstrapConfiguration.class);
        return ret;
    }

    /**
     * Set the REST endpoint in a BoostrapConfiguration for use with the test
     *
     * @param bs the config to change
     */
    private void setRestEndpoint(final BootstrapConfiguration bs)
        throws ConfigurationException
    {
        Mockito.when(bs.isConfigurationPresent(Mockito.eq(RestConfigurationSource.CONFIG_REST_HOST))).thenReturn(true);
        Mockito.when(bs.getConfiguration(Mockito.eq(RestConfigurationSource.CONFIG_REST_HOST))).thenReturn(drop.baseUri().toString());
    }

    /**
     * Full "happy" path test. Configure the ConfigurationProvider properly and try and get the configuration.
     *
     * @throws ConfigurationException
     */
    @Test
    public void testGetConfiguration()
        throws ConfigurationException, InvalidNameException
    {
        BootstrapConfiguration bc = getBootstrap();
        setRestEndpoint(bc);
        ConfigurationSource rcp = new RestConfigurationSource(bc, new NullCipher(), getId(WORKER), codec);
        TestConfig result = rcp.getConfiguration(TestConfig.class);
        assertEquals(DEFAULT_TEST_STRING, result.getTestData());
    }

    /**
     * Try retrieving configuration from another scope.
     *
     * @throws ConfigurationException
     */
    @Test
    public void testGetConfigurationScope()
        throws ConfigurationException, InvalidNameException
    {
        BootstrapConfiguration bc = getBootstrap();
        setRestEndpoint(bc);
        ConfigurationSource rcp = new RestConfigurationSource(bc, new NullCipher(), getId(WORKER), codec);
        AppWideTestConfig result = rcp.getConfiguration(AppWideTestConfig.class);
        assertEquals(ALTERNATE_TEST_STRING, result.getTestData());
    }

    @Test(expected = ConfigurationException.class)
    public void testGetInvalidConfiguration()
        throws ConfigurationException, InvalidNameException
    {
        BootstrapConfiguration bc = getBootstrap();
        setRestEndpoint(bc);
        RestConfigurationSource rcp = new RestConfigurationSource(bc, new NullCipher(), getId(INVALID_WORKER), codec);
        rcp.setRetries(0);
        TestConfig result = rcp.getConfiguration(TestConfig.class);
    }

    /**
     * Try and get an invalid configuration from the remote server and check an exception is thrown.
     *
     * @throws ConfigurationException
     */
    @Test(expected = ConfigurationException.class)
    public void testGetWrongConfiguration()
        throws ConfigurationException, InvalidNameException
    {
        BootstrapConfiguration bc = getBootstrap();
        setRestEndpoint(bc);
        RestConfigurationSource rcp = new RestConfigurationSource(bc, new NullCipher(), getId(WORKER), codec);
        rcp.setRetries(0);
        rcp.getConfiguration(OtherTestConfig.class);
    }

    /**
     * Try and init the RestConfigurationProvider without all the required config parameters. Check an exception is thrown.
     *
     * @throws ConfigurationException
     */
    @Test(expected = ConfigurationException.class)
    public void testGetConfigurationBadInit()
        throws ConfigurationException, InvalidNameException
    {
        BootstrapConfiguration bc = getBootstrap();
        ConfigurationSource rcp = new RestConfigurationSource(bc, new NullCipher(), getId(WORKER), codec);
        rcp.getConfiguration(OtherTestConfig.class);
    }

    /**
     * Test the health check by pointing at the test server.
     *
     * @throws ConfigurationException
     */
    @Test
    public void testHealthCheckHealthy()
        throws ConfigurationException, InvalidNameException
    {
        BootstrapConfiguration bc = getBootstrap();
        setRestEndpoint(bc);
        ManagedConfigurationSource rcp = new RestConfigurationSource(bc, new NullCipher(), getId(WORKER), codec);
        assertEquals(HealthStatus.HEALTHY, rcp.healthCheck().getStatus());
    }

    /**
     * Test the health check fails by poitning it a nonsense server.
     *
     * @throws ConfigurationException
     */
    @Test
    public void testHealthCheckUnhealthy()
        throws ConfigurationException, InvalidNameException
    {
        BootstrapConfiguration bc = getBootstrap();
        setRestEndpoint(bc);
        Mockito.when(bc.isConfigurationPresent(Mockito.eq(RestConfigurationSource.CONFIG_REST_HOST))).thenReturn(true);
        Mockito.when(bc.getConfiguration(Mockito.eq(RestConfigurationSource.CONFIG_REST_HOST))).thenReturn("http://1.1.1.1:9999");
        ManagedConfigurationSource rcp = new RestConfigurationSource(bc, new NullCipher(), getId(WORKER), codec);
        assertEquals(HealthStatus.UNHEALTHY, rcp.healthCheck().getStatus());
    }

    /**
     * Used by the DropwizardClientRule to make a demo app for the test which returns serialised JSON over HTTP REST.
     */
    @Path("/config/" + APP + "/" + WORKER + "/TestConfig")
    @Produces(MediaType.APPLICATION_JSON)
    public static class TestConfigResource
    {
        @GET
        public TestConfig getConfig()
        {
            return new TestConfig();
        }
    }

    /**
     * Test retrieval on a different scope.
     */
    @Path("/config/" + APP + "/AppWideTestConfig")
    @Produces(MediaType.APPLICATION_JSON)
    public static class AppWideTestConfigResource
    {
        @GET
        public AppWideTestConfig getConfig()
        {
            return new AppWideTestConfig();
        }
    }

    @Path("/config/" + APP + "/" + INVALID_WORKER + "/TestConfig")
    @Produces(MediaType.APPLICATION_JSON)
    public static class TestInvalidConfigResource
    {
        @GET
        public TestConfig getConfig()
        {
            TestConfig ret = new TestConfig();
            ret.setTestData("");
            return ret;
        }
    }

    /**
     * Our dummy configuration object for testing.
     */
    public static class TestConfig
    {
        @JsonProperty
        @NotBlank
        private String testData = DEFAULT_TEST_STRING;

        public String getTestData()
        {
            return testData;
        }

        public void setTestData(final String data)
        {
            this.testData = data;
        }
    }

    public static class AppWideTestConfig
    {
        @JsonProperty
        @NotBlank
        private String testData = ALTERNATE_TEST_STRING;

        public String getTestData()
        {
            return testData;
        }

        public void setTestData(final String data)
        {
            this.testData = data;
        }
    }

    /**
     * Another skeleton dummy class for testing.
     */
    public static class OtherTestConfig
    {

    }

}
