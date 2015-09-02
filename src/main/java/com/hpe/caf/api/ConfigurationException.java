package com.hpe.caf.api;


/**
 * Indicates there was a problem in the ConfigurationSource module.
 */
public class ConfigurationException extends Exception
{
    public ConfigurationException(final String message)
    {
        super(message);
    }


    public ConfigurationException(final String message, final Throwable cause)
    {
        super(message, cause);
    }
}
