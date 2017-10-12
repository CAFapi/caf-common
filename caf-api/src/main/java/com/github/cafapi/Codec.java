/*
 * Copyright 2015-2017 EntIT Software LLC, a Micro Focus company.
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
package com.github.cafapi;

import java.io.InputStream;

/**
 * A Codec specifies methods to serialise data from a Java object to byte format, and deserialise data from byte format back into a
 * specified Java class.
 */
public interface Codec extends Decoder
{
    default <T> T deserialise(byte[] data, Class<T> clazz)
        throws CodecException
    {
        return deserialise(data, clazz, DecodeMethod.getDefault());
    }

    /**
     * Deserialise the given data into the specified class.
     *
     * @param data the serialised data
     * @param clazz the class the serialised data represents
     * @param method specifies whether to use strict or lenient decoding during deserialisation
     * @param <T> the class the serialised data represents
     * @return an instance of the class specified represented by the data
     * @throws CodecException if the data could not be deserialised
     */
    <T> T deserialise(byte[] data, Class<T> clazz, DecodeMethod method)
        throws CodecException;

    /**
     * Deserialise the given data into the specified class using the default decode method.
     *
     * @param stream the serialised data as a stream
     * @param clazz the class the serialised data represents
     * @param <T> the class the serialised data represents
     * @return an instance of the class specified represented by the data
     * @throws CodecException if the data could not be deserialised
     */
    @Override
    default <T> T deserialise(InputStream stream, Class<T> clazz)
        throws CodecException
    {
        return deserialise(stream, clazz, DecodeMethod.getDefault());
    }

    /**
     * Deserialise the given data into the specified class using the default decode method.
     *
     * @param stream the serialised data as a stream
     * @param clazz the class the serialised data represents
     * @param method specifies whether to use strict or lenient decoding during deserialisation
     * @param <T> the class the serialised data represents
     * @return an instance of the class specified represented by the data
     * @throws CodecException if the data could not be deserialised
     */
    <T> T deserialise(InputStream stream, Class<T> clazz, DecodeMethod method)
        throws CodecException;

    /**
     * Serialise the given object into a byte data form.
     *
     * @param object the object to serialise
     * @param <T> the class of the object to serialise
     * @return the serialised data of the given object
     * @throws CodecException if the object could not be serialised
     */
    <T> byte[] serialise(T object)
        throws CodecException;
}
