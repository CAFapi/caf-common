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
package com.github.cafapi.common.util.ref;

import java.io.InputStream;

/**
 * Interface for defining how to retrieve objects or streams from a reference.
 */
public abstract class DataSource
{
    /**
     * Retrieve an object of the specified class identified by a specific reference.
     *
     * @param ref the reference that points to an instance of an object of the specified class
     * @param clazz the class of the object instance the reference points to
     * @param <T> the type of the object instance
     * @return the object instance of the specified class identified by the specified reference
     * @throws DataSourceException if the object instance cannot be acquired
     */
    public abstract <T> T getObject(final String ref, final Class<T> clazz)
        throws DataSourceException;

    /**
     * Retrieve a stream of data identified by a specific reference.
     *
     * @param ref the reference that points to a stream of data
     * @return a stream of data identified by the specified reference
     * @throws DataSourceException if the data stream cannot be acquired
     */
    public abstract InputStream getStream(final String ref)
        throws DataSourceException;

    /**
     * Determine the size of the data abstracted.
     *
     * @param ref the reference that points to the data
     * @return the size of the data, in bytes
     * @throws DataSourceException if the data size cannot be acquired
     */
    public abstract long getDataSize(final String ref)
        throws DataSourceException;
}
