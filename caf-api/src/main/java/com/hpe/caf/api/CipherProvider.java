package com.hpe.caf.api;


/**
 * Simple boilerplate for returning a Cipher implementation.
 * @since 5.0
 */
public interface CipherProvider
{
    /**
     * Get a Cipher implementation.
     * @param bootstrapConfiguration used for configuring the Cipher
     * @return a new Cipher instance
     * @throws CipherException if the Cipher instance could not be created
     */
    Cipher getCipher(BootstrapConfiguration bootstrapConfiguration)
        throws CipherException;
}
