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
package com.github.cafapi.common.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility class for use with try-with-resources, for classes where the close operation may throw an exception, but this is not a useful
 * check.
 *
 * @param <T> the class to close quietly
 */
public class QuietResource<T extends AutoCloseable> implements AutoCloseable
{
    private final T resource;
    private static final Logger LOG = LoggerFactory.getLogger(QuietResource.class);

    public QuietResource(final T resource)
    {
        this.resource = resource;
    }

    public T get()
    {
        return resource;
    }

    @Override
    public void close()
    {
        try {
            resource.close();
        } catch (final Exception e) {
            LOG.warn("Failed to close resource {}", get().getClass().getSimpleName(), e);
        }
    }
}
