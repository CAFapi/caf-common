package com.hpe.caf.util;

import org.junit.Assert;
import org.junit.Test;

import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.function.Consumer;

/**
 * Tests for the ModuleProvider class
 */
public class ModuleProviderTest {
    @Test
    public void singleThreadGetModuleTest(){
        RunModulesTesting();
    }

    /**
     * Attempting to verify that ModuleProvider behaves as expected when multiple threads are accessing it. Due to the nature of
     * the code though this doesn't have a great coverage on adding unique classes into the ModuleProvider cached map
     * (can't think of a way to dynamically add classes per thread).
     */
    @Test
    public void multiThreadGetModuleTest(){
        int overallNumberOfThreads = 16;
        int numberOfIterationsForEachThread = 500;
        final CountDownLatch overallThreadGate = new CountDownLatch(overallNumberOfThreads);
        final Collection<String> errors = Collections.synchronizedList(new LinkedList<>());

        try {
            TestUtils.RunMultiThreadedTest(r ->
            {
                try {
                    for(int index = 0; index < numberOfIterationsForEachThread; index++ ) {
                        RunModulesTesting();
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }, overallNumberOfThreads, overallThreadGate, errors);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void RunModulesTesting(){
        ModuleProvider provider = ModuleProvider.getInstance();
        FirstMultipleTestImpl explicitFirst = new FirstMultipleTestImpl();
        SecondMultipleTestImpl explicitSecond = new SecondMultipleTestImpl();
        SingleTestImpl explicitSingle = new SingleTestImpl();

        MultipleTestInterface firstImpl = provider.getModule(MultipleTestInterface.class, FirstMultipleTestImpl.class.getSimpleName());
        Assert.assertNotNull(firstImpl);
        Assert.assertEquals("Expecting FirstMultipleTestImpl instance returned by ModuleProvider to behave the same as our explictly constructed instance",
                explicitFirst.getNumber(),
                firstImpl.getNumber());

        MultipleTestInterface secondImpl = provider.getModule(MultipleTestInterface.class, SecondMultipleTestImpl.class.getSimpleName());
        Assert.assertNotNull(secondImpl);
        Assert.assertEquals("Expecting SecondMultipleTestImpl instance returned by ModuleProvider to behave the same as our explictly constructed instance",
                explicitSecond.getNumber(),
                secondImpl.getNumber());

        //test retrieving a different key
        SingleTestInterface singleImpl = provider.getModule(SingleTestInterface.class, SingleTestImpl.class.getSimpleName());
        Assert.assertNotNull(singleImpl);
        Assert.assertEquals("Expecting SingleTestImpl instance returned by ModuleProvider to behave the same as our explictly constructed instance",
                explicitSingle.getName(),
                singleImpl.getName());

        //verify that a second call re-uses than existing object and doesn't create a new instance of it it
        MultipleTestInterface firstImpl_again = provider.getModule(MultipleTestInterface.class, "FirstMultipleTestImpl");
        Assert.assertNotNull(firstImpl_again);
        Assert.assertEquals("Expecting FirstMultipleTestImpl instance returned by second call to ModuleProvider to behave" +
                        " the same as our explictly constructed instance",
                explicitFirst.getNumber(),
                firstImpl_again.getNumber()
        );
        Assert.assertTrue("Expecting instance returned from first call to ModuleProvider for FirstMultipleTestImpl to be" +
                        "same instance as returned as second call for FirstMultipleTestImpl",
                firstImpl == firstImpl_again);
    }


}
