# CAF-API

CAF-API is a sub module of CAF-Common and contains essential interfaces, exceptions, configurations and classes used in creating CAF services and frameworks. These interfaces promote code reuse and modularity across the implementing services. 

## Configurations

- BootstrapConfiguration: A method of providing a basic initial startup configuration.

## Exceptions

- CipherException: Thrown when a Cipher encounters a failure when encrypting or decrypting information.

- CodecException: Thrown when a Codec fails to encode or decode information.

- ConfigurationException: Thrown when there is a problem with the ConfigurationSource module.

- ElectionException: Thrown if there is a problem with the election process.

## Interfaces

- Cipher: Provides a method of encrypting and decrypting string data such as passwords.

- Codec: Specifies methods to serialise data from a Java object to byte format and deserialise data from byte format back into a specified Java class.

- Configuration: Indicates either that the class is a simple Java object that stores configuration, or for fields within a class, indicates this object is an embedded configuration object that can be overridden by a ConfigurationSource.

- ConfigurationSource: A ConfigurationSource is intended to provide an implementation-agnostic method of retrieving application-specific configuration.

- ContainsStringKeys: Used on a field of type Map with String keys to indicate that they keys must contains specific Strings.

- Election: Allows interaction with the election process of an individual service election.

- ElectionCallback: The endpoint that an election process will call to upon election or rejection of this instance from a particular Election.

- ElectionFactory: Factory class for returning Election objects for a specific service election.

- ElectionFactoryProvider: Simple boilerplate for providing an ElectionFactory.

- Encrypted: Marker annotation that specifies an item, typically in a configuration class, that a SecurityProvider can decrypt. The field that possesses this annotation must also have the appropriate getter and setter methods that match Java convention.

- ManagedConfigurationSource: Interface extending HealthReporter, ConfigurationMetricsReporter and ConfigurationSource.

### Reporters

- HealthReporter: Indicates this object can report on its health.

- ConfigurationMetricsReporter: Provides metrics for a ConfigurationSource.

### Providers

- CipherProvider: A simple boilerplate for returning a Cipher implementation.

- ElectionFactoryProvider: A simple boilerplate for returning an ElectionFactory.

## Classes

- HealthResult (final): A result returned from a class that implements HealthReporter, indicating its health status and if necessary, a message detailing further information.

- QuietResource: Utility class for use with try-with-resources, for classes where the close operation may throw an exception, but this is not a useful check. @param <T> the class to close quietly

## Enums

- DecodeMethod: Dictates the decoding method for a Codec, specifying whether the Codec should perform strict decoding or be more lenient which can help in being tolerant to data from a subtly different class version of the object.
  - STRICT: A "strict" Codec should not allow unknown fields. It may also reject inputs with duplicate fields or fields that are null when null is not allowed.
  - LENIENT: A "lenient" Codec may accept but ignore unknown fields, and may also accept duplicate entries within the input data (though how this behaves may be undefined). Unexpected nulls may be replaced with the primitive defaults.

- HealthStatus: The overall, concise status of the health of a service.
  - UNKNOWN: The health could not be determined at this time.
  - HEALTHY: The service is healthy and operating normally.
  - UNHEALTHY: The service is unhealthy or failed in some manner.
