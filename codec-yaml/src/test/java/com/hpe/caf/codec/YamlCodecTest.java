package com.hpe.caf.codec;


import com.hpe.caf.api.Codec;
import com.hpe.caf.api.CodecException;
import com.hpe.caf.api.DecodeMethod;
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


    @Test(expected = CodecException.class)
    public void testUnknownPropertyStrict()
        throws CodecException
    {
        Codec codec = new YamlCodec();
        ExtendedCodecTestData test = new ExtendedCodecTestData();
        test.setTestString(VERIFY_STRING);
        test.setTestValue(100);
        byte[] stuff = codec.serialise(test);
        codec.deserialise(stuff, CodecTestData.class, DecodeMethod.STRICT);
    }


    @Test
    public void testUnknownPropertyLenient()
        throws CodecException
    {
        Codec codec = new YamlCodec();
        ExtendedCodecTestData test = new ExtendedCodecTestData();
        test.setTestString(VERIFY_STRING);
        test.setTestValue(100);
        byte[] stuff = codec.serialise(test);
        codec.deserialise(stuff, CodecTestData.class, DecodeMethod.LENIENT);
    }


    @Test
    public void testPrivateSerialisation()
        throws CodecException
    {
        Codec codec = new YamlCodec();
        PrivateCodecTestData data = new PrivateCodecTestData(VERIFY_STRING);
        byte[] stuff = codec.serialise(data);
        Assert.assertEquals(VERIFY_STRING, codec.deserialise(stuff, PrivateCodecTestData.class).getTestData());
    }


    @Test
    public void testGetterVisibility()
        throws CodecException
    {
        Codec codec = new YamlCodec();
        RandomGetterTestData test = new RandomGetterTestData(VERIFY_STRING);
        byte[] stuff = codec.serialise(test);
        Assert.assertEquals(VERIFY_STRING, codec.deserialise(stuff, RandomGetterTestData.class).getTestData());
    }
}
