package com.hp.caf.api;


/**
 * The overall, concise status of the health of a service.
 */
public enum HealthStatus
{
    /**
     * The health could not be determined at this time.
     */
    UNKNOWN,
    /**
     * The service is healthy and operating normally.
     */
    HEALTHY,
    /**
     * The service is unhealthy or failed in some manner.
     */
    UNHEALTHY;
}
