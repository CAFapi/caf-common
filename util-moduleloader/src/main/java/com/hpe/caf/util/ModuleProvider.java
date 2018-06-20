/*
 * Copyright 2015-2018 Micro Focus or one of its affiliates.
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
package com.hpe.caf.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Allows retrieval of a module based on the Interface that it implements and its simple name.
 */
public final class ModuleProvider
{
    private static final ModuleProvider theInstance = new ModuleProvider();

    private final ConcurrentMap<Class, Map<String, Object>> loadedModules;

    private ModuleProvider()
    {
        loadedModules = new ConcurrentHashMap<>();
    }

    public static ModuleProvider getInstance()
    {
        return theInstance;
    }

    /**
     * Retrieves a module by its simple name, that implements a particular type T
     *
     * @param interfaceImplemented The interface that 'moduleType' implements and that the result should be returned as.
     * @param moduleType Represents particular instance of a factory type object e.g. 'ExampleWorkerBuilder'
     * @return An instance of moduleType as the type T passed in.
     * @throws NullPointerException If the module cannot be retrieved with the specified moduleType.
     */
    public <T> T getModule(final Class<T> interfaceImplemented, final String moduleType) throws NullPointerException
    {
        //check for this type in the map
        Map<String, Object> computedValue = loadedModules.computeIfAbsent(interfaceImplemented, ModuleProvider::loadModules);
        Object moduleInstance = computedValue.get(moduleType);
        Objects.requireNonNull(
            moduleInstance, "Unable to find implementation of " + interfaceImplemented.getName() + " with moduleType " + moduleType);
        return (T) moduleInstance;
    }

    private static <T> Map<String, Object> loadModules(final Class<T> interfaceImplemented)
    {
        List<T> modulesList = ModuleLoader.getServices(interfaceImplemented);
        Map<String, Object> modulesMap = new HashMap<>();
        for (final T module : modulesList) {
            modulesMap.put(getShortNameForType(module), module);
        }
        return modulesMap;
    }

    private static String getShortNameForType(final Object type)
    {
        return type.getClass().getSimpleName();
    }
}
