/*
 * Copyright 2015-2022 Micro Focus or one of its affiliates.
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
package com.hpe.caf.naming;


import org.testng.Assert;
import org.testng.annotations.Test;

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
        Iterator<Name> it = sp.descendingPathIterator();
        Assert.assertTrue(it.hasNext());
        Assert.assertEquals("group/Subgroup/name", it.next().toString());
        Assert.assertTrue(it.hasNext());
        Assert.assertEquals("group/Subgroup", it.next().toString());
        Assert.assertTrue(it.hasNext());
        Assert.assertEquals("group", it.next().toString());
        Assert.assertFalse(it.hasNext());
    }


    @Test(expected = NoSuchElementException.class)
    public void testDescendingPathIteratorException()
        throws InvalidNameException
    {
        ServicePath sp = new ServicePath("/group/name");
        Iterator<Name> it = sp.descendingPathIterator();
        it.next();
        it.next();
        it.next();
    }
}
