package com.hpe.caf.election;


import com.hpe.caf.api.Election;
import com.hpe.caf.api.ElectionCallback;

import java.util.concurrent.atomic.AtomicBoolean;


public class NullElection extends Election
{
    private final AtomicBoolean elected = new AtomicBoolean(false);


    public NullElection(final String reference, final ElectionCallback callback)
    {
        super(reference, callback);
    }


    @Override
    public void enter()
    {
        if ( elected.compareAndSet(false, true) ) {
            getCallback().elected();
        }
    }


    @Override
    public void withdraw()
    {
        if ( elected.compareAndSet(true, false) ) {
            getCallback().rejected();
        }
    }


    @Override
    public void resign()
    {
        if ( elected.compareAndSet(true, false) ) {
            getCallback().rejected();
        }
    }
}
