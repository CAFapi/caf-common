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
package com.hpe.caf.api;

/**
 * Allows interaction with the election process of an individual service election.
 */
public interface Election
{
    /**
     * Request to enter the election process of this specific election
     *
     * @throws ElectionException if it is not possible to enter for election
     */
    void enter()
        throws ElectionException;

    /**
     * Request withdrawal as leader, triggering a re-election process.
     */
    void withdraw();

    /**
     * Terminate participation in this election process, withdrawing as leader if elected.
     */
    void resign();
}
