package com.hpe.caf.api.autoscale;


import java.util.Collection;
import java.util.Collections;
import java.util.Objects;


/**
 * Information about the host for a specific service.
 */
public final class ServiceHost
{
    private final String host;
    private final Collection<Integer> ports;


    public ServiceHost(final String hostName, final Collection<Integer> ports)
    {
        this.host = Objects.requireNonNull(hostName);
        this.ports = Collections.unmodifiableCollection(Objects.requireNonNull(ports));
    }


    /**
     * @return the hostname of the system hosting the service
     */
    public String getHost()
    {
        return host;
    }


    /**
     * @return the ports used by the service on this host
     */
    public Collection<Integer> getPorts()
    {
        return ports;
    }
}
