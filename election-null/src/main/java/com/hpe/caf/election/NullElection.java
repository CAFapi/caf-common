package com.hpe.caf.election;


import com.hpe.caf.api.Election;
import com.hpe.caf.api.ElectionCallback;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;


public class NullElection implements Election
{
    private final AtomicBoolean elected = new AtomicBoolean(false);
    private final ElectionCallback callback;


    public NullElection(final ElectionCallback callback)
    {
        this.callback = Objects.requireNonNull(callback);
    }


    @Override
    public void enter()
    {
        if ( elected.compareAndSet(false, true) ) {
            callback.elected();
        }
    }


    @Override
    public void withdraw()
    {
        if ( elected.compareAndSet(true, false) ) {
            callback.rejected();
        }
    }


    @Override
    public void resign()
    {
        if ( elected.compareAndSet(true, false) ) {
            callback.rejected();
        }
    }
}
