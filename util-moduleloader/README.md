# Util-Moduleloader

This project contains a utility class called `ModuleLoader` that provides methods for finding and returning components at runtime using the Java ServiceLoader. If there is no advertised implementation or no default implementation is specified then a `ModuleLoaderException` is thrown.

The `ModuleProvider` class allows retrieval of a module based on the interface that it implements and its simple name.

## Usage

To use `ModuleLoader` call one of the overloaded static `getService(...)` methods passing in the interface to find an advertised service implementation for. To get all the advertised service implementations call `getServices(final class<T> intf)`.

To use `ModuleProvider`, create an instance using `ModuleProvider.getInstance();`. Then call `getModule(Class<T> interfaceImplemented, String moduleType)`.
