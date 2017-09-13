# Util-Naming

This project comprises of classes that are used internally in [CAF](http://cafapi.github.io/) services to store service paths and names.

## ServicePath

Simple object to store the service path, expected to be in the form /group/subgroup/id. Internally this is represented by a javax.naming.Name object. Any leading or terminating forward-slashes are removed upon creation. 

- Valid path: a/b/c
- Valid path: /a/b/c/ (leading and terminating slashes will be removed)
- Invalid path: /a/b//c/ (empty naming groups are not allowed)
- Invalid path: /a (no group)

### Usage

Create an instance using the `ServicePath` constructor which takes a `String path`: 

`public ServicePath(final String path) throws InvalidNameException`. 

## Name

A class that is similar to `javax.naming.Name`, but is iterable. A `Name` is a tokenized character String, delimited by forward-slashes.

### Usage

Constructed and used in the `ServicePath` constructor. To construct an intance of `ServicePath` see above.

`ServicePath` provides getters for various components of the `Name`. Example usage is below:

```
// Construct a new service path.
ServicePath servicePath = new ServicePath("a/b/c");

// Get the root of the service path e.g. "a".
String root = servicePath.getRoot();

// Get the leaf (or tip) of the service path i.e. the final part of the service name e.g. "c".
String leaf = servicePath.getLeaf();

// Get the immediate group of the service, the node above the leaf e.g. "b".
String group = servicePath.getGroup();
```



## Maintainers

The following people are responsible for maintaining this code:

- Adam Rogan (Belfast, UK adam.pau.rogan@microfocus.com)
