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

import com.fasterxml.jackson.databind.ObjectMapper;
import org.testng.Assert;
import org.testng.annotations.Test;
import tools.jackson.databind.JsonNode;

public class JacksonMigrationFunctionsTest
{
    private static final ObjectMapper JACKSON_2_MAPPER = new ObjectMapper();

    @Test
    public void testSimpleObjectConversion() throws Exception
    {
        final com.fasterxml.jackson.databind.JsonNode jackson2Node = JACKSON_2_MAPPER.readTree("{\"key\":\"value\"}");

        final JsonNode jackson3Node = JacksonMigrationFunctions.toJackson3(jackson2Node);

        Assert.assertNotNull(jackson3Node);
        Assert.assertEquals(jackson3Node.get("key").asText(), "value");
    }

    @Test
    public void testArrayConversion() throws Exception
    {
        final com.fasterxml.jackson.databind.JsonNode jackson2Node = JACKSON_2_MAPPER.readTree("[1,2,3]");

        final JsonNode jackson3Node = JacksonMigrationFunctions.toJackson3(jackson2Node);

        Assert.assertNotNull(jackson3Node);
        Assert.assertTrue(jackson3Node.isArray());
        Assert.assertEquals(jackson3Node.size(), 3);
        Assert.assertEquals(jackson3Node.get(0).asInt(), 1);
        Assert.assertEquals(jackson3Node.get(1).asInt(), 2);
        Assert.assertEquals(jackson3Node.get(2).asInt(), 3);
    }

    @Test
    public void testNestedObjectConversion() throws Exception
    {
        final com.fasterxml.jackson.databind.JsonNode jackson2Node = JACKSON_2_MAPPER.readTree("{\"outer\":{\"inner\":\"hello\"}}");

        final JsonNode jackson3Node = JacksonMigrationFunctions.toJackson3(jackson2Node);

        Assert.assertNotNull(jackson3Node);
        Assert.assertEquals(jackson3Node.get("outer").get("inner").asText(), "hello");
    }

    @Test
    public void testNullValueConversion() throws Exception
    {
        final com.fasterxml.jackson.databind.JsonNode jackson2Node = JACKSON_2_MAPPER.readTree("{\"key\":null}");

        final JsonNode jackson3Node = JacksonMigrationFunctions.toJackson3(jackson2Node);

        Assert.assertNotNull(jackson3Node);
        Assert.assertTrue(jackson3Node.get("key").isNull());
    }

    @Test
    public void testEmptyObjectConversion() throws Exception
    {
        final com.fasterxml.jackson.databind.JsonNode jackson2Node = JACKSON_2_MAPPER.readTree("{}");

        final JsonNode jackson3Node = JacksonMigrationFunctions.toJackson3(jackson2Node);

        Assert.assertNotNull(jackson3Node);
        Assert.assertTrue(jackson3Node.isObject());
        Assert.assertEquals(jackson3Node.size(), 0);
    }

    @Test
    public void testNumericFieldConversion() throws Exception
    {
        final com.fasterxml.jackson.databind.JsonNode jackson2Node = JACKSON_2_MAPPER.readTree("{\"count\":42,\"ratio\":3.14}");

        final JsonNode jackson3Node = JacksonMigrationFunctions.toJackson3(jackson2Node);

        Assert.assertNotNull(jackson3Node);
        Assert.assertEquals(jackson3Node.get("count").asInt(), 42);
        Assert.assertEquals(jackson3Node.get("ratio").asDouble(), 3.14, 0.001);
    }

    @Test
    public void testBooleanFieldConversion() throws Exception
    {
        final com.fasterxml.jackson.databind.JsonNode jackson2Node = JACKSON_2_MAPPER.readTree("{\"enabled\":true,\"disabled\":false}");

        final JsonNode jackson3Node = JacksonMigrationFunctions.toJackson3(jackson2Node);

        Assert.assertNotNull(jackson3Node);
        Assert.assertTrue(jackson3Node.get("enabled").asBoolean());
        Assert.assertFalse(jackson3Node.get("disabled").asBoolean());
    }
}
