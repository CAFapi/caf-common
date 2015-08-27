package com.hp.caf.codec;


import com.hp.caf.api.Codec;
import com.hp.caf.api.CodecException;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.error.YAMLException;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;


/**
 * Use SnakeYAML to serialise/deserialise data.
 */
public class YamlCodec extends Codec
{
    private final Yaml yaml = new Yaml();


    @Override
    public <T> T deserialise(final byte[] data, final Class<T> clazz)
            throws CodecException
    {
        try {
            return yaml.loadAs(new ByteArrayInputStream(data), clazz);
        } catch (YAMLException e) {
            throw new CodecException("Failed to deserialise", e);
        }
    }


    @Override
    public <T> T deserialise(final InputStream stream, final Class<T> clazz)
            throws CodecException
    {
        try {
            return yaml.loadAs(stream, clazz);
        } catch (YAMLException e) {
            throw new CodecException("Failed to deserialise", e);
        }
    }


    @Override
    public byte[] serialise(final Object object)
            throws CodecException
    {
        try {
            return yaml.dump(object).getBytes(StandardCharsets.UTF_8);
        } catch (YAMLException e) {
            throw new CodecException("Failed to serialise", e);
        }
    }
}
