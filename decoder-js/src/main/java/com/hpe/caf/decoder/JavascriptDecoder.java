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
package com.hpe.caf.decoder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hpe.caf.api.CodecException;
import com.hpe.caf.api.Decoder;
import com.hpe.caf.api.FileExtensions;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 * Decoder implementation that supports building objects from JavaScript. Supports a function 'getenv' which takes the
 * name of an environment variable and will return its value.
 */
@FileExtensions("js")
public class JavascriptDecoder implements Decoder
{
    private final ScriptEngineManager scriptManager;
    private final ObjectMapper objectMapper;

    public JavascriptDecoder()
    {
        scriptManager = new ScriptEngineManager();
        objectMapper = new ObjectMapper();
    }

    @Override
    public <T> T deserialise(final InputStream stream, final Class<T> clazz) throws CodecException
    {
        try {
            return decode(stream, clazz);
        } catch (final IOException | ScriptException | NoSuchMethodException ex) {
            throw new CodecException("Failed to decode JavaScript", ex);
        }
    }

    private <T> T decode(final InputStream stream, final Class<T> clazz) throws IOException, ScriptException, NoSuchMethodException
    {
        // I'm unsure about whether it would be safe to share the scripting engine across threads so I'm creating a separate one each time
        // this method is called.  It might be safe, assuming we just created a fresh Binding for each thread, but I'm finding it hard to
        // see a definitive answer on it in the reference.
        final ScriptEngine jsEngine = scriptManager.getEngineByName("nashorn");

        // Define a short-cut for accessing environment variables
        // Return the JSON.stringify method so that we can call it later
        final Object fnObj = jsEngine.eval(""
            + "getenv = com.hpe.caf.decoder.PropertyRetriever.getenv;"
            + ""
            + "({"
            + "    toJson: JSON.stringify"
            + "});");

        // Evaluate the supplied file which is expected to return an object
        final Object configObj = evaluateScript(jsEngine, stream);

        // Convert the object to a JSON string
        final String configJson = invokeStringMethod(jsEngine, fnObj, "toJson", configObj);

        // Deserialise the JSON string into the configuration object
        return objectMapper.readValue(configJson, clazz);
    }

    /**
     * Executes the specified script. The source of the script is supplied as a UTF-8 encoded {@code InputStream}. The default
     * {@code ScriptContext} for the {@code ScriptEngine} is used.
     *
     * @param scriptEngine the scripting engine
     * @param stream the source of the script, encoded in UTF-8
     * @return the value returned by the script
     * @throws IOException if the {@code InputStream} cannot be read
     * @throws ScriptException if an error occurs in the script
     */
    private static Object evaluateScript(final ScriptEngine scriptEngine, final InputStream stream) throws IOException, ScriptException
    {
        try (final InputStreamReader reader = new InputStreamReader(stream, StandardCharsets.UTF_8)) {
            return scriptEngine.eval(reader);
        }
    }

    /**
     * Calls a method on a script object compiled during a previous script execution, which is retained in the state of the ScriptEngine.
     *
     * @param jsEngine the JavaScript engine
     * @param thiz the instance of the class that contains the method to be called
     * @param name the name of the method to be called
     * @param args the arguments to pass to the method
     * @return the value returned by the procedure, which must be a String
     * @throws NoSuchMethodException if method with given name or matching argument types cannot be found.
     * @throws ScriptException if an error occurs during invocation of the method.
     */
    private static String invokeStringMethod(final ScriptEngine jsEngine, final Object thiz, final String name, final Object... args)
        throws NoSuchMethodException, ScriptException
    {
        return (String) ((Invocable) jsEngine).invokeMethod(thiz, name, args);
    }
}
