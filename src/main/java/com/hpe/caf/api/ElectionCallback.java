package com.hpe.caf.api;


/**
 * The endpoint that an election process will call to upon election or rejection of this
 * instance from a particular Election.
 * @since 5.0
 */
public interface ElectionCallback
{
    /**
     * Indicates this instance has been elected.
     */
    void elected();


    /**
     * Indicates this instance has been unelected.
     */
    void rejected();
}
