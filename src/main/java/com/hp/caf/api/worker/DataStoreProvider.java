package com.hp.caf.api.worker;


import com.hp.caf.api.ConfigurationSource;


/**
 * Simple boilerplate to return a DataStore implementation.
 */
public interface DataStoreProvider
{
    DataStore getDataStore(final ConfigurationSource configurationSource)
        throws DataStoreException;
}
