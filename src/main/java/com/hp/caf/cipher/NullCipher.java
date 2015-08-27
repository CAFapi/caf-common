package com.hp.caf.cipher;


import com.hp.caf.api.BootstrapConfiguration;
import com.hp.caf.api.Cipher;


/**
 * Pass-through module when no encryption/decryption is desired.
 */
public final class NullCipher extends Cipher
{
    /**
     * {@inheritDoc}
     */
    public NullCipher(final BootstrapConfiguration bootstrap)
    {
        super(bootstrap);
    }


    @Override
    public String decrypt(final String input)
    {
        return input;
    }


    @Override
    public String encrypt(final String input)
    {
        return input;
    }
}
