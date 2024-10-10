!not-ready-for-release!

#### Version Number
${version-number}

#### New Features
- US914108: Version Currency: JUnit 5 migration
- US969005: Add support for getting secrets  
  - A new `getSecret` method has been added to the `decoder-js` module.
  - A new `util-secret` module has been added containing a utility class for getting secrets.
  - Both these retrieve a secret value from various sources in a prescribed order of precedence:
    - Environment variables (direct value)
    - File content (path specified by environment variable with "_FILE" suffix)
    - System properties (with "CAF." prefix)

#### Known Issues
