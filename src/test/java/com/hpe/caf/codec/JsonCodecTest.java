package com.hpe.caf.codec;


import com.hpe.caf.api.Codec;
import com.hpe.caf.api.CodecException;
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
