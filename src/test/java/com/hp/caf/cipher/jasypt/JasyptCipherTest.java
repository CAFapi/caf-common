package com.hp.caf.cipher.jasypt;


import com.hp.caf.api.BootstrapConfiguration;
import com.hp.caf.api.Cipher;
import com.hp.caf.api.CipherException;
import com.hp.caf.api.ConfigurationException;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

public class JasyptCipherTest
{
    private static final String PASS = "test123";


    @Test
    public void jasyptStringTest()
            throws ConfigurationException, CipherException
    {
        BootstrapConfiguration boot = Mockito.mock(BootstrapConfiguration.class);
        Mockito.when(boot.getConfiguration(JasyptCipher.CONFIG_SECURITY_PASS)).thenReturn(PASS);
        Cipher sp = new JasyptCipher(boot);
        String testString = "test456";
        Assert.assertEquals(testString, sp.decrypt(sp.encrypt(testString)));
    }


    @Test
    public void jasyptClassTest()
            throws ConfigurationException, CipherException
    {
        BootstrapConfiguration boot = Mockito.mock(BootstrapConfiguration.class);
        Mockito.when(boot.getConfiguration(JasyptCipher.CONFIG_SECURITY_PASS)).thenReturn(PASS);
        Cipher sp = new JasyptCipher(boot);
        String myString = "test456";
        TestEncryptedConfiguration conf = new TestEncryptedConfiguration();
        conf.setTestConfig(sp.encrypt(myString));
        Assert.assertEquals(myString, sp.decrypt(conf).getTestConfig());
    }


    @Test(expected = CipherException.class)
    public void jasyptExceptionTest()
        throws ConfigurationException, CipherException
    {
        BootstrapConfiguration boot = Mockito.mock(BootstrapConfiguration.class);
        Mockito.when(boot.getConfiguration(JasyptCipher.CONFIG_SECURITY_PASS)).thenThrow(ConfigurationException.class);
        Cipher sp = new JasyptCipher(boot);
        String testString = "test456";
        sp.decrypt(sp.encrypt(testString));
    }
}
