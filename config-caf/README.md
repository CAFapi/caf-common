# config-caf

---

 This is a partial implementation of a `ManagedConfigurationSource` that retrieves application-specific configuration. Recursive lookups are supported if the configuration class comprises other objects marked `@Configuration`. Any configuration object marked `@Encrypted` will be decrypted. 


## System Property and Environment Variable Support
 The class also supports the ability to read configuration through a system property or environment variable.

 The configuration syntax expected is similar to that employed in bash, e.g. `${variableName}`. A default value can also be set for unresolved variables by appending it to the variable name after the variable default value delimiter, `:-`. e.g. `${variableName:-defaultVariableValue}`.

### Example
The example configuration below demonstrates the usage of a system property or environment variable, `CAF_STORAGE_SERVER_NAME`, to specify the host name of the storage server:

	"serverName": "${CAF_STORAGE_SERVER_NAME:-a1-dev-mem031.lab.lynx-connected.com}"

If a system property or environment variable with the name `CAF_STORAGE_SERVER_NAME` cannot be resolved during worker start-up, then a default value of `a1-dev-mem031.lab.lynx-connected.com` will be used instead.
