package com.hpe.caf.util;

import java.util.Collection;
import java.util.concurrent.CountDownLatch;
import java.util.function.Consumer;

/**
 * Created by Michael.McAlynn on 04/03/2016.
 */
public class TestUtils {
    public static void RunMultiThreadedTest(Consumer<?> function, int numberOfThreads, final CountDownLatch gate, final Collection<String> errors) throws InterruptedException {
        RunMultiThreadedTest(function, numberOfThreads, gate, errors, false);
    }

    public static void RunMultiThreadedTest(Consumer<?> function, int numberOfThreads, final CountDownLatch gate, final Collection<String> errors, boolean printPerformance) throws InterruptedException {
        for (Integer i = 0; i < numberOfThreads; i++) {
            final Integer threadNumber = i;
            new Thread() {
                public void run() {
                    try {
                        long timeBeforeTest = System.currentTimeMillis();
                        function.accept(null);
                        long timeAfterTest = System.currentTimeMillis();

                        if(printPerformance){
                            System.out.println(GetCurrentThreadPrefix() + " Finished - " + threadNumber + " test took "
                                    + (timeAfterTest - timeBeforeTest) + " milliseconds to execute");
                        }
                    } catch (Exception e) {
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

    private static String GetCurrentThreadPrefix() {
        return Thread.currentThread().getName() + " ID#" + Thread.currentThread().getId() + " : ";
    }
}
