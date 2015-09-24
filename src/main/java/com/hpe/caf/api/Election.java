package com.hpe.caf.api;


import java.util.Objects;


/**
 * Allows interaction with the election process of an individual service election.
 * @since 5.0
 */
public abstract class Election
{
    private final String electionReference;
    private final ElectionCallback callback;


    public Election(final String reference, final ElectionCallback callback)
    {
        this.electionReference = Objects.requireNonNull(reference);
        this.callback = Objects.requireNonNull(callback);
    }


    /**
     * @return the named reference to this particular election, typically a service name
     */
    public String getElectionReference()
    {
        return electionReference;
    }


    protected ElectionCallback getCallback()
    {
        return callback;
    }


    /**
     * Request to enter the election process of this specific election
     * @throws ElectionException if it is not possible to enter for election
     */
    public abstract void enter()
        throws ElectionException;


    /**
     * Request withdrawal as leader, triggering a re-election process.
     */
    public abstract void withdraw();


    /**
     * Terminate participation in this election process, withdrawing as leader if elected.
     */
    public abstract void resign();
}
