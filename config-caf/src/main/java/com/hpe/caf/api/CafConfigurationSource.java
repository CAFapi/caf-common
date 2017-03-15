/*
 * Copyright 2015-2017 Hewlett Packard Enterprise Development LP.
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

import com.hpe.caf.naming.Name;
import com.hpe.caf.naming.ServicePath;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.lang3.text.StrLookup;
import org.apache.commons.lang3.text.StrSubstitutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

/**
 * Partial implementation of a ManagedConfigurationSource that performs hierarchical lookups based upon the service's ServicePath, and
 * recursive lookup for configuration objects that themselves have configuration in marked with the @Configuration annotation.
 */
public abstract class CafConfigurationSource implements ManagedConfigurationSource
{
    private final Cipher security;
    private final ServicePath id;
    private final Codec codec;
    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
    private final AtomicInteger confRequests = new AtomicInteger(0);
    private final AtomicInteger confErrors = new AtomicInteger(0);
    private static final Logger LOG = LoggerFactory.getLogger(CafConfigurationSource.class);

    /**
     * Each ConfigurationProvider itself takes some initial source of configuration which it may or may not use to initialise itself. The
     * initial "bootstrap" configuration comes from the worker core itself.
     *
     * @param bootstrapProvider the initial provider of configuration
     * @param cipher for decrypting information in a configuration file
     * @param servicePath to localise configuration for this service
     * @param codec provides a mechanism to deserialise the configuration format
     */
    public CafConfigurationSource(final BootstrapConfiguration bootstrapProvider, final Cipher cipher, final ServicePath servicePath,
                                  final Codec codec)
    {
        this.security = Objects.requireNonNull(cipher);
        this.id = Objects.requireNonNull(servicePath);
        this.codec = Objects.requireNonNull(codec);
        Objects.requireNonNull(bootstrapProvider);
    }

