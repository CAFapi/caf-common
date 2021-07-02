/*
 * Copyright 2015-2021 Micro Focus or one of its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hpe.caf.codec;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hpe.caf.api.Codec;
import com.hpe.caf.api.CodecException;
import com.hpe.caf.api.DecodeMethod;
import com.ning.compress.lzf.LZFInputStream;
import com.ning.compress.lzf.LZFOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.naming.OperationNotSupportedException;

/**
 * Implementation of Codec that supports serialisation and deserialisation to and form JSON format that itself is compressed with a
 * high-speed LZF algorithm. In some crude tests, this was resulting in data that was approximately 60% of the original JSON size with
 * negligible performance impact.
 */
public class JsonLzfCodec implements Codec
{
    private final ObjectMapper strictMapper;
    private final ObjectMapper lenientMapper;

    public JsonLzfCodec()
    {
        strictMapper = ObjectMapperFactory.getStrictMapper();
        lenientMapper = ObjectMapperFactory.getLenientMapper();
    }
    
    @Override
    public <T> T deserialise(final Object data, final Class<T> clazz, final DecodeMethod method) throws CodecException
    {
        throw new CodecException("Operation not implemented for jsonlzf codec", new OperationNotSupportedException());
//        if (data instanceof String) {
//            return deserialise(((String)data).getBytes(StandardCharsets.UTF_8), clazz, method);
//        }
//        return getMapper(method).convertValue(data, clazz);
    }
    
    @Override
    public <T> T deserialise(final byte[] data, final Class<T> clazz, final DecodeMethod method)
        throws CodecException
    {
        try (ByteArrayInputStream bis = new ByteArrayInputStream(data);
             LZFInputStream lzf = new LZFInputStream(bis)) {
            return getMapper(method).readValue(lzf, clazz);
        } catch (final IOException e) {
            throw new CodecException("Failed to deserialise", e);
        }
    }

    @Override
    public <T> T deserialise(final InputStream stream, final Class<T> clazz, final DecodeMethod method)
        throws CodecException
    {
        try (LZFInputStream lzf = new LZFInputStream(stream)) {
            return getMapper(method).readValue(lzf, clazz);
        } catch (final IOException e) {
            throw new CodecException("Failed to deserialise", e);
        }
    }

    @Override
    public byte[] serialise(final Object object)
        throws CodecException
    {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             LZFOutputStream lzf = new LZFOutputStream(bos)) {
            getMapper(DecodeMethod.getDefault()).writeValue(lzf, object);
            return bos.toByteArray();
        } catch (final IOException e) {
            throw new CodecException("Failed to serialise", e);
        }
    }

    protected ObjectMapper getMapper(final DecodeMethod method)
    {
        return method == DecodeMethod.STRICT ? strictMapper : lenientMapper;
    }
}
