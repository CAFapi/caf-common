package com.hpe.caf.api;


/**
 * Dictates the decoding method for a Codec, specifying whether the Codec should
 * perform strict decoding or be more lenient which can help in being tolerant to
 * data from a subtly different class version of the object.
 */
public enum DecodeMethod
{
    /**
     * A "strict" Codec should not allow unknown fields. It may also reject inputs
     * with duplicate fields or fields that are null when null is not allowed.
     */
    STRICT,
    /**
     * A "lenient" Codec may accept but ignore unknown fields, and may also accept duplicate
     * entries within the input data (though how this behaves may be undefined). Unexpected
     * nulls may be replaced with the primitive defaults.
     */
    LENIENT;


    /**
     * @return the default, assumed DecodeMethod
     */
    public static DecodeMethod getDefault()
    {
        return STRICT;
    }
}
