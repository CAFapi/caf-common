# config-decoder

---

This is an implementation of the `ConfigurationDecoderProvider` interface that selects a `Decoder` to use when interpreting the
configuration files.  The decision is made by looking at the `CAF_CONFIG_DECODER` environment variable.
