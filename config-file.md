# config-file

---

 This is an implementation of a `ConfigurationSource` that provides
 configuration data from a file on a local disk. The directory in which the
 files reside can be configured.


## Configuration

 The following configuration can *optionally* be provided via the bootstrap
 configuration (typically environment variables):

 - config.path: the absolute directory that contains the config files, if this
  is not specified then it will use the current working directory. If a path is
  specified, then it must be a valid directory


## Usage

 This module expects flat configuration files in the specified directory to be
 named in the following format. They will always be prefixed with "cfg", and
 have underscore delimited sections consisting of the groups, subgroups and
 service name of the application's `ServicePath`, then finally the simplified
 class name of the configuration object that is being acquired. This means
 that acquiring the configuration class `TestConfiguration` within an
 application with a `ServicePath` of `group/subgroup/service` would prompt the
 module to look for the file `cfg_group_subgroup_service_TestConfiguration`.

 This module fully supports hierarchical and recursive configuration. If the
 file above is not found it will proceed to drop the top level of the
 `ServicePath` (eg. it will open `cfg_group_subgroup_TestConfiguration`) and
 carry on down until a file is returned.


## Failure modes

 The following scenarios will prevent the module from initialising:

 - The bootstrap configuration variable `config.path` is incorrectly set

 The following scenarios have been identified as possible runtime failure modes
 for this module:

 - Disk read failures including file system corruption
 - Unreasonably sized configuration files


## Maintainers

 The following people are contacts for developing and maintaining this module:

 - Richard Hickman (Cambridge, UK, richard.hickman@hp.com)
