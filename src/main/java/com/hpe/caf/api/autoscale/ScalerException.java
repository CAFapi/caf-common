package com.hpe.caf.api.autoscale;


/**
 * Thrown by the autoscaler application or components when encountering a problem.
 */
public class ScalerException extends Exception
{
    public ScalerException(final String message, final Throwable cause)
    {
        super(message, cause);
    }


    public ScalerException(final String message)
    {
        super(message);
    }
}
