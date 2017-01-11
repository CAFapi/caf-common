# Util-Ref

This project provides classes and utility wrappers for interactions with data storage and are used throughout [CAF](http://cafapi.github.io/) component and service development.

## DataSource

Interface for defining how to retrieve objects or streams from a reference.

## DataSourceException

General exception for DataSource acquisition failures.

## ReferencedData

Utility wrapper for allowing data to potentially be within a message or located on a remote DataStore. The `acquire(ObjectSource)` method allows transparent method of obtaining an InputStream to the data.

This is primarily intended for use with large amounts of binary data that needs to be processed without storing it all in memory.

### Usage

To instantiate a ReferencedData object, call one of the methods:

- `public static ReferencedData getReferencedData(final String ref)`

- `public static ReferencedData getWrappedData(final byte[] data)`

- `public static ReferencedData getWrappedData(final String ref, final byte[] data)`

The `acquire(...)` method is used to return the referenced data as a stream, and can potentially perform a remote lookup if the object has only member value `reference`.

## ReferencedObject

Utility wrapper for allowing a serialized object to potentially be within a message or located on a remote DataStore. The acquire(ObjectSource) method allows transparent method of obtaining the wrapped data, which will only be retrieved the first time acquire is called (if it is not already present).

It should be noted that because this class returns a fully formed Java object instance, the entire instance must be loaded into memory. As such, this wrapper should only be used for objects that are at most a few megabytes of data.

### Usage

```
<dependency>
  <groupId>com.github.cafapi.util</groupId>
  <artifactId>util-ref</artifactId>
  <version>xxx</version>
</dependency>
```

To instantiate a ReferencedObject object, call one of the methods:

- `public static <T> ReferencedObject<T> getReferencedObject(final Class<T> clazz, final String ref)`

- `public static <T> ReferencedObject<T> getWrappedObject(final Class<T> clazz, final T obj)`

To acquire the data potentially from a lookup to the data storage call:

- `public synchronized T acquire(final DataSource source) throws DataSourceException`

## SourceNotFoundException

Thrown when the source reference is not found.

## Maintainer

The following people are responsible for maintaining this code:

- Christopher Comac (Belfast, UK, christopher.jam.comac@hpe.com)
