package com.hp.caf.cipher.jasypt;


import com.hp.caf.api.Encrypted;


public class TestEncryptedConfiguration
{
    @Encrypted
    private String testConfig;


    public String getTestConfig()
    {
        return testConfig;
    }


    public void setTestConfig(final String testConfig)
    {
        this.testConfig = testConfig;
    }
}
