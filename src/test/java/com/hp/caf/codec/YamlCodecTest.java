package com.hp.caf.codec;


import com.hp.caf.api.Codec;
import com.hp.caf.api.CodecException;
import org.junit.Assert;
import org.junit.Test;


public class YamlCodecTest
{
    private static final String VERIFY_STRING = "test456";


    @Test
    public void testYamlCodec()
            throws CodecException
    {
        Codec codec = new YamlCodec();
        CodecTestData test = new CodecTestData();
        test.setTestString(VERIFY_STRING);
        byte[] stuff = codec.serialise(test);
        CodecTestData res = codec.deserialise(stuff, CodecTestData.class);
        Assert.assertEquals(test.getTestString(), res.getTestString());
    }
}
