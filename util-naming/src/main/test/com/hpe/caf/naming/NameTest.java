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
package com.hpe.caf.naming;

import java.util.Iterator;
import java.util.NoSuchElementException;
import org.testng.Assert;
import org.testng.annotations.Test;

public class NameTest
{
    @Test
    public void testBasicName()
    {
        String input = "a/b/c";
        Name name = new Name(input);
        Assert.assertEquals(input, name.toString());
        Assert.assertEquals(3, name.size());
    }


    @Test
    public void testTrailingSlash()
    {
        String input = "a/b/c";
        Name name = new Name("/" + input + "/");
        Assert.assertEquals(input, name.toString());
        Assert.assertEquals(3, name.size());
    }


    @Test
    public void testGetIndex()
    {
        String input = "/a/b/c";
        Name name = new Name(input);
        Assert.assertEquals("a", name.getIndex(0));
        Assert.assertEquals("b", name.getIndex(1));
        Assert.assertEquals("c", name.getIndex(2));
    }


    @Test(expected = IllegalArgumentException.class)
    public void testGetIndexLowerBound()
    {
        String input = "/a/b/c";
        Name name = new Name(input);
        name.getIndex(-1);
    }


    @Test(expected = IllegalArgumentException.class)
    public void testGetIndexUpperBound()
    {
        String input = "/a/b/c";
        Name name = new Name(input);
        name.getIndex(3);
    }


    @Test
    public void testIterator()
    {
        String input = "/a/b/c";
        Name name = new Name(input);
        Iterator<String> it = name.iterator();
        Assert.assertTrue(it.hasNext());
        Assert.assertEquals("a", it.next());
        Assert.assertTrue(it.hasNext());
        Assert.assertEquals("b", it.next());
        Assert.assertTrue(it.hasNext());
        Assert.assertEquals("c", it.next());
        Assert.assertFalse(it.hasNext());
    }


    @Test(expected = NoSuchElementException.class)
    public void testIteratorBounds()
    {
        String input = "/a/b/c";
        Name name = new Name(input);
        Iterator<String> it = name.iterator();
        it.next();
        it.next();
        it.next();
        it.next();
    }


    @Test
    public void testGetPrefix()
    {
        String input = "/a/b/c";
        Name name = new Name(input);
        Name subName = name.getPrefix(2);
        Assert.assertEquals(2, subName.size());
        Assert.assertEquals("a/b", subName.toString());
    }


    @Test(expected = IllegalArgumentException.class)
    public void getGetPrefixLowerBound()
    {
        String input = "/a/b/c";
        Name name = new Name(input);
        name.getPrefix(-1);
    }


    @Test(expected = IllegalArgumentException.class)
    public void getGetPrefixUpperBound()
    {
        String input = "/a/b/c";
        Name name = new Name(input);
        name.getPrefix(4);
    }


    @Test
    public void testEquals()
    {
        String input = "/a/b/c";
        Name name = new Name(input);
        Name otherName = new Name(input);
        Assert.assertTrue(name.equals(otherName));
    }
}
