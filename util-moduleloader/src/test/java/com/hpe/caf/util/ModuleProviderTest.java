/*
 * Copyright 2015-2019 Micro Focus or one of its affiliates.
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
import java.util.Collections;
import java.util.LinkedList;
import java.util.concurrent.CountDownLatch;

/**
 * Tests for the ModuleProvider class
 */
public class ModuleProviderTest
{
    @Test
    public void singleThreadGetModuleTest()
    {
        RunModulesTesting();
    }

    /**
     * Attempting to verify that ModuleProvider behaves as expected when multiple threads are accessing it. Due to the nature of the code
     * though this doesn't have a great coverage on adding unique classes into the ModuleProvider cached map (can't think of a way to
     * dynamically add classes per thread).
     */
    @Test
    public void multiThreadGetModuleTest()
    {
        int overallNumberOfThreads = 16;
        int numberOfIterationsForEachThread = 500;
        final CountDownLatch overallThreadGate = new CountDownLatch(overallNumberOfThreads);
        final Collection<String> errors = Collections.synchronizedList(new LinkedList<>());

        try {
            TestUtils.RunMultiThreadedTest(r
                -> {
                try {
                    for (int index = 0; index < numberOfIterationsForEachThread; index++) {
                        RunModulesTesting();
                    }
                } catch (final Exception e) {
                    throw new RuntimeException(e);
                }
            }, overallNumberOfThreads, overallThreadGate, errors);
        } catch (final InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void RunModulesTesting()
    {
        ModuleProvider provider = ModuleProvider.getInstance();
        FirstMultipleTestImpl explicitFirst = new FirstMultipleTestImpl();
        SecondMultipleTestImpl explicitSecond = new SecondMultipleTestImpl();
        SingleTestImpl explicitSingle = new SingleTestImpl();

        MultipleTestInterface firstImpl = provider.getModule(MultipleTestInterface.class, FirstMultipleTestImpl.class.getSimpleName());
        Assert.assertNotNull(firstImpl);
        Assert.assertEquals(explicitFirst.getNumber(), firstImpl.getNumber(),
                            "Expecting FirstMultipleTestImpl instance returned by ModuleProvider"
                            + " to behave the same as our explictly constructed instance");

        MultipleTestInterface secondImpl = provider.getModule(MultipleTestInterface.class, SecondMultipleTestImpl.class.getSimpleName());
        Assert.assertNotNull(secondImpl);
        Assert.assertEquals(explicitSecond.getNumber(), secondImpl.getNumber(),
                            "Expecting SecondMultipleTestImpl instance returned by ModuleProvider"
                            + " to behave the same as our explictly constructed instance");

        //test retrieving a different key
        SingleTestInterface singleImpl = provider.getModule(SingleTestInterface.class, SingleTestImpl.class.getSimpleName());
        Assert.assertNotNull(singleImpl);
        Assert.assertEquals(explicitSingle.getName(), singleImpl.getName(),
                            "Expecting SingleTestImpl instance returned by ModuleProvider"
                            + " to behave the same as our explictly constructed instance");

        //verify that a second call re-uses than existing object and doesn't create a new instance of it it
        MultipleTestInterface firstImpl_again = provider.getModule(MultipleTestInterface.class, "FirstMultipleTestImpl");
        Assert.assertNotNull(firstImpl_again);
        Assert.assertEquals(explicitFirst.getNumber(), firstImpl_again.getNumber(),
                            "Expecting FirstMultipleTestImpl instance returned by second call to ModuleProvider"
                            + " to behave the same as our explictly constructed instance");
        Assert.assertTrue(firstImpl == firstImpl_again,
                          "Expecting instance returned from first call to ModuleProvider"
                          + " for FirstMultipleTestImpl to be same instance as returned as second call for FirstMultipleTestImpl");
    }
}
