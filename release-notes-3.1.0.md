#### Version Number
${version-number}

#### New Features
- US915147: A new default `livenessCheck` method in the `HealthReporter` interface has been added.  
  - This method can optionally be implemented to provide a liveness check. The default implementation returns 
    `HealthResult.RESULT_HEALTHY`.

#### Known Issues
- None
