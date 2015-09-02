package com.hpe.caf.codec;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.hpe.caf.api.Codec;
import com.hpe.caf.api.CodecException;
import com.ning.compress.lzf.LZFInputStream;
import com.ning.compress.lzf.LZFOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;


/**
 * Implementation of Codec that supports serialisation and deserialisation to and form JSON format
 * that itself is compressed with a high-speed LZF algorithm. In some crude tests, this was resulting
 * in data that was approximately 60% of the original JSON size with negligible performance impact.
 */
public class JsonLzfCodec extends Codec
{
    private final ObjectMapper mapper = new ObjectMapper();


    @Override
    public <T> T deserialise(final byte[] data, final Class<T> clazz)
            throws CodecException
    {
        try (ByteArrayInputStream bis = new ByteArrayInputStream(data);
             LZFInputStream lzf = new LZFInputStream(bis)) {
            return mapper.readValue(lzf, clazz);
        } catch (IOException e) {
            throw new CodecException("Failed to deserialise", e);
        }
    }


    @Override
    public <T> T deserialise(final InputStream stream, final Class<T> clazz)
            throws CodecException
    {
        try (LZFInputStream lzf = new LZFInputStream(stream)) {
            return mapper.readValue(lzf, clazz);
        } catch (IOException e) {
            throw new CodecException("Failed to deserialise", e);
        }
    }


    @Override
    public byte[] serialise(final Object object)
            throws CodecException
    {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             LZFOutputStream lzf = new LZFOutputStream(bos)) {
            mapper.writeValue(lzf, object);
            return bos.toByteArray();
        } catch (IOException e) {
            throw new CodecException("Failed to serialise", e);
        }
    }
}
