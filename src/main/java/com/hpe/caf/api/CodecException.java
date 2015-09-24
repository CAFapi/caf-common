package com.hpe.caf.api;


/**
 * Thrown when a Codec fails to encode or decode information.
 * @since 1.0
 */
public class CodecException extends Exception
{
    /**
     * Create a new CodecException
     * @param message information explaining the exception
     * @param cause the original cause of this exception
     */
    public CodecException(final String message, final Throwable cause)
    {
        super(message, cause);
    }
}
