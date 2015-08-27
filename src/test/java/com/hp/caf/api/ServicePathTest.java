package com.hp.caf.api;


import org.junit.Assert;
import org.junit.Test;

import javax.naming.InvalidNameException;
import java.util.Iterator;
import java.util.NoSuchElementException;


public class ServicePathTest
{
    @Test
    public void testValid()
            throws InvalidNameException
    {
        ServicePath sp = new ServicePath("group/Subgroup/name");
        Assert.assertEquals("group", sp.getRoot());
        Assert.assertEquals("Subgroup", sp.getGroup());
        Assert.assertEquals("name", sp.getLeaf());
    }


    @Test
    public void testStrip()
            throws InvalidNameException
    {
        ServicePath sp = new ServicePath("/group/Subgroup/name/");
        Assert.assertEquals("group", sp.getRoot());
        Assert.assertEquals("Subgroup", sp.getGroup());
        Assert.assertEquals("name", sp.getLeaf());
    }


    @Test(expected = InvalidNameException.class)
    public void testEmptyGroup()
        throws InvalidNameException
    {
        ServicePath sp = new ServicePath("/group/Subgroup//name/");
    }


    @Test(expected = InvalidNameException.class)
    public void testNoGroup()
        throws InvalidNameException
    {
        ServicePath sp = new ServicePath("/name");
    }


    @Test
    public void testGroupIterator()
            throws InvalidNameException
    {
        ServicePath sp = new ServicePath("/group/Subgroup/name/");
        Iterator<String> it = sp.groupIterator();
        Assert.assertTrue(it.hasNext());
        Assert.assertEquals("group", it.next());
        Assert.assertTrue(it.hasNext());
        Assert.assertEquals("Subgroup", it.next());
        Assert.assertFalse(it.hasNext());
    }


    @Test(expected = NoSuchElementException.class)
    public void testGroupIteratorException()
        throws InvalidNameException
    {
        ServicePath sp = new ServicePath("/group/name");
        Iterator<String> it = sp.groupIterator();
        it.next();
        it.next();
    }


    @Test
    public void testDescendingPathIterator()
            throws InvalidNameException
    {
        ServicePath sp = new ServicePath("/group/Subgroup/name/");
        Iterator<String> it = sp.descendingPathIterator();
        Assert.assertTrue(it.hasNext());
        Assert.assertEquals("group/Subgroup/name", it.next());
        Assert.assertTrue(it.hasNext());
        Assert.assertEquals("group/Subgroup", it.next());
        Assert.assertTrue(it.hasNext());
        Assert.assertEquals("group", it.next());
        Assert.assertFalse(it.hasNext());
    }


    @Test(expected = NoSuchElementException.class)
    public void testDescendingPathIteratorException()
        throws InvalidNameException
    {
        ServicePath sp = new ServicePath("/group/name");
        Iterator<String> it = sp.descendingPathIterator();
        it.next();
        it.next();
        it.next();
    }
}
