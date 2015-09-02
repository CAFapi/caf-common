package com.hpe.caf.api;


/**
 * Thrown when a Cipher encounters a failure when encrypting or decrypting information.
 */
public class CipherException extends Exception
{
    public CipherException(final String message, final Throwable cause)
    {
        super(message, cause);
    }
}
