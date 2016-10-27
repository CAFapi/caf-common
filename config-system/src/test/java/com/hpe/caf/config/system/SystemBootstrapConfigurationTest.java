package com.hpe.caf.config.system;


import com.hpe.caf.api.BootstrapConfiguration;
import com.hpe.caf.api.ConfigurationException;
import org.junit.Assert;
import org.junit.Test;


public class SystemBootstrapConfigurationTest
{
    private final SystemBootstrapConfiguration sysbsc = new SystemBootstrapConfiguration();
    private static final String TEST_PROP = "paas.TEST_PROP.1";


    @Test
    public void isConfigurationPresent()
    {
        System.clearProperty(TEST_PROP);
        Assert.assertEquals(false, sysbsc.isConfigurationPresent(TEST_PROP));
        System.setProperty(TEST_PROP, "test123");
        Assert.assertEquals(true, sysbsc.isConfigurationPresent(TEST_PROP));
    }


    @Test(expected = ConfigurationException.class)
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


    @Test(expected = ConfigurationException.class)
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


    @Test(expected = ConfigurationException.class)
    public void getServicePathException()
        throws ConfigurationException
    {
        String testApp = "@!!/////";
        System.setProperty(SystemBootstrapConfiguration.ENV_MARATHON_APP_ID, testApp);
        sysbsc.getServicePath();
    }

}
