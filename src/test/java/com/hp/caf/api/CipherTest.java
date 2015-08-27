package com.hp.caf.api;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

public class CipherTest
{
    private static final String DECRYPTED_STRING = "decrypted";
    private static final String ENCRYPTED_STRING = "encrypted";


    @Test
    public void testDecrypt() throws CipherException {
        Cipher t = new TestCipher(Mockito.mock(BootstrapConfiguration.class));
        TestClass data = new TestClass();
        data.setTestString(ENCRYPTED_STRING);
        t.decrypt(data);
        Assert.assertTrue(data.getTestString().equals(DECRYPTED_STRING));
    }


    private class TestCipher extends Cipher
    {
        public TestCipher(final BootstrapConfiguration bootstrap) {
            super(bootstrap);
        }

        @Override
        public String decrypt(final String input) throws CipherException {
            return DECRYPTED_STRING;
        }

        @Override
        public String encrypt(final String input) throws CipherException {
            return ENCRYPTED_STRING;
        }
    }


    private class TestClass
    {
        @Encrypted
        private String testString;


        public String getTestString() {
            return testString;
        }


        public void setTestString(final String testString) {
            this.testString = testString;
        }
    }
}
