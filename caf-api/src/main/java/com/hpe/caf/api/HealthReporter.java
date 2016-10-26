package com.hpe.caf.api;


/**
 * Indicates this object can report on its health.
 * @since 1.0
 */
public interface HealthReporter
{
    /**
     * @return the result of the health check
     */
    HealthResult healthCheck();
}
