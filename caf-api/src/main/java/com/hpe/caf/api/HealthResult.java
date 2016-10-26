package com.hpe.caf.api;


import java.util.Objects;


/**
 * A result returned from a class that implements HealthReporter, indicating its health status
 * and if necessary, a message detailing further information.
 * @since 1.0
 */
public final class HealthResult
{
    public static final HealthResult RESULT_HEALTHY = new HealthResult(HealthStatus.HEALTHY);
    private final HealthStatus status;
    private String message;


    public HealthResult(final HealthStatus healthStatus, final String healthMessage)
    {
        this.status = Objects.requireNonNull(healthStatus);
        this.message = healthMessage;
    }


    public HealthResult(final HealthStatus healthStatus)
    {
        this.status = healthStatus;
    }


    public HealthStatus getStatus()
    {
        return status;
    }


    public String getMessage()
    {
        return message;
    }
}
