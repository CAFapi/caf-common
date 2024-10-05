!not-ready-for-release!

#### Version Number
${version-number}

#### New Features
- US915147: A new default `livenessCheck` method in the `HealthReporter` interface has been added.  
  - This method can optionally be implemented to provide a liveness check. The default implementation returns 
    `HealthResult.RESULT_HEALTHY`.

- US969005: A new `getenvfile` method has been added to the `PropertyRetriever` class in the `decoder-js` module.
  - This method reads the contents of a file pointed to by the supplied environment variable.

#### Known Issues
- None
