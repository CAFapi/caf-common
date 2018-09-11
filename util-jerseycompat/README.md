# Util-Jerseycompat

This project contains a compatibility class to allow Jersey 1 libraries to co-exist with Jersey 2 apps, such as Dropwizard.

## Usage

```
<dependency>
  <groupId>com.github.cafapi.util</groupId>
  <artifactId>util-jerseycompat</artifactId>
  <version>xxx</version>
</dependency>
```

To use this, ensure the following is performed before starting the Jersey 2 app:

`ServiceFinder.setIteratorProvider(new Jersey2ServiceIteratorProvider());`
