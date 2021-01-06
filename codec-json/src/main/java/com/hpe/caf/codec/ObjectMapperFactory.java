/*
 * Copyright 2015-2021 Micro Focus or one of its affiliates.
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
package com.hpe.caf.codec;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.guava.GuavaModule;

/**
 * Provides pre-configured strict and lenient Jackson JSON ObjectMapper instances.
 */
public final class ObjectMapperFactory
{
    private ObjectMapperFactory()
    {
    }

    private static ObjectMapper getCommonMapper()
    {
        ObjectMapper commonMapper = new ObjectMapper();
        commonMapper.registerModule(new GuavaModule());
        commonMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        commonMapper.setVisibility(PropertyAccessor.GETTER, JsonAutoDetect.Visibility.NONE);

        return commonMapper;
    }

    /**
     * @return an ObjectMapper which fails on unknown properties, does not accept null for primitives, or duplicates
     */
    public static ObjectMapper getStrictMapper()
    {
        ObjectMapper strictMapper = getCommonMapper();
        strictMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);
        strictMapper.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, true);
        strictMapper.configure(DeserializationFeature.FAIL_ON_READING_DUP_TREE_KEY, true);
        return strictMapper;
    }

    /**
     * @return an ObjectMapper which ignores unknown properties, uses defaults in case of null for primitive, and accepts duplicates
     */
    public static ObjectMapper getLenientMapper()
    {
        ObjectMapper lenientMapper = getCommonMapper();
        lenientMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        lenientMapper.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, false);
        lenientMapper.configure(DeserializationFeature.FAIL_ON_READING_DUP_TREE_KEY, false);
        return lenientMapper;
    }
}
