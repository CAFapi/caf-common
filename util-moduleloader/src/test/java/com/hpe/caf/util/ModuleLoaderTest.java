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
package com.hpe.caf.util;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Collection;

public class ModuleLoaderTest
{
    /**
     * Ensure that for an interface with a single advertised implementation, ComponentLoader returns it.
     */
    @Test
    public void testSingle()
        throws Exception
    {
        ModuleLoader.getService(SingleTestInterface.class);
    }

    /**
     * Ensure that for an interface with two advertised implementations, ComponentLoader returns both.
     */
    @Test
    public void testMultiple()
        throws Exception
    {
        Collection<MultipleTestInterface> res = ModuleLoader.getServices(MultipleTestInterface.class);
        Assert.assertEquals(2, res.size());
    }

    /**
     * Ensure that for an interface with no advertised implementations, and no default provided, ComponentLoader throws an Exception.
     */
    @Test(expectedExceptions = Exception.class)
    public void testMissing()
        throws Exception
    {
        ModuleLoader.getService(MissingTestInterface.class);
    }

    /**
     * Ensure that for an interface with no advertised implementation but a provided default, ComponentLoader returns the default.
     */
    @Test
    public void testDefault()
        throws Exception
    {
        MissingTestInterface ret = ModuleLoader.getService(MissingTestInterface.class, MissingTestImpl.class);
        Assert.assertEquals(MissingTestImpl.class, ret.getClass());
    }
}
