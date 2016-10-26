package com.hpe.caf.api;


import com.hpe.caf.naming.ServicePath;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import javax.naming.InvalidNameException;
import javax.validation.Valid;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.InputStream;


public class CafConfigurationSourceTest
{
    private static final String TEST_STRING = "test";
    private static final String TEST_STRING_DEC = "test2";
    private static final int TEST_INT = 101;
    private static final long DEFAULT_LONG = 200L;
    private static final long TEST_LONG = 16384L;


    @Test
    public void testConfigurationSource()
            throws CodecException, CipherException, ConfigurationException, InvalidNameException
    {
        InputStream is = Mockito.mock(InputStream.class);
        Validator val = Validation.buildDefaultValidatorFactory().getValidator();

        ServicePath id = new ServicePath("/testApp/unitTest");
        TestData data = new TestData();
        data.setTestString(TEST_STRING);
        data.setValue(TEST_INT);

        Codec c = Mockito.mock(Codec.class);
        Mockito.when(c.deserialise(is, TestData.class)).thenReturn(data);

        Cipher cipher = Mockito.mock(Cipher.class);
        Mockito.when(cipher.decrypt(TEST_STRING)).thenReturn(TEST_STRING_DEC);

        CafConfigurationSource config = Mockito.mock(CafConfigurationSource.class);
        Mockito.when(config.getConfigurationStream(TestData.class, id.getPath())).thenReturn(is);
        Mockito.when(config.getConfigurationStream(Mockito.eq(SubConfig.class), Mockito.any())).thenThrow(ConfigurationException.class);
        Mockito.when(config.getServicePath()).thenReturn(id);
        Mockito.when(config.getCodec()).thenReturn(c);
        Mockito.when(config.getCipher()).thenReturn(cipher);
        Mockito.when(config.getValidator()).thenReturn(val);

        TestData result = config.getConfiguration(TestData.class);
        Assert.assertEquals(TEST_INT, result.getValue());
        Assert.assertEquals(TEST_STRING_DEC, result.getTestString());
        Assert.assertEquals(DEFAULT_LONG, result.getSubConfig().getTestLong());
    }


    @Test(expected = ConfigurationException.class)
    public void testConfigurationSourceValidation()
            throws CodecException, CipherException, ConfigurationException, InvalidNameException
    {
        InputStream is = Mockito.mock(InputStream.class);
        ServicePath id = new ServicePath("/testApp/unitTest");
        TestData data = new TestData();
        data.setTestString(TEST_STRING);
        data.setValue(1);

        Codec c = Mockito.mock(Codec.class);
        Mockito.when(c.deserialise(is, TestData.class)).thenReturn(data);
        Cipher cipher = Mockito.mock(Cipher.class);
        Mockito.when(cipher.decrypt(TEST_STRING)).thenReturn(TEST_STRING_DEC);
        Validator val = Validation.buildDefaultValidatorFactory().getValidator();

        CafConfigurationSource config = Mockito.mock(CafConfigurationSource.class);
        Mockito.when(config.getConfigurationStream(TestData.class, id.getPath())).thenReturn(is);
        Mockito.when(config.getConfigurationStream(Mockito.eq(SubConfig.class), Mockito.any())).thenThrow(ConfigurationException.class);
        Mockito.when(config.getServicePath()).thenReturn(id);
        Mockito.when(config.getCodec()).thenReturn(c);
        Mockito.when(config.getCipher()).thenReturn(cipher);
        Mockito.when(config.getValidator()).thenReturn(val);

        config.getConfiguration(TestData.class);
    }


