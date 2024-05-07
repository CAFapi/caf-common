!not-ready-for-release!

#### Version Number
${version-number}

#### New Features
- None

#### Breaking Changes
- US915147: The `healthCheck` method in the`HealthReporter` interface has been removed.  
  Two new methods have been added which replace the `healthcheck` method:
  - `checkAlive` (Returns the result of the liveness health check)
  - `checkReady` (Returns the result of the readiness health check)

#### Known Issues
- None
