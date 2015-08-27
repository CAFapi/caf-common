package com.hp.caf.codec;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hp.caf.api.Codec;
import com.hp.caf.api.CodecException;

import java.io.IOException;
import java.io.InputStream;


/**
 * Implementation of Codec that supports serialisation and deserialisation to and form JSON format.
 */
public class JsonCodec extends Codec
{
    private final ObjectMapper mapper = new ObjectMapper();


    @Override
    public <T> T deserialise(final byte[] data, final Class<T> clazz)
            throws CodecException
    {
        try {
            return mapper.readValue(data, clazz);
        } catch (IOException e) {
            throw new CodecException("Failed to deserialise", e);
        }
    }


    @Override
    public <T> T deserialise(final InputStream stream, final Class<T> clazz)
            throws CodecException
    {
        try {
            return mapper.readValue(stream, clazz);
        } catch (IOException e) {
            throw new CodecException("Failed to deserialise", e);
        }
    }


    @Override
    public byte[] serialise(final Object object)
            throws CodecException
    {
        try {
            return mapper.writeValueAsBytes(object);
        } catch (JsonProcessingException e) {
            throw new CodecException("Failed to serialise", e);
        }
    }
}
