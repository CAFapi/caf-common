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
 - CAF\_RESOURCE\_PATH: the resource directory that contains the config files


## Usage

 This module expects flat configuration files in the specified directory to be
 named in the following format. They will always be prefixed with "cfg", and
 have underscore delimited sections consisting of the groups, subgroups and
 service name of the application's `ServicePath`, then finally the simplified
 class name of the configuration object that is being acquired. This means
 that acquiring the configuration class `TestConfiguration` within an
 application with a `ServicePath` of `group/subgroup/service` would prompt the
 module to look for the file `cfg_group_subgroup_service_TestConfiguration`.

 The Decoder passed to the `ConfigurationSource` may be annotated with `FileExtensions`, which will be appended to the
  constructed filename when looking for configuration files.
  e.g. a Decoder defining FileExtensions as "js" passed in, will cause the module to look for a config file
   using the previously described filename rules and appending ".js". For the prior example the module would now look
   for the file `cfg_group_subgroup_service_TestConfiguration.js`.

 This module fully supports hierarchical and recursive configuration. If the
 file above is not found it will proceed to drop the top level of the
 `ServicePath` (eg. it will open `cfg_group_subgroup_TestConfiguration`) and
 carry on down until a file is returned.


## Failure modes

 The following scenarios will prevent the module from initialising:

 - The bootstrap configuration variable `config.path` is incorrectly set
 - The bootstrap configuration variable `CAF_RESOURCE_PATH` is incorrectly set
 - The specified resource path is not found

 The following scenarios have been identified as possible runtime failure modes
 for this module:

 - Disk read failures including file system corruption
 - Unreasonably sized configuration files

## Maintainers

The following people are responsible for maintaining this code:

- Andy Reid (Belfast, UK, andrew.reid@microfocus.com)
- Dermot Hardy (Belfast, UK, dermot.hardy@microfocus.com)
- Anthony Mcgreevy (Belfast, UK, anthony.mcgreevy@microfocus.com)
- Davide Giorgio Picchione (Belfast, UK, davide-giorgio.picchione@microfocus.com)
- Thilagavathi Santhoshkumar (Belfast, UK, thilagavathi.santhoshkumar@microfocus.com)
- Radoslav Straka (Belfast, UK, radoslav.straka@microfocus.com)
- Michael Bryson (Belfast, UK, michael.bryson@microfocus.com)
- Rahul Kulkarni (Chicago, USA, rahul.kulkarni@microfocus.com)
- Kusuma Ghosh Dastidar (Pleasanton, USA, vgkusuma@microfocus.com)
- Om Mariappan (Pleasanton, USA, omkumar.mariappan@microfocus.com)
- Morvin Shah (Pleasanton, USA, morivn.pan.shah@microfocus.com)
