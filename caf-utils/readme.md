# util-tools

---

 This subproject contains various standalone applications that support the
 Common Application Framework or aid developers while using it.


## The EncryptData tool

 This tool is for passing text through a `Cipher` and producing the output,
 which can then be put in configuration files where parameters have been
 annotated with `@Encrypted`. Typically, a lot of symmetric ciphers need a
 key or password set to perform the encryption. This is generally expected to
 be found as a Java system property or environment variable. See the
 documentation on your specific `Cipher` implementation for details. The
 encryption result will be output to `stdout`.

### Usage

 You must have `util-tools`, and a `Cipher` implementation on the available
 classpath. The syntax for using the utility is as follows:

```
 java -cp "*" com.hpe.caf.util.EncryptData data
```

## The GenerateConfig tool

 This tool is for serialising simple Java objects using an available `Codec`
 generally to assist with creating configuration files. The serialised data
 will be output to `stdout`.

### Usage

 You must have `util-tools`, and a `Codec` implementation on the available
 classpath, along with the class you are trying to serialise. The syntax for
 using the utility is as follows:

```
 java -cp "*" com.hpe.caf.util.GenerateConfig fully.qualified.class.name
```

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
