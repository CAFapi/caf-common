package com.hpe.caf.util;


import org.junit.Assert;
import org.junit.Test;

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
    @Test(expected = Exception.class)
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
