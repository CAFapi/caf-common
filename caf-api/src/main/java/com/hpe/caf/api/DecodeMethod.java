/*
 * Copyright 2015-2018 Micro Focus or one of its affiliates.
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
package com.hpe.caf.api;

/**
 * Dictates the decoding method for a Codec, specifying whether the Codec should perform strict decoding or be more lenient which can help
 * in being tolerant to data from a subtly different class version of the object.
 */
public enum DecodeMethod
{
    /**
     * A "strict" Codec should not allow unknown fields. It may also reject inputs with duplicate fields or fields that are null when null
     * is not allowed.
     */
    STRICT,
    /**
     * A "lenient" Codec may accept but ignore unknown fields, and may also accept duplicate entries within the input data (though how
     * this behaves may be undefined). Unexpected nulls may be replaced with the primitive defaults.
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
