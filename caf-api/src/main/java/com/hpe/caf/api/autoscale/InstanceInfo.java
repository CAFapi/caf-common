package com.hpe.caf.api.autoscale;


import java.util.Collection;
import java.util.Collections;
import java.util.Objects;


/**
 * Contains information about the instances of a currently running service.
 * @since 5.0
 */
public final class InstanceInfo
{
    private final int instancesRunning;
    private final int instancesStaging;
    private final Collection<ServiceHost> hosts;


    public InstanceInfo(final int running, final int staging, final Collection<ServiceHost> hosts)
    {
        this.instancesRunning = running;
        this.instancesStaging = staging;
        this.hosts = Collections.unmodifiableCollection(Objects.requireNonNull(hosts));
    }


    /**
     * @return the number of instances of a service that are running and active
     */
    public int getInstancesRunning()
    {
        return instancesRunning;
    }


    /**
     * @return the number of instances of a service that are being prepared to run
     */
    public int getInstancesStaging()
    {
        return instancesStaging;
    }


    /**
     * @return the combination of the number of running instances and those being prepared
     */
    public int getTotalInstances()
    {
        return getInstancesRunning() + getInstancesStaging();
    }


    /**
     * @return the hosts that these instances are running upon
     */
    public Collection<ServiceHost> getHosts()
    {
        return hosts;
    }
}
