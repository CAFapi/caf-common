/*
 * Copyright 2015-2023 Open Text.
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

import java.util.Objects;

/**
 * A result returned from a class that implements HealthReporter, indicating its health status and if necessary, a message detailing
 * further information.
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
