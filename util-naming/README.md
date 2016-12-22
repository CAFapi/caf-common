# Util-Naming

This project comprises of classes that are used internally in CAF services to store service paths and names.

## Service Path

Simple object to store the service path, expected to be in the form /group/subgroup/id. Internally this is represented by a javax.naming.Name object. Any leading or terminating forward-slashes are removed upon creation. 

- Valid path: a/b/c
- Valid path: /a/b/c/ (leading and terminating slashes will be removed)
- Invalid path: /a/b//c/ (empty naming groups are not allowed)
- Invalid path: /a (no group)

### Usage

Create in instance using the ServicePath constructor which takes a `String path` i.e. 

`public ServicePath(final String path) throws InvalidNameException`. 

## Name

A class that is similar to java.naming.Name, but is iterable. A Name is a tokenized character String, delimited by forward-slashes.

### Usage

Construct an instance of Name by one of the overloaded constructors:

- `public Name(final String name)`

- `public Name(final List<String> components)`
