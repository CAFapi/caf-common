package com.hp.caf.election;


import com.hp.caf.api.ConfigurationSource;
import com.hp.caf.api.ElectionFactory;
import com.hp.caf.api.ElectionFactoryProvider;


public class NullElectionFactoryProvider implements ElectionFactoryProvider
{
    @Override
    public ElectionFactory getElectionManager(final ConfigurationSource configurationSource)
    {
        return new NullElectionFactory();
    }
}
