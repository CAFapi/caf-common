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
package com.github.cafapi.util.processidentifier;

import java.util.UUID;

/**
 * Provides a unique identifier representing the current process.
 */
public final class ProcessIdentifier
{
    /**
     * A unique identifier for the current process.
     */
    private static final UUID processId;

    /**
     * Initializing an identifier for this process.
     */
    static {
        processId = UUID.randomUUID();
    }

    /**
     * Ensure this class can't be instantiated (it is intended to be static)
     */
    private ProcessIdentifier()
    {
    }

    /**
     * Returns a unique identifier for this process.
     *
     * @return Unique identifier for this process.
     */
    public static UUID getProcessId()
    {
        return processId;
    }
}
