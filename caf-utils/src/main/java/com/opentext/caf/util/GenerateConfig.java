/*
 * Copyright 2015-2024 Open Text.
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
package com.opentext.caf.util;

import com.opentext.caf.api.Codec;
import com.opentext.caf.api.CodecException;

import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;

/**
 * Utility app for generating configuration files. It will use whichever implementation of Codec is present on the classpath, and will
 * output the result to stdout.
 *
 * Example usage: java -cp "*" com.hpe.caf.util.GenerateConfig com.hp.caf.worker.queue.RabbitWorkerQueueConfiguration
 */
public final class GenerateConfig
{
    private GenerateConfig()
    {
    }

    public static void main(final String[] args)
        throws ClassNotFoundException, NoSuchMethodException, CodecException, InstantiationException, IllegalAccessException,
               InvocationTargetException, ModuleLoaderException
    {
        if (args.length < 1) {
            System.err.println("Usage: java -cp * com.hpe.caf.util.GenerateConfig configClassName");
        } else {
            String className = args[0];
            try {
                Class clazz = Class.forName(className);
                Codec codec = ModuleLoader.getService(Codec.class);
                System.out.println(new String(codec.serialise(clazz.getConstructor().newInstance()), StandardCharsets.UTF_8));
            } catch (final ClassNotFoundException e) {
                System.err.println("Class not found on classpath: " + className);
                throw e;
            } catch (final InstantiationException | InvocationTargetException |
                           NoSuchMethodException | IllegalAccessException | CodecException e) {
                System.err.println("Failed to generate configuration");
                throw e;
            } catch (final ModuleLoaderException e) {
                System.err.println("Could not load codec component");
                throw e;
            }
        }
    }
}