    /**
     * Acquire a configuration class from the provider. The requested class will be a simple Java object that when returned, and can be
     * interacted with using getters and other standard mechanisms. Configuration classes may themselves contain other configuration
     * objects, which will be recursively acquired if marked @Configuration. Any fields marked @Encrypted will be decrypted, any fields
     * marked and any validation annotations will be processed.
     *
     * @param configClass the class that represents your configuration
     * @param <T> the class that represents your configuration
     * @return the configuration class requested, if it can be deserialised
     * @throws ConfigurationException if the configuration class cannot be acquired or deserialised
     */
    @Override
    public final <T> T getConfiguration(final Class<T> configClass)
        throws ConfigurationException
    {
        Objects.requireNonNull(configClass);
        incrementRequests();
        T config = getCompleteConfig(configClass);
        Set<ConstraintViolation<T>> violations = getValidator().validate(config);
        if (violations.isEmpty()) {
            return config;
        } else {
            incrementErrors();
            LOG.error("Configuration constraint violations found for {}: {}", configClass.getSimpleName(), violations);
            throw new ConfigurationException("Configuration validation failed for " + configClass.getSimpleName());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final int getConfigurationRequests()
    {
        return confRequests.get();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final int getConfigurationErrors()
    {
        return confErrors.get();
    }

    protected Cipher getCipher()
    {
        return this.security;
    }

    protected ServicePath getServicePath()
    {
        return this.id;
    }

    protected Codec getCodec()
    {
        return this.codec;
    }

    protected Validator getValidator()
    {
        return this.validator;
    }

    /**
     * Acquire and return a stream of the serialised data from the transport source.
     *
     * @param configClass the configuration class to be acquired
     * @param relativePath the partial service path that defines the scope to try and acquire the configuration in
     * @return the stream containing the serailised configuration of the class
     * @throws ConfigurationException if the stream cannot be acquired
     */
    protected abstract InputStream getConfigurationStream(final Class configClass, final Name relativePath)
        throws ConfigurationException;

    /**
     * This is the recursive entry point for acquiring a complete configuration class to return. Attempt to acquire a deserialised object
     * representing the configuration class requested, and analyse it for declared fields marked @Configuration. If any are found, the
     * method recursively calls itself until all configuration is satisfied.
     *
     * @param configClass the class representing configuration to acquire
     * @param <T> the class representing configuration to acquire
     * @return the completed (at this level) configuration
     * @throws ConfigurationException if configuration cannot be acquired
     */
    private <T> T getCompleteConfig(final Class<T> configClass)
        throws ConfigurationException
    {
        T config = getConfig(configClass);
        for (final Field f : configClass.getDeclaredFields()) {
            if (f.isAnnotationPresent(Configuration.class)) {
                try {
                    Method setter = getMethod(f.getName(), configClass, PropertyDescriptor::getWriteMethod);
                    if (setter != null) {
                        setter.invoke(config, getCompleteConfig(f.getType()));
                    }
                } catch (final ConfigurationException e) {
                    LOG.debug("Didn't find any overriding configuration", e);
                } catch (final InvocationTargetException | IllegalAccessException e) {
                    incrementErrors();
                    throw new ConfigurationException("Failed to get complete configuration for " + configClass.getSimpleName(), e);
                }
            } else if (f.getType().equals(String.class) && f.isAnnotationPresent(Encrypted.class)) {
                try {
                    Method getter = getMethod(f.getName(), config.getClass(), PropertyDescriptor::getReadMethod);
                    Method setter = getMethod(f.getName(), config.getClass(), PropertyDescriptor::getWriteMethod);
                    if (getter != null && setter != null) {
                        setter.invoke(config, getCipher().decrypt(tokenSubstitutor((String) getter.invoke(config))));
                    }
                } catch (final CipherException | InvocationTargetException | IllegalAccessException e) {
                    throw new ConfigurationException("Failed to decrypt class fields", e);
                }
            } else if (f.getType().equals(String.class)) {
                try {
                    String propertyName = f.getName();
                    Method getter = getMethod(propertyName, config.getClass(), PropertyDescriptor::getReadMethod);
                    Method setter = getMethod(propertyName, config.getClass(), PropertyDescriptor::getWriteMethod);
                    if (getter != null && setter != null) {
                        // Property value may contain tokens that require substitution.
                        String propertyValueByToken = tokenSubstitutor((String) getter.invoke(config));
                        setter.invoke(config, propertyValueByToken);
                    }
                } catch (final InvocationTargetException | IllegalAccessException e) {
                    throw new ConfigurationException("Failed to get complete configuration for " + configClass.getSimpleName(), e);
                }
            }
        }
        return config;
    }

    /**
     * Acquire, deserialise and decrypt a configuration object from a data stream.
     *
     * @param configClass the class representing configuration to acquire
     * @param <T> the class representing configuration to acquire
     * @return the deserialised configuration object
     * @throws ConfigurationException if the configuration cannot be acquired
     */
    private <T> T getConfig(final Class<T> configClass)
        throws ConfigurationException
    {
        Iterator<Name> it = getServicePath().descendingPathIterator();
        while (it.hasNext()) {
            try (InputStream in = getConfigurationStream(configClass, it.next())) {
                return getCodec().deserialise(in, configClass);
            } catch (final ConfigurationException e) {
                LOG.trace("No configuration at this path level", e);
            } catch (final CodecException | IOException e) {
                incrementErrors();
                throw new ConfigurationException("Failed to get configuration for " + configClass.getSimpleName(), e);
            }
        }
        incrementErrors();
        throw new ConfigurationException("No configuration found for " + configClass.getSimpleName());
    }

    private static String tokenSubstitutor(final String source)
    {
        final StrSubstitutor strSubstitutor = new StrSubstitutor(
            new StrLookup<Object>()
        {
            @Override
            public String lookup(final String key)
            {
                return (System.getProperty(key) != null) ? System.getProperty(key) : System.getenv(key);
            }
        });

        return strSubstitutor.replace(source);
    }

    /**
     * Increase the number of configuration requests recorded.
     */
    protected void incrementRequests()
    {
        this.confRequests.incrementAndGet();
    }

    /**
     * Increase the number of configuration errors recorded.
     */
    protected void incrementErrors()
    {
        this.confErrors.incrementAndGet();
    }

    private Method getMethod(
        final String propertyName,
        final Class<?> beanClass,
        final Function<PropertyDescriptor, Method> function
    )
    {
        try {
            PropertyDescriptor propertyDescriptor = new PropertyDescriptor(propertyName, beanClass);
            return function.apply(propertyDescriptor);
        } catch (final IntrospectionException e) {
            LOG.debug(String.format("Unable to "
                + "create Property Descriptor from field %s :", propertyName) + System.lineSeparator()
                + ExceptionUtils.getStackTrace(e));
            return null;
        }
    }
}
