# Util-Jerseycompat

This project contains a compatibility class to allow Jersey 1 libraries to co-exist with Jersey 2 apps, such as Dropwizard.

## Usage

```
<dependency>
  <groupId>com.github.cafapi.common.util</groupId>
  <artifactId>util-jerseycompat</artifactId>
  <version>xxx</version>
</dependency>
```

To use this, ensure the following is performed before starting the Jersey 2 app:

`ServiceFinder.setIteratorProvider(new Jersey2ServiceIteratorProvider());`

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
