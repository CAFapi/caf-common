# CAF-PARENT

---

This project defines a Maven POM file which specifies dependency management for all child projects. 

Using this POM file as the parent for your project will ensure dependency versions and scopes are kept consistent and compatible with all other CAF components, services and the Worker Framework.

## Usage

To use CAF-PARENT as the parent for your component, add it like this to your pom file:

```
<parent>
  <groupId>com.github.cafapi</groupId>
  <artifactId>caf-parent</artifactId>
  <version>1.2.0-SNAPSHOT</version>
</parent>
```

## Maintainer

The following people are responsible for maintaining this code:

- Adam Rogan (Belfast, UK adam.pau.rogan@hpe.com)
