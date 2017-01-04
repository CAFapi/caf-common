# Container Cert Script

The container cert script contains CAcert installation scripts for installing Java security certificates primarily for use in [CAF](http://cafapi.github.io/) service development and installation of the certificates in Docker containers.

It also contains an install script for installing SSL keystore.

## Usage

To install the scripts in a container, add the following dependency to your container's pom:

```
<dependency>
  <groupId>com.github.cafapi</groupId>
  <artifactId>container-cert-script</artifactId>
  <type>tar.gz</type>
</dependency>
```

In your docker container's configuration, unpack the container-cert-script in a dependencySet like this:

```
<dependencySet>
  <useProjectArtifact>false</useProjectArtifact>
  <scope>runtime</scope>
  <excludes>
      <exclude>com.github.cafapi:container-cert-script</exclude>
  </excludes>
</dependencySet>
<dependencySet>
  <useProjectArtifact>false</useProjectArtifact>
  <useTransitiveFiltering>true</useTransitiveFiltering>
  <unpack>true</unpack>
  <includes>
      <include>com.github.cafapi:container-cert-script</include>
  </includes>
</dependencySet>
```

# Maintainers

The following people are responsible for maintaining this project:

- Zara McKeown (Belfast, UK, zara.mckeown@hpe.com)
