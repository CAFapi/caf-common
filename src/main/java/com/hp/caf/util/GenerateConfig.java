package com.hp.caf.util;


import com.hp.caf.api.Codec;
import com.hp.caf.api.CodecException;

import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;


/**
 * Utility app for generating configuration files. It can generate either json or yaml format
 * configuration files from a basic Java object on the classpath, and will output to stdout.
 *
 * Example usage:
 * java -cp "*" com.hp.caf.util.GenerateConfig com.hp.caf.worker.queue.RabbitWorkerQueueConfiguration
 */
public final class GenerateConfig
{
    private GenerateConfig() { }


    public static void main(final String[] args)
        throws ClassNotFoundException, NoSuchMethodException, CodecException, InstantiationException, IllegalAccessException, InvocationTargetException, ComponentLoaderException
    {
        if ( args.length < 1 ) {
            System.err.println("Usage: java -cp * com.hp.caf.util.GenerateConfig configClassName");
        } else {
            String className = args[0];
            try {
                Class clazz = Class.forName(className);
                Codec codec = ComponentLoader.getService(Codec.class);
                System.out.println(new String(codec.serialise(clazz.getConstructor().newInstance()), StandardCharsets.UTF_8));
            } catch (ClassNotFoundException e) {
                System.err.println("Class not found on classpath: " + className);
                throw e;
            } catch (InstantiationException | InvocationTargetException | NoSuchMethodException | IllegalAccessException | CodecException e) {
                System.err.println("Failed to generate configuration");
                throw e;
            } catch (ComponentLoaderException e) {
                System.err.println("Could not load codec component");
                throw e;
            }
        }
    }
}
