package com.hpe.caf.api;

import java.io.InputStream;

/**
 * An an implementation of Decoder for use with tests.
 */
public class TestDecoder_1 implements Decoder{
    @Override
    public <T> T deserialise(InputStream stream, Class<T> clazz) throws CodecException {
        return null;
    }
}
