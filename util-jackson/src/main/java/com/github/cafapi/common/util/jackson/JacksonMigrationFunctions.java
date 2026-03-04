/*
 * Copyright 2015-2026 Open Text.
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
package com.github.cafapi.common.util.jackson;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

public final class JacksonMigrationFunctions
{
    private static final ObjectMapper JACKSON_3_MAPPER = new ObjectMapper();

    private JacksonMigrationFunctions()
    {
    }

    public static JsonNode toJackson3(final com.fasterxml.jackson.databind.JsonNode jsonNode)
    {
        return JACKSON_3_MAPPER.readTree(jsonNode.toString());
    }
}