    @Test
    public void testRecursiveConfiguration()
            throws CodecException, ConfigurationException, CipherException, InvalidNameException
    {
        InputStream is = Mockito.mock(InputStream.class);

        ServicePath id = new ServicePath("/testApp/unitTest");
        TestData data = new TestData();
        data.setTestString(TEST_STRING);
        data.setValue(TEST_INT);
        SubConfig subConfig = new SubConfig();
        subConfig.setTestLong(TEST_LONG);

        TestData dataDec = new TestData();
        dataDec.setTestString(TEST_STRING_DEC);
        dataDec.setValue(TEST_INT);

        Codec c = Mockito.mock(Codec.class);
        Mockito.when(c.deserialise(is, TestData.class)).thenReturn(data);
        Mockito.when(c.deserialise(is, SubConfig.class)).thenReturn(subConfig);
        Cipher cipher = Mockito.mock(Cipher.class);
        Mockito.when(cipher.decrypt(TEST_STRING)).thenReturn(TEST_STRING_DEC);
        Validator val = Validation.buildDefaultValidatorFactory().getValidator();

        CafConfigurationSource config = Mockito.mock(CafConfigurationSource.class);
        Mockito.when(config.getConfigurationStream(TestData.class, id.getPath())).thenReturn(is);
        Mockito.when(config.getConfigurationStream(SubConfig.class, id.getPath())).thenReturn(is);
        Mockito.when(config.getServicePath()).thenReturn(id);
        Mockito.when(config.getCodec()).thenReturn(c);
        Mockito.when(config.getCipher()).thenReturn(cipher);
        Mockito.when(config.getValidator()).thenReturn(val);

        TestData result = config.getConfiguration(TestData.class);
        Assert.assertEquals(TEST_LONG, result.getSubConfig().getTestLong());
    }


    @Test(expected = ConfigurationException.class)
    public void testRecursiveConfigurationValidation()
            throws CodecException, ConfigurationException, CipherException, InvalidNameException
    {
        InputStream is = Mockito.mock(InputStream.class);
        ServicePath id = new ServicePath("/testApp/unitTest");

        TestData data = new TestData();
        data.setTestString(TEST_STRING);
        data.setValue(TEST_INT);
        SubConfig subConfig = new SubConfig();
        subConfig.setTestLong(10);

        Codec c = Mockito.mock(Codec.class);
        Mockito.when(c.deserialise(is, TestData.class)).thenReturn(data);
        Mockito.when(c.deserialise(is, SubConfig.class)).thenReturn(subConfig);
        Cipher cipher = Mockito.mock(Cipher.class);
        Mockito.when(cipher.decrypt(TEST_STRING)).thenReturn(TEST_STRING_DEC);
        Validator val = Validation.buildDefaultValidatorFactory().getValidator();

        CafConfigurationSource config = Mockito.mock(CafConfigurationSource.class);
        Mockito.when(config.getConfigurationStream(TestData.class, id.getPath())).thenReturn(is);
        Mockito.when(config.getConfigurationStream(SubConfig.class, id.getPath())).thenReturn(is);
        Mockito.when(config.getServicePath()).thenReturn(id);
        Mockito.when(config.getCodec()).thenReturn(c);
        Mockito.when(config.getCipher()).thenReturn(cipher);
        Mockito.when(config.getValidator()).thenReturn(val);

        config.getConfiguration(TestData.class);
    }


    private class TestData
    {
        @Encrypted
        private String testString;
        @Min(5)
        private int value;
        @Configuration
        @Valid
        @NotNull
        private SubConfig subConfig = new SubConfig(DEFAULT_LONG);


        public String getTestString() {
            return testString;
        }


        public void setTestString(final String testString) {
            this.testString = testString;
        }


        public int getValue() {
            return value;
        }


        public void setValue(final int value) {
            this.value = value;
        }


        public SubConfig getSubConfig()
        {
            return subConfig;
        }


        public void setSubConfig(final SubConfig subConfig)
        {
            this.subConfig = subConfig;
        }
    }


    private class SubConfig
    {
        @Min(100)
        private long testLong;


        public SubConfig() { }


        public SubConfig(final long val)
        {
            this.testLong = val;
        }


        public long getTestLong()
        {
            return testLong;
        }


        public void setTestLong(final long testLong)
        {
            this.testLong = testLong;
        }
    }
}
