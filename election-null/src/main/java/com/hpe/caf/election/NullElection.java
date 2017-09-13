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
package com.hpe.caf.election;

import com.hpe.caf.api.Election;
import com.hpe.caf.api.ElectionCallback;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

public class NullElection implements Election
{
    private final AtomicBoolean elected = new AtomicBoolean(false);
    private final ElectionCallback callback;

    public NullElection(final ElectionCallback callback)
    {
        this.callback = Objects.requireNonNull(callback);
    }

    @Override
    public void enter()
    {
        if (elected.compareAndSet(false, true)) {
            callback.elected();
        }
    }

    @Override
    public void withdraw()
    {
        if (elected.compareAndSet(true, false)) {
            callback.rejected();
        }
    }

    @Override
    public void resign()
    {
        if (elected.compareAndSet(true, false)) {
            callback.rejected();
        }
    }
}
