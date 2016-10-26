package com.hpe.caf.api.autoscale;


import java.util.Objects;


/**
 * A representation of a scaling request to be handled by the autoscaler.
 * @since 5.0
 */
public final class ScalingAction
{
    private final ScalingOperation operation;
    private final int amount;
    /**
     * Perform no scaling.
     */
    public static final ScalingAction NO_ACTION = new ScalingAction(ScalingOperation.NONE, 0);
    /**
     * Scale up by one instance.
     */
    public static final ScalingAction SCALE_UP = new ScalingAction(ScalingOperation.SCALE_UP, 1);
    /**
     * Scale down by one instance.
     */
    public static final ScalingAction SCALE_DOWN = new ScalingAction(ScalingOperation.SCALE_DOWN, 1);


    public ScalingAction(final ScalingOperation scalingOperation, final int amount)
    {
        this.operation = Objects.requireNonNull(scalingOperation);
        this.amount = amount;
    }


    /**
     * @return the operation requested
     */
    public ScalingOperation getOperation()
    {
        return operation;
    }


    /**
     * @return the amount of instances requested to scale up or down by
     */
    public int getAmount()
    {
        return amount;
    }
}
