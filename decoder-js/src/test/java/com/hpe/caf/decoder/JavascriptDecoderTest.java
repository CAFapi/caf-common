/*
 * Copyright 2015-2024 Open Text.
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
package com.hpe.caf.decoder;

import com.hpe.caf.api.CodecException;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import static org.mockito.Mockito.doReturn;

/**
 * Unit tests for JavaScriptDecoder class.
 */
@ExtendWith(MockitoExtension.class)
public class JavascriptDecoderTest {

    /**
     * Tests that the decoded input use environment property values.
     * @throws CodecException
     */
    @Test
    public void deserializeWithEnvironmentVariablesTest() throws CodecException, URISyntaxException
    {
        int expectedMyInt = ThreadLocalRandom.current().nextInt();
        String expectedMyString = "Test Result"+ UUID.randomUUID().toString();
        boolean expectedMyBoolean = true;
        String expectedNestedString = "Nested Result"+ UUID.randomUUID().toString();
        int expectedNestedInt = ThreadLocalRandom.current().nextInt();
        boolean expectedNestedBoolean = true;

        URL myFileUrl = getClass().getClassLoader().getResource("my-file");
        Path myFilePath = Paths.get(myFileUrl.toURI());
        String expectedMyFilePath = myFilePath.toString();

        Mockito.spy(PropertyRetriever.class);
        doReturn(Integer.toString(expectedMyInt)).when(PropertyRetriever.getenv("TEST_MYINT"));
        doReturn(expectedMyString).when(PropertyRetriever.getenv("TEST_MYSTRING"));
        doReturn(Boolean.toString(expectedMyBoolean)).when(PropertyRetriever.getenv("TEST_MYBOOLEAN"));
        doReturn(expectedNestedString).when(PropertyRetriever.getenv("TEST_MYNESTEDSTRING"));
        doReturn(Integer.toString(expectedNestedInt)).when(PropertyRetriever.getenv("TEST_MYNESTEDINT"));
        doReturn(Boolean.toString(expectedNestedBoolean)).when(PropertyRetriever.getenv("TEST_MYNESTEDBOOLEAN"));
        doReturn(expectedMyFilePath).when(PropertyRetriever.getenv("TEST_MYFILE"));

        InputStream inputToDecode = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream("DecodeResultInput.js");

        JavascriptDecoder decoder = new JavascriptDecoder();
        DecodeResult result = decoder.deserialise(inputToDecode, DecodeResult.class);
        Assert.assertNotNull(result, "Deserialized result should not be null");
        Assert.assertEquals(result.getMyString(), expectedMyString, "Decoded myString should have been resolved to expected property " +
                "value.");
        Assert.assertEquals(result.getMyInt(), expectedMyInt, "Decoded myInt should have been resolved to expected property " +
                "value.");
        Assert.assertEquals(result.isMyBoolean(), expectedMyBoolean, "Decoded myBoolean should have been resolved to expected property " +
                "value.");
        Assert.assertEquals(result.getMyFile(), "he", "Decoded myFile should have been resolved to expected property " +
                "value.");
        DecodeResult.NestedProp nestedResult = result.getMyNestedProp();
        Assert.assertEquals(nestedResult.getMyNestedString(), expectedNestedString, "Decoded nested string should have been resolved to expected property " +
                "value.");
        Assert.assertEquals(nestedResult.getMyNestedInt(), expectedNestedInt, "Decoded nested int should have been resolved to expected property " +
                "value.");
        Assert.assertEquals(nestedResult.isMyNestedBoolean(), expectedNestedBoolean, "Decoded nested boolean should have been resolved to expected property " +
                "value.");
    }

    /**
     * Tests that deserialized input returns appropriate values when no environment variables are set.
     * @throws CodecException
     */
    @Test
    public void deserializeWithoutEnvironmentVariablesTest() throws CodecException {
        //default values specified in resource input file
        int expectedMyInt = 100;
        String expectedMyString = "default-appended";
        boolean expectedMyBoolean = false;
        String expectedNestedString = "default nested string";
        int expectedNestedInt = 500;
        boolean expectedNestedBoolean = false;

        InputStream inputToDecode = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream("DecodeResultInput.js");

        JavascriptDecoder decoder = new JavascriptDecoder();
        DecodeResult result = decoder.deserialise(inputToDecode, DecodeResult.class);
        Assert.assertNotNull(result, "Deserialized result should not be null");
        Assert.assertEquals(result.getMyString(), expectedMyString, "Decoded myString should have been resolved to expected property " +
                "value.");
        Assert.assertEquals(result.getMyInt(), expectedMyInt, "Decoded myInt should have been resolved to expected property " +
                "value.");
        Assert.assertEquals(result.isMyBoolean(), expectedMyBoolean, "Decoded myBoolean should have been resolved to expected property " +
                "value.");
        DecodeResult.NestedProp nestedResult = result.getMyNestedProp();
        Assert.assertEquals(nestedResult.getMyNestedString(), expectedNestedString, "Decoded nested string should have been resolved to expected property " +
                "value.");
        Assert.assertEquals(nestedResult.getMyNestedInt(), expectedNestedInt, "Decoded nested int should have been resolved to expected property " +
                "value.");
        Assert.assertEquals(nestedResult.isMyNestedBoolean(), expectedNestedBoolean, "Decoded nested boolean should have been resolved to expected property " +
                "value.");
    }

    /**
     * Test that appropriate exception thrown when an invalid input stream i.e. not JavaScript, is passed.
     */
    @Test
    public void invalidInputFile(){
        InputStream inputToDecode = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream("InvalidInput.sh");

        JavascriptDecoder decoder = new JavascriptDecoder();
        try {
            decoder.deserialise(inputToDecode, DecodeResult.class);
            Assert.fail("CodecException should have been thrown for invalid file.");
        } catch (CodecException e) {
        }
    }
}