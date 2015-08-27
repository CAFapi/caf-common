package com.hp.caf.api;


/**
 * Indicates this object can report on its health.
 */
public interface HealthReporter
{
    /**
     * @return the result of the health check
     */
    HealthResult healthCheck();
}
