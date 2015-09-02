package com.hpe.caf.codec;


import com.hpe.caf.api.Codec;
import com.hpe.caf.api.CodecException;
import org.junit.Assert;
import org.junit.Test;


public class JsonLzfCodecTest
{
    private static final String VERIFY_STRING = "test456";


    @Test
    public void testJsonLzfCodec()
            throws CodecException
    {
        Codec codec = new JsonLzfCodec();
        CodecTestData test = new CodecTestData();
        test.setTestString(VERIFY_STRING);
        byte[] stuff = codec.serialise(test);
        CodecTestData res = codec.deserialise(stuff, CodecTestData.class);
        Assert.assertEquals(test.getTestString(), res.getTestString());
    }

}
