package com.hpe.caf.api.autoscale;


import com.hpe.caf.api.HealthReporter;


/**
 * Creates WorkloadAnalyser instances for a specific target and profile.
 * @since 9.0
 */
public interface WorkloadAnalyserFactory extends HealthReporter
{
    /**
     * Instantiate a new WorkloadAnalyser for a specific target and profile.
     * @param scalingTarget the reference to the target used for analysing workloads
     * @param scalingProfile the name of the profile to use for scaling
     * @return a new WorkloadAnalyser instance for the given scaling target and profile
     */
    WorkloadAnalyser getAnalyser(String scalingTarget, String scalingProfile);
}
