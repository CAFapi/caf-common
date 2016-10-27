package com.hpe.caf.cipher;


import com.hpe.caf.api.Cipher;


/**
 * Pass-through module when no encryption/decryption is desired.
 */
public final class NullCipher implements Cipher
{
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
