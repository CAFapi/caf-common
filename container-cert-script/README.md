# Container Cert Script

The container cert script contains CAcert installation scripts for installing Java security certificates.

It also contains an install script for installing SSL keystore.

## Usage

To install the scripts in a container, add the following dependency to your pom:

```
<dependency>
  <groupId>com.github.cafapi</groupId>
  <artifactId>container-cert-script</artifactId>
  <type>tar.gz</type>
</dependency>
```

Then unpack the container-cert-script in a dependencySet like this:

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
