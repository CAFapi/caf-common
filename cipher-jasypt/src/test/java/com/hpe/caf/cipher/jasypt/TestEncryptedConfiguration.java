package com.hpe.caf.cipher.jasypt;


import com.hpe.caf.api.Encrypted;


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
