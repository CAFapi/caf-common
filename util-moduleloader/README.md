# Util-Moduleloader

This project contains a utility class called `ModuleLoader` that provides methods for finding and returning components at runtime using the Java ServiceLoader. If there is no advertised implementation or no default implementation is specified then a `ModuleLoaderException` is thrown.

The `ModuleProvider` class allows retrieval of a module based on the interface that it implements and its simple name.

The advantages of using `util-moduleloader` over a direct call to the Java ServiceLoader are:

- Provides a means of retrieving services which is consistent across CAF services and components.
- Encapsulates all calls to Java ServiceLoader hiding the ServiceLoader details.
- Logs information which is standard across all CAF services which use `util-moduleloader`.
- Validates the returned service implementations.
- Provides its own exception and handles ServiceLoader exceptions.

## Usage

```
<dependency>
  <groupId>com.github.cafapi.util</groupId>
  <artifactId>util-moduleloader</artifactId>
  <version>xxx</version>
</dependency>
```

To use `ModuleLoader` call one of the overloaded static `getService(...)` methods passing in the interface of which to find a list of advertised service implementations.

To use `ModuleProvider`, create an instance using `ModuleProvider.getInstance();`. Then call `getModule(Class<T> interfaceImplemented, String moduleType)`.
