# cipher-jasypt

---

 This is an implementation of a `Cipher` that uses the Jasypt library to
 provide password-based encryption using MD5 and DES.


## Configuration

 The following configuration is *required* be provided via the bootstrap
 configuration (typically environment variables):

 - cipher.pass: the key to decrypt or encrypt data with


## Usage

 There are no special usage notes for this module. It should be noted that the
 type of encryption used in this module is acceptable with export laws.


## Failure modes

 The following scenarios will prevent the module from initialising:

 - The `cipher.pass` configuration parameter was not specified


## Maintainers

 The following people are contacts for developing and maintaining this module:

 - Richard Hickman (Cambridge, UK, richard.hickman@hp.com)
