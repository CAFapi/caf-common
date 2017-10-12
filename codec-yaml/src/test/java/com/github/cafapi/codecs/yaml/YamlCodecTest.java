/*
 * Copyright 2015-2017 EntIT Software LLC, a Micro Focus company.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.cafapi.codecs.yaml;

import com.github.cafapi.Codec;
import com.github.cafapi.CodecException;
import com.github.cafapi.DecodeMethod;
import org.testng.Assert;
import org.testng.annotations.Test;

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

    @Test(expectedExceptions = CodecException.class)
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
