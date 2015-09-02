package com.hpe.caf.election;


import com.hpe.caf.api.Election;
import com.hpe.caf.api.ElectionCallback;
import com.hpe.caf.api.ElectionFactory;


public class NullElectionFactory extends ElectionFactory
{
    @Override
    public Election getElection(final String serviceReference, final ElectionCallback callback)
    {
        return new NullElection(serviceReference, callback);
    }
}
