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
