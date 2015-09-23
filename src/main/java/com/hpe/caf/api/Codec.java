package com.hpe.caf.api;


import java.io.InputStream;


/**
 * A Codec specifies methods to serialise data from a Java object to byte format,
 * and deserialise data from byte format back into a specified Java class.
 */
public abstract class Codec
{
    /**
     * Deserialise the given data into the specified class using the default decode method.
     * @param data the serialised data
     * @param clazz the class the serialised data represents
     * @param <T> the class the serialised data represents
     * @return an instance of the class specified represented by the data
     * @throws CodecException if the data could not be deserialised
     */
    public final <T> T deserialise(final byte[] data, final Class<T> clazz)
        throws CodecException
    {
        return deserialise(data, clazz, DecodeMethod.getDefault());
    }


    /**
     * Deserialise the given data into the specified class.
     * @param data the serialised data
     * @param clazz the class the serialised data represents
     * @param method specifies whether to use strict or lenient decoding during deserialisation
     * @param <T> the class the serialised data represents
     * @return an instance of the class specified represented by the data
     * @throws CodecException if the data could not be deserialised
     */
    public abstract <T> T deserialise(final byte[] data, final Class<T> clazz, final DecodeMethod method)
        throws CodecException;


    /**
     * Deserialise the given data into the specified class using the default decode method.
     * @param stream the serialised data as a stream
     * @param clazz the class the serialised data represents
     * @param <T> the class the serialised data represents
     * @return an instance of the class specified represented by the data
     * @throws CodecException if the data could not be deserialised
     */
    public final <T> T deserialise(final InputStream stream, final Class<T> clazz)
        throws CodecException
    {
        return deserialise(stream, clazz, DecodeMethod.getDefault());
    }


    /**
     * Deserialise the given data into the specified class using the default decode method.
     * @param stream the serialised data as a stream
     * @param clazz the class the serialised data represents
     * @param method specifies whether to use strict or lenient decoding during deserialisation
     * @param <T> the class the serialised data represents
     * @return an instance of the class specified represented by the data
     * @throws CodecException if the data could not be deserialised
     */
    public abstract <T> T deserialise(final InputStream stream, final Class<T> clazz, final DecodeMethod method)
        throws CodecException;


    /**
     * Serialise the given object into a byte data form.
     * @param object the object to serialise
     * @param <T> the class of the object to serialise
     * @return the serialised data of the given object
     * @throws CodecException if the object could not be serialised
     */
    public abstract <T> byte[] serialise(final T object)
        throws CodecException;
}
