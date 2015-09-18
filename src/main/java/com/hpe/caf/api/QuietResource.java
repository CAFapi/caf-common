package com.hpe.caf.api;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Utility class for use with try-with-resources, for classes where the close operation
 * may throw an exception, but this is not a useful check.
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
        } catch (Exception e) {
            LOG.warn("Failed to close resource {}", get().getClass().getSimpleName(), e);
        }
    }
}
