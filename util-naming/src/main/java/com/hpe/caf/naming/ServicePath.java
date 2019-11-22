/*
 * Copyright 2015-2020 Micro Focus or one of its affiliates.
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

import javax.naming.InvalidNameException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * Simple object to store the service path, expected to be in the form /group/subgroup/id. Internally this is represented by a
 * javax.naming.Name object. Any leading or terminating forward-slashes are removed upon creation.
 *
 * Valid path: a/b/c Valid path: /a/b/c/ (leading and terminating slashes will be removed) Invalid path: /a/b//c/ (empty naming groups are
 * not allowed) Invalid path: /a (no group)
 */
public final class ServicePath implements Iterable<String>
{
    private final Name pathName;

    /**
     * Create a new ServicePath from a String.
     *
     * @param path the String to create the ServicePath from
     * @throws InvalidNameException if there are invalid characters, empty groups, or less than 2 components
     */
    public ServicePath(final String path)
        throws InvalidNameException
    {
        Objects.requireNonNull(path);
        pathName = new Name(path);
        if (pathName.size() < 2) {
            throw new InvalidNameException("At minimum, a service must have a group");
        }
        for (final String n : this) {
            if (n.isEmpty()) {
                throw new InvalidNameException("Empty subgroups are invalid");
            }
        }
    }

    /**
     * Return the root of the service path, ie. the first group the service is a part of. In the service path "/a/b/c", this will be "a".
     *
     * @return the root of the service path
     */
    public String getRoot()
    {
        return pathName.getIndex(0);
    }

    /**
     * Return the leaf or "tip" of the service path, ie. the final part of the service name. In the service path "/a/b/c", this will be
     * "c".
     *
     * @return the leaf of the service path
     */
    public String getLeaf()
    {
        return pathName.getIndex(pathName.size() - 1);
    }

    /**
     * Return the immediate group of the service, ie. the node above the leaf. In the service path "/a/b/c", this will be "b".
     *
     * @return the immediate group of the service
     */
    public String getGroup()
    {
        return pathName.getIndex(pathName.size() - 2);
    }

    /**
     * @return the full service path
     */
    public Name getPath()
    {
        return pathName;
    }

    /**
     * @return the full service path, as a String
     */
    @Override
    public String toString()
    {
        return getPath().toString();
    }

    /**
     * {@inheritDoc}
     *
     * Each iteration is the next element of the service path.
     */
    @Override
    public Iterator<String> iterator()
    {
        return pathName.iterator();
    }

    /**
     * @return an iterator that only iterates over the groups and subgroups of the service
     */
    public Iterator<String> groupIterator()
    {
        return pathName.getPrefix(pathName.size() - 1).iterator();
    }

    /**
     * @return an Iterator where each element is the entire service path up to the (decrementing) index
     */
    public Iterator<Name> descendingPathIterator()
    {
        return new DescendingPathIterator(pathName);
    }

    public static class DescendingPathIterator implements Iterator<Name>
    {
        private final Name name;
        private int count;

        public DescendingPathIterator(final Name name)
        {
            this.name = Objects.requireNonNull(name);
            this.count = name.size();
        }

        @Override
        public boolean hasNext()
        {
            return count > 0;
        }

        @Override
        public Name next()
        {
            if (count <= 0) {
                throw new NoSuchElementException("Element beyond end of iteration");
            } else {
                return name.getPrefix(count--);
            }
        }
    }
}
