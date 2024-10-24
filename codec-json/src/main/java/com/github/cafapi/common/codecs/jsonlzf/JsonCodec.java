/*
 * Copyright 2015-2024 Open Text.
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
package com.github.cafapi.common.codecs.jsonlzf;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.cafapi.common.api.Codec;
import com.github.cafapi.common.api.CodecException;
import com.github.cafapi.common.api.DecodeMethod;
import com.github.cafapi.common.api.FileExtensions;

import java.io.IOException;
import java.io.InputStream;

/**
 * Implementation of Codec that supports serialisation and deserialisation to and form JSON format.
 */
@FileExtensions({"", "json"})
public class JsonCodec implements Codec
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
        } catch (final IOException e) {
            throw new CodecException("Failed to deserialise", e);
        }
    }

    @Override
    public <T> T deserialise(final InputStream stream, final Class<T> clazz, final DecodeMethod method)
        throws CodecException
    {
        try {
            return getMapper(method).readValue(stream, clazz);
        } catch (final IOException e) {
            throw new CodecException("Failed to deserialise", e);
        }
    }

    @Override
    public byte[] serialise(final Object object)
        throws CodecException
    {
        try {
            return getMapper(DecodeMethod.getDefault()).writeValueAsBytes(object);
        } catch (final JsonProcessingException e) {
            throw new CodecException("Failed to serialise", e);
        }
    }

    protected ObjectMapper getMapper(final DecodeMethod method)
    {
        return method == DecodeMethod.STRICT ? strictMapper : lenientMapper;
    }
}
