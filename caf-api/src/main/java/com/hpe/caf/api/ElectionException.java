package com.hpe.caf.api;


/**
 * Thrown if there is a problem with the election process.
 * @since 5.0
 */
public class ElectionException extends Exception
{
    public ElectionException(final String message, final Throwable cause)
    {
        super(message, cause);
    }
}
