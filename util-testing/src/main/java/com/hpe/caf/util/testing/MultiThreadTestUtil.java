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
package com.hpe.caf.util.testing;

import org.junit.Assert;

import java.util.Collection;
import java.util.concurrent.CountDownLatch;
import java.util.function.Consumer;

/**
 * Utility class for running multi-threaded tests.
 */
public class MultiThreadTestUtil
{
    public static void RunMultiThreadedTest(
        final Consumer<?> function,
        final int numberOfThreads,
        final CountDownLatch gate,
        final Collection<String> errors
    )
        throws InterruptedException
    {
        for (Integer i = 0; i < numberOfThreads; i++) {
            new Thread()
            {
                public void run()
                {
                    try {
                        function.accept(null);
                    } catch (final Exception e) {
                        gate.countDown();
                        errors.add(GetCurrentThreadPrefix() + " Failure - " + e);
                        throw new RuntimeException(e);
                    } catch (final AssertionError e) {
                        gate.countDown();
                        errors.add(GetCurrentThreadPrefix() + " Failure - " + e);
                        Assert.fail(e.getMessage());
                    }
                    gate.countDown();
                }
            }.start();
        }
        gate.await();
    }

    private static String GetCurrentThreadPrefix()
    {
        return Thread.currentThread().getName() + " ID#" + Thread.currentThread().getId() + " : ";
    }
}
