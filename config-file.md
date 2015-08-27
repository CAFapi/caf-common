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

 This module is very simple. Simply put your serialised configuration data into
 files (ending in `.conf`). The root name of the file should match the class
 name of the configuration object that is required. This means that if you
 perform the operation `getConfiguration(MyConfiguration.class)` then there
 should be a file called `MyConfiguration.conf`.


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
