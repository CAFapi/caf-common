package com.hpe.caf.util.processidentifier;

import com.hpe.caf.util.testing.MultiThreadTestUtil;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;

/**
 * Tests for ProcessIdentifier class
 */
public class ProcessIdentifierTest {
    /**
     * Test to verify that process identifier remains the same across multiple calls to 'getProcessId' method.
     */
    @Test
    public void testRetrieveProcessId(){
        UUID originalProcessID = ProcessIdentifier.getProcessId();
        final int overallNumberOfThreads = 16;
        final int iterationsPerThread = 10;
        final CountDownLatch overallThreadGate = new CountDownLatch(overallNumberOfThreads);
        final Collection<String> errors = Collections.synchronizedList(new LinkedList<>());

        try {
            MultiThreadTestUtil.RunMultiThreadedTest(r ->
            {
                try {
                    for (int i = 0; i < iterationsPerThread; i++) {
                        UUID currentThreadProcessId = ProcessIdentifier.getProcessId();
                        Assert.assertEquals("ProcessID retrieved by this thread should be the same as that obtained" +
                                "at start of test.", originalProcessID, currentThreadProcessId);
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }, overallNumberOfThreads, overallThreadGate, errors);
        } catch (InterruptedException e) {
            e.printStackTrace();
            Assert.fail(e.getMessage());
        }
        Assert.assertEquals("Expecting no errors thrown during multi-thread retrieval of process IDs", 0, errors.size());
    }
}
