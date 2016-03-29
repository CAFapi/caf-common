package com.hpe.caf.util.testing;

import org.junit.Assert;

import java.util.Collection;
import java.util.concurrent.CountDownLatch;
import java.util.function.Consumer;

/**
 * Utility class for running multi-threaded tests.
 */
public class MultiThreadTestUtil {
    public static void RunMultiThreadedTest(Consumer<?> function, int numberOfThreads, final CountDownLatch gate, final Collection<String> errors) throws InterruptedException {
        for (Integer i = 0; i < numberOfThreads; i++) {
            new Thread() {
                public void run() {
                    try {
                        function.accept(null);
                    } catch (Exception e) {
                        gate.countDown();
                        errors.add(GetCurrentThreadPrefix() + " Failure - " + e);
                        throw new RuntimeException(e);
                    }
                    catch(AssertionError e){
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

    private static String GetCurrentThreadPrefix() {
        return Thread.currentThread().getName() + " ID#" + Thread.currentThread().getId() + " : ";
    }

}
