!not-ready-for-release!

#### Version Number
${version-number}

#### New Features
- US914108: Version Currency: JUnit 5 migration
- US969005: Add support for getting secrets  
  - A new `SecretRetriever` class has been added to the `decoder-js` module containing a `getSecret(String key)` method for getting secrets.
  - A new `util-secret` module has been added containing a `SecretUtil` class for getting secrets.
  - Both these retrieve a secret value from configurable sources:
    - Environment variables (direct value) - enabled via `CAF_ENABLE_ENV_SECRETS` (defaults to `true`)
    - File content (path specified by environment variable with `_FILE` suffix) - enabled via `CAF_ENABLE_FILE_SECRETS` (defaults to `false`)

#### Known Issues
