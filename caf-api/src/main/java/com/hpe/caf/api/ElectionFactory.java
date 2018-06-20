/*
 * Copyright 2015-2018 Micro Focus or one of its affiliates.
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
 * Factory class for returning Election objects for a specific service election.
 */
public interface ElectionFactory
{
    /**
     * Request a handle class used to interact with a particular service election.
     *
     * @param serviceReference a unique handle for an election, typically a service name
     * @param callback the object that will be called upon the instance being elected/unelected
     * @return an object to interact with for a specific election
     */
    Election getElection(String serviceReference, ElectionCallback callback);
}
