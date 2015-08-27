package com.hp.caf.cipher.jasypt;


import com.hp.caf.api.BootstrapConfiguration;
import com.hp.caf.api.Cipher;
import com.hp.caf.api.CipherException;
import com.hp.caf.api.CipherProvider;


public class JasyptCipherProvider implements CipherProvider
{
    @Override
    public Cipher getCipher(final BootstrapConfiguration bootstrapConfiguration)
            throws CipherException
    {
        return new JasyptCipher(bootstrapConfiguration);
    }
}
