/*
 * Copyright 2015-2017 Hewlett Packard Enterprise Development LP.
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
package com.hpe.caf.config.system;


import com.hpe.caf.api.BootstrapConfiguration;
import com.hpe.caf.api.ConfigurationException;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;


public class SystemBootstrapConfigurationTest
{
    private SystemBootstrapConfiguration sysbsc = new SystemBootstrapConfiguration();
    private static final String TEST_PROP = "paas.TEST_PROP.1";

    
    @BeforeMethod
    public void setUp()
    {
        sysbsc = new SystemBootstrapConfiguration();
    }
    
    @AfterMethod
    public void tearDown()
    {
        System.clearProperty(TEST_PROP);
        System.clearProperty(SystemBootstrapConfiguration.ENV_MARATHON_APP_ID);
        System.clearProperty(BootstrapConfiguration.CONFIG_APP_NAME);
    }

    @Test
    public void isConfigurationPresent()
    {
        System.clearProperty(TEST_PROP);
        Assert.assertEquals(false, sysbsc.isConfigurationPresent(TEST_PROP));
        System.setProperty(TEST_PROP, "test123");
        Assert.assertEquals(true, sysbsc.isConfigurationPresent(TEST_PROP));
    }


    @Test(expectedExceptions = ConfigurationException.class)
    public void getConfigurationInvalid()
            throws ConfigurationException
    {
        System.clearProperty(TEST_PROP);
        sysbsc.getConfiguration(TEST_PROP);
    }


    @Test
    public void getConfiguration()
            throws ConfigurationException
    {
        String testVal = "test123";
        System.setProperty(TEST_PROP, testVal);
        Assert.assertEquals(testVal, sysbsc.getConfiguration(TEST_PROP));
    }


    @Test
    public void getConfigurationInteger()
        throws ConfigurationException
    {
        System.setProperty(TEST_PROP, "101");
        Assert.assertEquals(101, sysbsc.getConfigurationInteger(TEST_PROP));
    }


    @Test
    public void getConfigurationIntegerMinMax()
        throws ConfigurationException
    {
        System.setProperty(TEST_PROP, "101");
        Assert.assertEquals(100, sysbsc.getConfigurationInteger(TEST_PROP, 1, 100));
        Assert.assertEquals(200, sysbsc.getConfigurationInteger(TEST_PROP, 200, 300));
    }


    @Test(expectedExceptions = ConfigurationException.class)
    public void getConfigurationIntegerInvalid()
            throws ConfigurationException
    {
        System.setProperty(TEST_PROP, "blah");
        sysbsc.getConfigurationInteger(TEST_PROP);
    }


    @Test
    public void getConfigurationBoolean()
            throws ConfigurationException
    {
        System.setProperty(TEST_PROP, "true");
        Assert.assertTrue(sysbsc.getConfigurationBoolean(TEST_PROP));
    }


    @Test
    public void getServicePath()
        throws ConfigurationException
    {
        String testApp = "test/app";
        System.setProperty(SystemBootstrapConfiguration.ENV_MARATHON_APP_ID, testApp);
        Assert.assertEquals(testApp, sysbsc.getServicePath().toString());
        String testApp2 = "test/app2";
        System.setProperty(BootstrapConfiguration.CONFIG_APP_NAME, testApp2);
        Assert.assertEquals(testApp2, sysbsc.getServicePath().toString());
    }


    @Test(expectedExceptions = ConfigurationException.class)
    public void getServicePathException()
        throws ConfigurationException
    {
        String testApp = "@!!/////";
        System.setProperty(SystemBootstrapConfiguration.ENV_MARATHON_APP_ID, testApp);
        sysbsc.getServicePath();
    }

}
