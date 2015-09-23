package com.hpe.caf.codec;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hpe.caf.api.Codec;
import com.hpe.caf.api.CodecException;
import com.hpe.caf.api.DecodeMethod;

import java.io.IOException;
import java.io.InputStream;


/**
 * Implementation of Codec that supports serialisation and deserialisation to and form JSON format.
 */
public class JsonCodec extends Codec
{
    private final ObjectMapper strictMapper;
    private final ObjectMapper lenientMapper;


    public JsonCodec()
    {
        strictMapper = ObjectMapperFactory.getStrictMapper();
        lenientMapper = ObjectMapperFactory.getLenientMapper();
    }


    @Override
    public <T> T deserialise(final byte[] data, final Class<T> clazz, final DecodeMethod method)
            throws CodecException
    {
        try {
            return getMapper(method).readValue(data, clazz);
        } catch (IOException e) {
            throw new CodecException("Failed to deserialise", e);
        }
    }


    @Override
    public <T> T deserialise(final InputStream stream, final Class<T> clazz, final DecodeMethod method)
            throws CodecException
    {
        try {
            return getMapper(method).readValue(stream, clazz);
        } catch (IOException e) {
            throw new CodecException("Failed to deserialise", e);
        }
    }


    @Override
    public byte[] serialise(final Object object)
            throws CodecException
    {
        try {
            return getMapper(DecodeMethod.getDefault()).writeValueAsBytes(object);
        } catch (JsonProcessingException e) {
            throw new CodecException("Failed to serialise", e);
        }
    }


    protected ObjectMapper getMapper(final DecodeMethod method)
    {
        return method == DecodeMethod.STRICT ? strictMapper : lenientMapper;
    }
}
