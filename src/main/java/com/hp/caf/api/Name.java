package com.hp.caf.api;


import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;


/**
 * A class that is similar to java.naming.Name, but is iterable.
 * A Name is a tokenized character String, delimited by forward-slashes.
 */
public class Name implements Iterable<String>
{
    private final List<String> components;
    private final String strRepresentation;


    /**
     * Create a new Name. Leading and trailing forward-slashes are allowed.
     * @param name the String to create the name from
     */
    public Name(final String name)
    {
        String fixedName = name.replaceAll("^/+|/+$", "");
        components = Arrays.asList(fixedName.split(Pattern.quote("/")));
        strRepresentation = String.join("/", components);
    }


    /**
     * @return the number of tokenized components of this Name
     */
    public int size()
    {
        return components.size();
    }


    /**
     * Get the components of the Name at the specified numeric index
     * @param index the index of the token to return, starting from 0
     * @return the requested Name component
     * @throws IllegalArgumentException if you request an index that does not exist
     */
    public String getIndex(final int index)
    {
        if ( index < 0 || index >= components.size() ) {
            throw new IllegalArgumentException("Index out of bounds");
        }
        return components.get(index);
    }


    /**
     * Return a Name that consists of a subsection of the current Name.
     * @param upperIndex the upper index (exclusive) of components to build the Name from
     * @return a new Name, that is a sub-set of the current Name
     * @throws IllegalArgumentException if you request an index that is no in the range of components of this Name
     */
    public Name getPrefix(final int upperIndex)
    {
        if ( upperIndex < 0 || upperIndex > components.size() ) {
            throw new IllegalArgumentException("Index out of bounds");
        }
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < upperIndex; i++) {
            builder.append("/").append(getIndex(i));
        }
        return new Name(builder.toString());
    }


    @Override
    public String toString()
    {
        return strRepresentation;
    }


    @Override
    public Iterator<String> iterator()
    {
        return components.iterator();
    }
}
