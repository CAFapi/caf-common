package com.hp.caf.codec;


import com.hp.caf.api.Codec;
import com.hp.caf.api.CodecException;
import org.junit.Assert;
import org.junit.Test;


public class JsonCodecTest
{
    private static final String VERIFY_STRING = "test456";


    @Test
    public void testJsonCodec()
            throws CodecException
    {
        Codec codec = new JsonCodec();
        CodecTestData test = new CodecTestData();
        test.setTestString(VERIFY_STRING);
        byte[] stuff = codec.serialise(test);
        CodecTestData res = codec.deserialise(stuff, CodecTestData.class);
        Assert.assertEquals(test.getTestString(), res.getTestString());
    }

}
