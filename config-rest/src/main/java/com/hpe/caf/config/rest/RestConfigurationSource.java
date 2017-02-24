/*
 * Copyright 2015-2017 Hewlett Packard Enterprise Development LP.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hpe.caf.config.rest;


import com.hpe.caf.api.BootstrapConfiguration;
import com.hpe.caf.api.CafConfigurationSource;
import com.hpe.caf.api.Cipher;
import com.hpe.caf.api.ConfigurationException;
import com.hpe.caf.api.Decoder;
import com.hpe.caf.api.HealthResult;
import com.hpe.caf.api.HealthStatus;
import com.hpe.caf.naming.Name;
import com.hpe.caf.naming.ServicePath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit.ErrorHandler;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.Path;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;


/**
 * Retrieves JSON data from an HTTP REST source.
 * It expects the configuration on the remote server to be relative to the REST endpoint in the format /config/servicePath/configName,
 * that is to say, if you were requesting the configuration for the backend class TestWorker, with service path group/subgroup/name
 * on the endpoint localhost:8080, it would request the JSON from http://localhost:8080/config/group/subgroup/name/TestWorker
 */
public class RestConfigurationSource extends CafConfigurationSource
{
    public static final String CONFIG_REST_HOST = "CAF_CONFIG_HOST";
    @Deprecated
    public static final String OLD_CONFIG_REST_HOST = "config.rest.host";
    private URL httpServer;
    private RemoteRestConfiguration remote;
    private int retries = 5;
    private static final Logger LOG = LoggerFactory.getLogger(RestConfigurationSource.class);


    /**
     * {@inheritDoc}
     * This ConfigurationProvider requires config.rest.host to be available from the bootstrap configuration.
     */
    public RestConfigurationSource(final BootstrapConfiguration bootstrap, final Cipher cipher, final ServicePath servicePath,
            final Decoder decoder)
            throws ConfigurationException
    {
        super(bootstrap, cipher, servicePath, decoder);
        try {
            httpServer = new URL(getConfigHost(bootstrap));
            LOG.debug("REST host is {}", httpServer);
        } catch (MalformedURLException e) {
            throw new ConfigurationException("Invalid endpoint URL", e);
        }
        RestAdapter adapter =
                new RestAdapter.Builder().setEndpoint(httpServer.toString()).setErrorHandler(new RemoteRestConfigurationErrorHandler()).build();
        remote = adapter.create(RemoteRestConfiguration.class);
        LOG.debug("Initialised");
    }


    @Override
    public void shutdown()
    {
        // nothing to do
    }


    @Override
    /**
     * {@inheritDoc}
     *
     * This health check attempts to open a socket to the REST host and port and tries a simple connection.
     */
    public HealthResult healthCheck()
    {
        try ( Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(httpServer.getHost(), httpServer.getPort()), 5000);
            return HealthResult.RESULT_HEALTHY;
        } catch (IOException e) {
            LOG.warn("Connection failure to HTTP endpoint", e);
            return new HealthResult(HealthStatus.UNHEALTHY, "Cannot connect to REST endpoint: " + httpServer);
        }
    }


    public void setRetries(final int retries)
    {
        this.retries = Math.max(0,retries);
    }


    /**
     * {@inheritDoc}
     *
     * Descend back through the full service path to try and acquire configuration.
     * This means we will try and get a more specific configuration first before going
     * down to a more general configuration.
     */
    @Override
    protected InputStream getConfigurationStream(final Class configClass, final Name relativePath)
            throws ConfigurationException
    {
        try {
            return remoteCall(relativePath.toString(), configClass.getSimpleName()).getBody().in();
        } catch (HttpConfigurationException e) {
            throw new ConfigurationException("No configuration at path: " + relativePath, e);
        } catch (IOException | InterruptedException e) {
            throw new ConfigurationException("Failed to retrieve configuration", e);
        }
    }


    /**
     * Perform exponential backoff/retry of acquiring a data file from an HTTP source.
     * @param configName the configuration file name
     * @return the HTTP response
     * @throws ConfigurationException if the data cannot be acquired
     * @throws InterruptedException usually if the application is shutting down
     */
    private Response remoteCall(final String path, final String configName)
            throws ConfigurationException, InterruptedException
    {
        int i = 0;
        while ( true ) {
            try {
                return remote.getRemoteConfiguration(path, configName);
            } catch ( HttpConfigurationException nfe ) {
                // don't retry if we have an explicit failure from the HTTP server
                throw nfe;
            } catch (ConfigurationException e) {
                LOG.debug("HTTP client call failed, retrying");
                if ( i == retries ) {
                    throw e;
                } else {
                    Thread.sleep((long)Math.pow(2,i) * 1000L);
                }
                i++;
            }
        }
    }


    private String getConfigHost(final BootstrapConfiguration bootstrap)
        throws ConfigurationException
    {
        String ret;
        if ( bootstrap.isConfigurationPresent(CONFIG_REST_HOST) ) {
            ret = bootstrap.getConfiguration(CONFIG_REST_HOST);
        } else if ( bootstrap.isConfigurationPresent(OLD_CONFIG_REST_HOST) ) {
            ret = bootstrap.getConfiguration(OLD_CONFIG_REST_HOST);
        } else {
            throw new ConfigurationException("Configuration parameter CONFIG_REST_HOST not present");
        }
        return ret;
    }


    public interface RemoteRestConfiguration
    {
        @GET("/config/{servicePath}/{configResource}")
        Response getRemoteConfiguration(@Path(value="servicePath", encode=false) final String service, @Path("configResource") final String config)
            throws ConfigurationException;
    }


    private static class RemoteRestConfigurationErrorHandler implements ErrorHandler
    {
        @Override
        public Throwable handleError(final RetrofitError retrofitError)
        {
            if ( retrofitError.getKind() == RetrofitError.Kind.HTTP ) {
                return new HttpConfigurationException("HTTP source cannot provide configuration: " + retrofitError.getResponse().getStatus() );
            } else {
                return new ConfigurationException("Could not retrieve configuration", retrofitError);
            }
        }
    }


    private static class HttpConfigurationException extends ConfigurationException
    {
        public HttpConfigurationException(final String message)
        {
            super(message);
        }
    }
}
