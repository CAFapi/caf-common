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
package com.github.cafapi.util.processidentifier;

import com.github.cafapi.util.testing.MultiThreadTestUtil;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Tests for ProcessIdentifier class
 */
public class ProcessIdentifierTest
{
    /**
     * Test to verify that process identifier remains the same across multiple calls to 'getProcessId' method.
     */
    @Test
    public void testRetrieveProcessId()
    {
        UUID originalProcessID = ProcessIdentifier.getProcessId();
        final int overallNumberOfThreads = 16;
        final int iterationsPerThread = 10;
        final CountDownLatch overallThreadGate = new CountDownLatch(overallNumberOfThreads);
        final Collection<String> errors = Collections.synchronizedList(new LinkedList<>());

        try {
            MultiThreadTestUtil.RunMultiThreadedTest(r
                -> {
                try {
                    for (int i = 0; i < iterationsPerThread; i++) {
                        UUID currentThreadProcessId = ProcessIdentifier.getProcessId();
                        Assert.assertEquals(originalProcessID, currentThreadProcessId,
                                            "ProcessID retrieved by this thread should be the same as that obtained at start of test.");
                    }
                } catch (final Exception e) {
                    throw new RuntimeException(e);
                }
            }, overallNumberOfThreads, overallThreadGate, errors);
        } catch (final InterruptedException e) {
            e.printStackTrace();
            Assert.fail(e.getMessage());
        }
        Assert.assertEquals(0, errors.size(), "Expecting no errors thrown during multi-thread retrieval of process IDs");
    }
}
