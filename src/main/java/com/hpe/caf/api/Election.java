package com.hpe.caf.api;


/**
 * Allows interaction with the election process of an individual service election.
 * @since 9.0
 */
public interface Election
{
    /**
     * Request to enter the election process of this specific election
     * @throws ElectionException if it is not possible to enter for election
     */
    void enter()
        throws ElectionException;


    /**
     * Request withdrawal as leader, triggering a re-election process.
     */
    void withdraw();


    /**
     * Terminate participation in this election process, withdrawing as leader if elected.
     */
    void resign();
}
