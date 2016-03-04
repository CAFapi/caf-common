package com.hpe.caf.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by Michael.McAlynn on 03/03/2016.
 */
public final class ModuleProvider {
    private static final ModuleProvider theInstance = new ModuleProvider();

    private final ConcurrentMap<Class, Map<String, Object>> loadedModules;

    private ModuleProvider(){
        loadedModules = new ConcurrentHashMap<>();
    }

    public static ModuleProvider getInstance(){
        return theInstance;
    }

    /**
     * Retrieves a module by its simple name, that implements a particular type T
     *
     * @param interfaceImplemented The interface that 'moduleType' implements and that the result should be returned as.
     * @param moduleType Represents particular instance of a factory type object e.g. 'ExampleWorkerBuilder'
     * @return An instance of moduleType as the type T passed in.
     */
    public <T> T getModule(Class<T> interfaceImplemented, String moduleType) {


        //check for this type in the map
        Map<String, Object> computedValue = loadedModules.computeIfAbsent(interfaceImplemented, ModuleProvider::loadModules);
        Object moduleInstance = computedValue.get(moduleType);
        Objects.requireNonNull(moduleInstance, "Unable to find implementation of "+ interfaceImplemented.getName() + " with moduleType "+moduleType);
        return (T) moduleInstance;
    }

    private static <T> Map<String, Object> loadModules(Class<T> interfaceImplemented){
        List<T> modulesList = ModuleLoader.getServices(interfaceImplemented);
        Map<String, Object> modulesMap = new HashMap<>();
        for(T module : modulesList){
             modulesMap.put(getShortNameForType(module), module);
        }
        return modulesMap;
    }

    private static String getShortNameForType(Object type){
        return type.getClass().getSimpleName();
    }
}
