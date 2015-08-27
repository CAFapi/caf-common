package com.hp.caf.api;


/**
 * Thrown if there is a problem with the election process.
 */
public class ElectionException extends Exception
{
    public ElectionException(final String message, final Throwable cause)
    {
        super(message, cause);
    }
}
