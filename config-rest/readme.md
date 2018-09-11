# config-rest

---

 This is an implementation of a `ConfigurationSource` that provides
 configuration data from a remote HTTP server.


## Configuration

 The following configuration is *required* be provided via the bootstrap
 configuration (typically environment variables):

 - config.rest.host: the URL endpoint to the REST/HTTP server that is hosting
  the configuration files (eg. http://my-server:8080)


## Usage

 This module will only initialise if the REST/HTTP endpoint is set as per the
 configuration. When requests come in for a particular configuration class, it
 will query the configured endpoint under the following URL:

 ```
 http://endpoint/config/(appName)/(serviceName)/v(serviceVersion)/(configClass)
 ```

 There application name, service name, and service version are populated from
 a `ServiceIdentifier` which originates from the calling application. If you
 have an application of "testApp" with a service called "testService" and
 version 1, then perform `getConfiguration(MyConfiguration.class)`, the
 following URL will be called:

 ```
 http://endpoint/config/testApp/testService/v1/MyConfiguration
 ```

 The module supports exponential backoff and retry in case of network failures.


## Failure modes

 The following scenarios will prevent the module from initialising:

 - The bootstrap configuration variable `config.rest.host` is not set

 The following scenarios have been identified as possible runtime failure modes
 for this module:

 - Non-transient network failures that exceed the retry period
