# config-decoder

---

This is an implementation of the `ConfigurationDecoderProvider` interface that selects a `Decoder` to use when interpreting the
configuration files.  The decision is made by looking at the `CAF_CONFIG_DECODER` environment variable.

The `CAF_CONFIG_DECODER` variable should be set to the name of the class implementing the 'Decoder' interface that should be loaded (the name being the short name of the class).

e.g.
Given a class TestDecoder as below;
```
package com.hpe.decoders;

import java.io.InputStream;

public class TestDecoder implements Decoder{
    @Override
    public <T> T deserialise(InputStream stream, Class<T> clazz) throws CodecException {
        return null;
    }
}
```

Which in its project specifies a META-INF/services resource file 'com.opentext.caf.api.Decoder' containing;

```
com.hpe.decoders.TestDecoder
```

The `CAF_CONFIG_DECODER` variable should be set to `TestDecoder` to load that Decoder implementation.
