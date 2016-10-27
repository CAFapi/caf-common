package com.hpe.caf.cipher.jasypt;


import com.hpe.caf.api.BootstrapConfiguration;
import com.hpe.caf.api.Cipher;
import com.hpe.caf.api.CipherException;
import com.hpe.caf.api.CipherProvider;


public class JasyptCipherProvider implements CipherProvider
{
    @Override
    public Cipher getCipher(final BootstrapConfiguration bootstrapConfiguration)
            throws CipherException
    {
        return new JasyptCipher(bootstrapConfiguration);
    }
}
