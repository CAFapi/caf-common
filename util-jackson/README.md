# util-jackson

This module provides utility functions to aid migration from Jackson 2 (`com.fasterxml.jackson`) to Jackson 3 (`tools.jackson`) within CAF components and services.

## JacksonMigrationFunctions

A utility class with helper methods to convert between Jackson 2 and Jackson 3 types.

### Usage

```xml
<dependency>
    <groupId>com.github.cafapi.common</groupId>
    <artifactId>util-jackson</artifactId>
    <version>xxx</version>
</dependency>
```

#### `toJackson3(com.fasterxml.jackson.databind.JsonNode jsonNode)`

Converts a Jackson 2 `JsonNode` into a Jackson 3 `JsonNode` by serialising the node to its JSON string representation and re-parsing it with the Jackson 3 `ObjectMapper`.

```java
import com.github.cafapi.common.util.jackson.JacksonMigrationFunctions;

tools.jackson.databind.JsonNode jackson3Node = JacksonMigrationFunctions.toJackson3(jackson2Node);
```