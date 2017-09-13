/*
 * Copyright 2015-2017 EntIT Software LLC, a Micro Focus company.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hpe.caf.codec;

import com.hpe.caf.api.Codec;
import com.hpe.caf.api.CodecException;
import com.hpe.caf.api.DecodeMethod;
import com.hpe.caf.api.FileExtensions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.error.YAMLException;
import org.yaml.snakeyaml.introspector.BeanAccess;
import org.yaml.snakeyaml.representer.Representer;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * Use SnakeYAML to serialise/deserialise data.
 *
 * The strict version does not allow missing properties, whereas the lenient version does.
 */
@FileExtensions({"", "yaml", "yml"})
public class YamlCodec implements Codec
{
    private final Yaml strictYaml;
    private final Yaml lenientYaml;

    public YamlCodec()
    {
        Representer lenient = new Representer();
        lenient.getPropertyUtils().setSkipMissingProperties(true);
        lenient.getPropertyUtils().setBeanAccess(BeanAccess.FIELD);
        Representer strict = new Representer();
        strict.getPropertyUtils().setSkipMissingProperties(false);
        strict.getPropertyUtils().setBeanAccess(BeanAccess.FIELD);
        lenientYaml = new Yaml(lenient);
        strictYaml = new Yaml(strict);
    }

    @Override
    public <T> T deserialise(final byte[] data, final Class<T> clazz, final DecodeMethod method)
        throws CodecException
    {
        try {
            return getYaml(method).loadAs(new ByteArrayInputStream(data), clazz);
        } catch (final YAMLException e) {
            throw new CodecException("Failed to deserialise", e);
        }
    }

    @Override
    public <T> T deserialise(final InputStream stream, final Class<T> clazz, final DecodeMethod method)
        throws CodecException
    {
        try {
            return getYaml(method).loadAs(stream, clazz);
        } catch (final YAMLException e) {
            throw new CodecException("Failed to deserialise", e);
        }
    }

    @Override
    public byte[] serialise(final Object object)
        throws CodecException
    {
        try {
            return getYaml(DecodeMethod.getDefault()).dump(object).getBytes(StandardCharsets.UTF_8);
        } catch (final YAMLException e) {
            throw new CodecException("Failed to serialise", e);
        }
    }

    private Yaml getYaml(final DecodeMethod method)
    {
        return method == DecodeMethod.STRICT ? strictYaml : lenientYaml;
    }
}
