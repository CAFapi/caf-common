package com.hpe.caf.util;


/**
 * Thrown when there is a problem finding or instantiating a requested component.
 * @since 6.0
 */
public class ModuleLoaderException extends Exception
{
    public ModuleLoaderException(final String message)
    {
        super(message);
    }


    public ModuleLoaderException(final String message, final Throwable cause)
    {
        super(message, cause);
    }
}
