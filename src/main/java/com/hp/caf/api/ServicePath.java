package com.hp.caf.api;


import javax.naming.CompositeName;
import javax.naming.InvalidNameException;
import javax.naming.Name;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;


/**
 * Simple object to store the service path, expected to be in the form /group/subgroup/id.
 * Internally this is represented by a javax.naming.Name object. Any leading or terminating
 * forward-slashes are removed upon creation.
 *
 * Valid path: a/b/c
 * Valid path: /a/b/c/ (leading and terminating slashes will be removed)
 * Invalid path: /a/b//c/ (empty naming groups are not allowed)
 * Invalid path: /a (no group)
 */
public final class ServicePath implements Iterable<String>
{
    private final Name pathName;


    /**
     * Create a new ServicePath from a String.
     * @param path the String to create the ServicePath from
     * @throws InvalidNameException if there are invalid characters, empty groups, or less than 2 components
     */
    public ServicePath(final String path)
            throws InvalidNameException
    {
        Objects.requireNonNull(path);
        pathName = new CompositeName(path.replaceAll("^/+|/+$", ""));
        if ( pathName.size() < 2 ) {
            throw new InvalidNameException("At minimum, a service must have a group");
        }
        for ( String n : this ) {
            if ( n.isEmpty() ) {
                throw new InvalidNameException("Empty subgroups are invalid");
            }
        }
    }


    /**
     * Return the root of the service path, ie. the first group the service is a part of.
     * In the service path "/a/b/c", this will be "a".
     * @return the root of the service path
     */
    public String getRoot()
    {
        return pathName.get(0);
    }


    /**
     * Return the leaf or "tip" of the service path, ie. the final part of the service name.
     * In the service path "/a/b/c", this will be "c".
     * @return the leaf of the service path
     */
    public String getLeaf()
    {
        return pathName.get(pathName.size() - 1);
    }


    /**
     * Return the immediate group of the service, ie. the node above the leaf.
     * In the service path "/a/b/c", this will be "b".
     * @return the immediate group of the service
     */
    public String getGroup()
    {
        return pathName.get(pathName.size() - 2);
    }


    /**
     * @return the full service path, as a String
     */
    public String getPath()
    {
        return pathName.toString();
    }


    @Override
    public String toString()
    {
        return getPath();
    }


    /**
     * {@inheritDoc}
     *
     * Each iteration is the next element of the service path.
     */
    @Override
    public Iterator<String> iterator()
    {
        return new NameIterator(pathName);
    }


    /**
     * @return an iterator that only iterates over the groups and subgroups of the service
     */
    public Iterator<String> groupIterator()
    {
        return new NameIterator(pathName.getPrefix(pathName.size() - 1));
    }


    /**
     * @return an Iterator where each element is the entire service path up to the (decrementing) index
     */
    public Iterator<String> descendingPathIterator()
    {
        return new DescendingPathIterator(pathName);
    }


    public static class NameIterator implements Iterator<String>
    {
        private final Name name;
        private int count = 0;


        public NameIterator(final Name name)
        {
            this.name = Objects.requireNonNull(name);
        }


        @Override
        public boolean hasNext()
        {
            return count < name.size();
        }


        @Override
        public String next()
        {
            if ( count >= name.size() ) {
                throw new NoSuchElementException("Element beyond end of iteration");
            } else {
                return name.get(count++);
            }
        }
    }


    public static class DescendingPathIterator implements Iterator<String>
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
        public String next()
        {
            if ( count <= 0 ) {
                throw new NoSuchElementException("Element beyond end of iteration");
            } else {
                return name.getPrefix(count--).toString();
            }
        }
    }
}
