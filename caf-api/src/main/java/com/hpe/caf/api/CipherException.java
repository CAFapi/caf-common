package com.hpe.caf.api;


/**
 * Thrown when a Cipher encounters a failure when encrypting or decrypting information.
 * @since 4.0
 */
public class CipherException extends Exception
{
    /**
     * Create a new CipherException.
     * @param message the message indicating the problem
     * @param cause the exception cause
     */
    public CipherException(final String message, final Throwable cause)
    {
        super(message, cause);
    }
}
