# config-system

---

 This is an implementation of a `BootstrapConfiguration` that provides
 basic startup configuration from Java system properties and environment
 variables.


## Configuration

 There is no configuration for this module.


## Usage

 All calls to retrieve a value based upon a key will be provided from both the
 Java system properties and environment variables. The standard method that
 should be used by developers/operations is environment variables, but note
 that Java system properties will take precedence if both are set.


## Maintainers

 The following people are contacts for developing and maintaining this module:

 - Richard Hickman (Cambridge, UK, richard.hickman@hp.com)
