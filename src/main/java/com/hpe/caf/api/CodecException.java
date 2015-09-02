package com.hpe.caf.api;


/**
 * Thrown when a Codec fails to encode or decode information.
 */
public class CodecException extends Exception
{
    public CodecException(final String message, final Throwable cause)
    {
        super(message, cause);
    }
}
