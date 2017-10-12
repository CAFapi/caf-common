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
package com.github.cafapi.util;

import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.ServiceLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility class that provides methods for finding and returning components at runtime using the Java ServiceLoader.
 */
public final class ModuleLoader
{
    private static final Logger LOG = LoggerFactory.getLogger(ModuleLoader.class);

    private ModuleLoader()
    {
    }

    /**
     * Determine the first advertised service implementation for the specified interface. The implementations are advertised via the Java
     * "ServiceLoader" mechanism.
     *
     * @param intf the interface to find an advertised service implementation for
     * @param <T> the interface
     * @return an advertised implementation of intf of type T
     * @throws ModuleLoaderException if there is no advertised implementation available
     */
    public static <T> T getService(final Class<T> intf)
        throws ModuleLoaderException
    {
        return getService(intf, null);
    }

    /**
     * Determine the first advertised service implementation for the specified interface. The implementations are advertised via the Java
     * "ServiceLoader" mechanism.
     *
     * @param intf the interface to find an advertised service implementation for
     * @param defaultImpl the default implementation class if an advertised one is not found, may be null
     * @param <T> the interface
     * @return an advertised implementation of intf of type T
     * @throws ModuleLoaderException if the implementation is missing and no defaultImpl is specified
     */
    public static <T> T getService(final Class<T> intf, final Class<? extends T> defaultImpl)
        throws ModuleLoaderException
    {
        final T implementation = getServiceOrElse(intf, null);

        if (implementation != null) {
            return implementation;
        }

        if (defaultImpl == null) {
            throw new ModuleLoaderException("Missing implementation: " + intf);
        }

        try {
            return defaultImpl.getConstructor().newInstance();
        } catch (final InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new ModuleLoaderException("Cannot instantiate class", e);
        }
    }

    /**
     * Determine the first advertised service implementation for the specified interface. The implementations are advertised via the Java
     * "ServiceLoader" mechanism.
     *
     * @param <T> the interface
     * @param intf the interface to find an advertised service implementation for
     * @param defaultObj the default object to return if an advertised one is not found, may be null
     * @return an advertised implementation of intf of type T, or the default object if one is not found
     */
    public static <T> T getServiceOrElse(final Class<T> intf, final T defaultObj)
    {
        Objects.requireNonNull(intf);
        final T ret;
        List<T> implementations = getServices(intf);
        if (implementations.isEmpty()) {
            return defaultObj;
        } else {
            ret = implementations.get(0);
        }

        if (implementations.size() > 1) {
            LOG.warn("There is more than one implementation of {} available on the classpath, taking the first available", intf);
        }
        LOG.info("Detected component implementation {}", ret.getClass().getSimpleName());
        return ret;
    }

    /**
     * Get all advertised service implementations of the specified interface.
     *
     * @param intf the interface to find advertised service implementations of
     * @param <T> the interface
     * @return a collection of implementations of the specified interface
     */
    public static <T> List<T> getServices(final Class<T> intf)
    {
        Objects.requireNonNull(intf);
        List<T> ret = new LinkedList<>();
        for (final T t : ServiceLoader.load(intf)) {
            ret.add(t);
        }
        return ret;
    }
}
