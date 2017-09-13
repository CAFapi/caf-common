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
package com.github.cafapi;

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