package com.hp.caf.api;


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


/**
 * A ConfigurationSource is intended to provide an implementation-agnostic method of
 * retrieving application-specific configuration. The configuration itself will be
 * provided by a simple Java object, which will be retrieved from some data source and
 * deserialised by a codec.
 */
public abstract class ConfigurationSource implements HealthReporter, ConfigurationMetricsReporter
{
    private final Cipher security;
    private final ServicePath id;
    private final Codec codec;
    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
    private final AtomicInteger confRequests = new AtomicInteger(0);
    private final AtomicInteger confErrors = new AtomicInteger(0);
    private static final Logger LOG = LoggerFactory.getLogger(ConfigurationSource.class);


    /**
     * Each ConfigurationProvider itself takes some initial source of configuration
     * which it may or may not use to initialise itself. The initial "bootstrap"
     * configuration comes from the worker core itself.
     * @param bootstrapProvider the initial provider of configuration
     * @param cipher for decrypting information in a configuration file
     * @param servicePath to localise configuration for this service
     * @param codec provides a mechanism to deserialise the configuration format
     */
    public ConfigurationSource(final BootstrapConfiguration bootstrapProvider, final Cipher cipher, final ServicePath servicePath,
            final Codec codec)
    {
        this.security = Objects.requireNonNull(cipher);
        this.id = Objects.requireNonNull(servicePath);
        this.codec = Objects.requireNonNull(codec);
        Objects.requireNonNull(bootstrapProvider);
    }


    /**
     * Acquire a configuration class from the provider. The requested class will be
     * a simple Java object that when returned, and can be interacted with using getters
     * and other standard mechanisms. Configuration classes may themselves contain other
     * configuration objects, which will be recursively acquired if marked @Configuration.
     * Any fields marked @Encrypted will be decrypted, any fields marked and any
     * validation annotations will be processed.
     * @param configClass the class that represents your configuration
     * @param <T> the class that represents your configuration
     * @return the configuration class requested, if it can be deserialised
     * @throws ConfigurationException if the configuration class cannot be acquired or deserialised
     */
    public final <T> T getConfiguration(final Class<T> configClass)
            throws ConfigurationException
    {
        Objects.requireNonNull(configClass);
        incrementRequests();
        T config = getCompleteConfig(configClass);
        Set<ConstraintViolation<T>> violations =  getValidator().validate(config);
        if ( violations.isEmpty() ) {
            return config;
        } else {
            incrementErrors();
            throw new ConfigurationException("Configuration file failed validation");
        }
    }


    /**
     * Perform necessary shutdown operations.
     */
    public abstract void shutdown();


    @Override
    public final int getConfigurationRequests()
    {
        return confRequests.get();
    }


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
     * @param configClass the configuration class to be acquired
     * @return the stream containing the serailised configuration of the class
     * @throws ConfigurationException if the stream cannot be acquired
     */
    protected abstract InputStream getConfigurationStream(final Class configClass, final String relativePath)
            throws ConfigurationException;


    /**
     * This is the recursive entry point for acquiring a complete configuration class
     * to return. Attempt to acquire a deserialised object representing the configuration
     * class requested, and analyse it for declared fields marked @Configuration. If any
     * are found, the method recursively calls itself until all configuration is satisfied.
     * @param configClass the class representing configuration to acquire
     * @param <T> the class representing configuration to acquire
     * @return the completed (at this level) configuration
     * @throws ConfigurationException if configuration cannot be acquired
     */
    private <T> T getCompleteConfig(final Class<T> configClass)
            throws ConfigurationException
    {
        T config = getConfig(configClass);
        for ( Field f : configClass.getDeclaredFields() ) {
            if ( f.isAnnotationPresent(Configuration.class) ) {
                try {
                    Method setter = new PropertyDescriptor(f.getName(), configClass).getWriteMethod();
                    if ( setter != null ) {
                        setter.invoke(config, getCompleteConfig(f.getType()));
                    }
                } catch (ConfigurationException e) {
                    LOG.debug("Didn't find any overriding configuration", e);
                } catch (IntrospectionException | InvocationTargetException | IllegalAccessException e) {
                    incrementErrors();
                    throw new ConfigurationException("Failed to get complete configuration", e);
                }
            }
        }
        return config;
    }


    /**
     * Acquire, deserialise and decrypt a configuration object from a data stream.
     * @param configClass the class representing configuration to acquire
     * @param <T> the class representing configuration to acquire
     * @return the deserialised configuration object
     * @throws ConfigurationException if the configuration cannot be acquired
     */
    private <T> T getConfig(final Class<T> configClass)
            throws ConfigurationException
    {
        Iterator<String> it = getServicePath().descendingPathIterator();
        while ( it.hasNext() ) {
            try (InputStream in = getConfigurationStream(configClass, it.next())) {
                T deser = getCodec().deserialise(in, configClass);
                return getCipher().decrypt(deser);
            } catch (ConfigurationException e ) {
                LOG.trace("No configuration at this path level", e);
            } catch (CipherException | CodecException | IOException e) {
                incrementErrors();
                throw new ConfigurationException("Failed to get configuration", e);
            }
        }
        incrementErrors();
        throw new ConfigurationException("No configuration found");
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
}
