package com.hpe.caf.config.yaml;


import com.hpe.caf.api.Configuration;

import javax.validation.Valid;


public class RootConfig
{
    private String testString = "test";
    @Configuration
    @Valid
    private InnerConfig innerConfig;


    public String getTestString()
    {
        return testString;
    }


    public void setTestString(final String testString)
    {
        this.testString = testString;
    }


    public InnerConfig getInnerConfig()
    {
        return innerConfig;
    }


    public void setInnerConfig(final InnerConfig innerConfig)
    {
        this.innerConfig = innerConfig;
    }
}
