package com.hpe.caf.api;


/**
 * Factory class for returning Election objects for a specific service election.
 */
public abstract class ElectionFactory
{
    /**
     * Request a handle class used to interact with a particular service election.
     * @param serviceReference a unique handle for an election, typically a service name
     * @param callback the object that will be called upon the instance being elected/unelected
     * @return an object to interact with for a specific election
     */
    public abstract Election getElection(final String serviceReference, final ElectionCallback callback);
}
