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

import java.util.HashMap;
import java.util.Map;
import javax.validation.Validation;
import javax.validation.Validator;
import org.testng.Assert;
import org.testng.annotations.Test;

public class ContainsStringKeysValidatorTest
{
    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    public void testValidator()
    {
        TestClass test = new TestClass();
        Map<String, Integer> a = new HashMap<>();
        a.put("testKey", 1);
        test.setTestMap(a);
        Assert.assertTrue(validator.validate(test).isEmpty());
        a.clear();
        a.put("stuff", 2);
        Assert.assertFalse(validator.validate(test).isEmpty());
    }

    private class TestClass
    {
        @ContainsStringKeys(keys = {"testKey"})
        Map<String, Integer> testMap;

        public Map<String, Integer> getTestMap()
        {
            return testMap;
        }

        public void setTestMap(final Map<String, Integer> testMap)
        {
            this.testMap = testMap;
        }
    }
}
