package com.hpe.caf.decoder;

/**
 * Provides access to properties requested during decoding.
 */
public class PropertyRetriever {
    /**
     * Gets the value to use for the specified property.
     * @param key Name of property.
     * @return Value of property specified.
     */
    public static String getProperty(String key){
        return System.getenv(key);
    }
}
