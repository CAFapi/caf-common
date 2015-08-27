package com.hp.caf.election;


import com.hp.caf.api.Election;
import com.hp.caf.api.ElectionCallback;
import com.hp.caf.api.ElectionFactory;


public class NullElectionFactory extends ElectionFactory
{
    @Override
    public Election getElection(final String serviceReference, final ElectionCallback callback)
    {
        return new NullElection(serviceReference, callback);
    }
}
