package com.hpe.caf.config.yaml;


import org.hibernate.validator.constraints.NotBlank;


public class TestFileConfig
{
    @NotBlank
    private String testString = "test123";


    public TestFileConfig() { }


    public String getTestString()
    {
        return this.testString;
    }


    public void setTestString(final String input)
    {
        this.testString = input;
    }
}