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

import java.util.Collection;
import java.util.concurrent.CountDownLatch;
import java.util.function.Consumer;

/**
 * Created by Michael.McAlynn on 04/03/2016.
 */
public class TestUtils
{
    public static void RunMultiThreadedTest(final Consumer<?> function,
                                            final int numberOfThreads,
                                            final CountDownLatch gate,
                                            final Collection<String> errors)
        throws InterruptedException
    {
        RunMultiThreadedTest(function, numberOfThreads, gate, errors, false);
    }

    public static void RunMultiThreadedTest(final Consumer<?> function,
                                            final int numberOfThreads,
                                            final CountDownLatch gate,
                                            final Collection<String> errors,
                                            final boolean printPerformance)
        throws InterruptedException
    {
        for (Integer i = 0; i < numberOfThreads; i++) {
            final Integer threadNumber = i;
            new Thread()
            {
                public void run()
                {
                    try {
                        long timeBeforeTest = System.currentTimeMillis();
                        function.accept(null);
                        long timeAfterTest = System.currentTimeMillis();

                        if (printPerformance) {
                            System.out.println(GetCurrentThreadPrefix() + " Finished - " + threadNumber + " test took "
                                + (timeAfterTest - timeBeforeTest) + " milliseconds to execute");
                        }
                    } catch (final Exception e) {
                        gate.countDown();
                        errors.add(GetCurrentThreadPrefix() + " Failure - " + e);
                        throw new RuntimeException(e);
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
