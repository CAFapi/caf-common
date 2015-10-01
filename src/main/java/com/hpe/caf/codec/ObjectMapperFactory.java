package com.hpe.caf.codec;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;


/**
 * Provides pre-configured strict and lenient Jackson JSON ObjectMapper instances.
 */
public final class ObjectMapperFactory
{
    private ObjectMapperFactory() { }


    /**
     * @return an ObjectMapper which fails on unknown properties, does not accept null for primitives, or duplicates
     */
    public static ObjectMapper getStrictMapper()
    {
        ObjectMapper strictMapper = new ObjectMapper();
        strictMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        strictMapper.setVisibility(PropertyAccessor.GETTER, JsonAutoDetect.Visibility.NONE);
        strictMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);
        strictMapper.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, true);
        strictMapper.configure(DeserializationFeature.FAIL_ON_READING_DUP_TREE_KEY, true);
        return strictMapper;
    }


    /**
     * @return an ObjectMapper which ignores unknown properties, uses defaults in case of null for primitive, and accepts duplicates
     */
    public static ObjectMapper getLenientMapper()
    {
        ObjectMapper lenientMapper = new ObjectMapper();
        lenientMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        lenientMapper.setVisibility(PropertyAccessor.GETTER, JsonAutoDetect.Visibility.NONE);
        lenientMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        lenientMapper.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, false);
        lenientMapper.configure(DeserializationFeature.FAIL_ON_READING_DUP_TREE_KEY, false);
        return lenientMapper;
    }
}
