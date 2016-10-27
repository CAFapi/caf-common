package com.hpe.caf.codec;


import java.util.Objects;


public class PrivateCodecTestData
{
    private String testData;


    public PrivateCodecTestData(final String val)
    {
        this.testData = Objects.requireNonNull(val);
    }


    public PrivateCodecTestData() { }


    public String getTestData()
    {
        return testData;
    }
}
