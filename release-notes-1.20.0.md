
#### Version Number
${version-number}

#### New Features
 - Resource configuration file support  
    A facility has been added so that configuration files can be supplied as resources.  Previously the file-based configuration module only supported reading from the standard filesystem.
 - Javascript config parser ported to GraalVM JS  
    The configuration parser has been moved from Nashorn to the GraalVM JS engine. 
   This allows ECMAScript 2020 language features to be used.

#### Known Issues
 - None
