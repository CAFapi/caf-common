# Util-Jerseycompat

This project contains a compatibility class to allow Jersey 1 libraries to co-exist with Jersey 2 apps, such as Dropwizard.

## Usage

Parent your project on [CAF Parent](https://github.com/conalsmith49/caf-common/tree/develop/caf-parent) and add the dependency:

```
<dependency>
  <groupId>com.github.cafapi.util</groupId>
  <artifactId>util-jerseycompat</artifactId>
</dependency>
```

To use this, ensure the following is performed before starting the Jersey 2 app:

`ServiceFinder.setIteratorProvider(new Jersey2ServiceIteratorProvider());`

## Maintainers

The following people are contacts for developing and maintaining this module:

- Andrew Reid (Belfast, UK, andrew.reid@hpe.com)
