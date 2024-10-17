/*
 * Copyright 2015-2024 Open Text.
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
package com.opentext.caf.api;

/**
 * The endpoint that an election process will call to upon election or rejection of this instance from a particular Election.
 */
public interface ElectionCallback
{
    /**
     * Indicates this instance has been elected.
     */
    void elected();

    /**
     * Indicates this instance has been unelected.
     */
    void rejected();
}
