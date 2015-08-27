package com.hp.caf.cipher;


import com.hp.caf.api.BootstrapConfiguration;
import com.hp.caf.api.Cipher;
import com.hp.caf.api.CipherProvider;


public final class NullCipherProvider implements CipherProvider
{
    @Override
    public Cipher getCipher(final BootstrapConfiguration bootstrapConfiguration)
    {
        return new NullCipher(bootstrapConfiguration);
    }
}
