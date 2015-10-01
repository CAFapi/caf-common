package com.hpe.caf.codec;


public class RandomGetterTestData extends PrivateCodecTestData
{
    RandomGetterTestData() { }


    public RandomGetterTestData(final String val)
    {
        super(val);
    }


    public String getValueWithoutMemberVariable()
    {
        return "hello!";
    }
}
