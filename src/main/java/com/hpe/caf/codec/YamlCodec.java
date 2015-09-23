package com.hpe.caf.codec;


import com.hpe.caf.api.Codec;
import com.hpe.caf.api.CodecException;
import com.hpe.caf.api.DecodeMethod;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.error.YAMLException;
import org.yaml.snakeyaml.representer.Representer;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;


/**
 * Use SnakeYAML to serialise/deserialise data.
 *
 * The strict version does not allow missing properties, whereas the lenient version does.
 */
public class YamlCodec extends Codec
{
    private final Yaml strictYaml;
    private final Yaml lenientYaml;


    public YamlCodec()
    {
        Representer lenient = new Representer();
        lenient.getPropertyUtils().setSkipMissingProperties(true);
        Representer strict = new Representer();
        strict.getPropertyUtils().setSkipMissingProperties(false);
        lenientYaml = new Yaml(lenient);
        strictYaml = new Yaml(strict);
    }


    @Override
    public <T> T deserialise(final byte[] data, final Class<T> clazz, final DecodeMethod method)
            throws CodecException
    {
        try {
            return getYaml(method).loadAs(new ByteArrayInputStream(data), clazz);
        } catch (YAMLException e) {
            throw new CodecException("Failed to deserialise", e);
        }
    }


    @Override
    public <T> T deserialise(final InputStream stream, final Class<T> clazz, final DecodeMethod method)
            throws CodecException
    {
        try {
            return getYaml(method).loadAs(stream, clazz);
        } catch (YAMLException e) {
            throw new CodecException("Failed to deserialise", e);
        }
    }


    @Override
    public byte[] serialise(final Object object)
            throws CodecException
    {
        try {
            return getYaml(DecodeMethod.getDefault()).dump(object).getBytes(StandardCharsets.UTF_8);
        } catch (YAMLException e) {
            throw new CodecException("Failed to serialise", e);
        }
    }


    private Yaml getYaml(final DecodeMethod method)
    {
        return method == DecodeMethod.STRICT ? strictYaml : lenientYaml;
    }
}
