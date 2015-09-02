package com.hpe.caf.election;


import com.hpe.caf.api.ConfigurationSource;
import com.hpe.caf.api.ElectionFactory;
import com.hpe.caf.api.ElectionFactoryProvider;


public class NullElectionFactoryProvider implements ElectionFactoryProvider
{
    @Override
    public ElectionFactory getElectionManager(final ConfigurationSource configurationSource)
    {
        return new NullElectionFactory();
    }
}
