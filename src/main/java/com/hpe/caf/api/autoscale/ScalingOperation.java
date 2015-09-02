package com.hpe.caf.api.autoscale;


/**
 * The type of recommendations a WorkloadAnalyser can make.
 */
public enum ScalingOperation
{
    /**
     * No scaling operation needs to be performed.
     */
    NONE,
    /**
     * The algorithm indicates the scaler should scale to more instances of the service.
     */
    SCALE_UP,
    /**
     * The algorithm indicates the scale should scale down the number of instances of the service.
     */
    SCALE_DOWN;
}
