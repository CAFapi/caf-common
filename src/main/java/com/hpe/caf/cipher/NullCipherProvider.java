package com.hpe.caf.cipher;


import com.hpe.caf.api.BootstrapConfiguration;
import com.hpe.caf.api.Cipher;
import com.hpe.caf.api.CipherProvider;


public final class NullCipherProvider implements CipherProvider
{
    @Override
    public Cipher getCipher(final BootstrapConfiguration bootstrapConfiguration)
    {
        return new NullCipher(bootstrapConfiguration);
    }
}
