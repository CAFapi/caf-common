package com.hpe.caf.api;


/**
 * Indicates there was a problem in the ConfigurationSource module.
 * @since 1.0
 */
public class ConfigurationException extends Exception
{
    /**
     * Create a new ConfigurationException
     * @param message information about this exception
     */
    public ConfigurationException(final String message)
    {
        super(message);
    }


    /**
     * Create a new ConfigurationException
     * @param message information about this exception
     * @param cause the original cause of this exception
     */
    public ConfigurationException(final String message, final Throwable cause)
    {
        super(message, cause);
    }
}
