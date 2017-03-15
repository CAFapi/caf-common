/*
 * Copyright 2015-2017 Hewlett Packard Enterprise Development LP.
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
package com.hpe.caf.util.jerseycompat;

import org.glassfish.jersey.internal.ServiceFinder;

import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Compatibility class to allow Jersey 1 libraries to co-exist with Jersey 2 apps, such as Dropwizard.
 *
 * To use this, ensure the following is performed before starting the Jersey 2 app: ServiceFinder.setIteratorProvider(new
 * Jersey2ServiceIteratorProvider());
 */
public class Jersey2ServiceIteratorProvider extends ServiceFinder.ServiceIteratorProvider
{
    ServiceFinder.DefaultServiceIteratorProvider delegate = new ServiceFinder.DefaultServiceIteratorProvider();

    @Override
    public <T> Iterator<T> createIterator(Class<T> service, String serviceName, ClassLoader loader, boolean ignoreOnClassNotFound)
    {
        return delegate.createIterator(service, serviceName, loader, ignoreOnClassNotFound);
    }

    /**
     * Excludes all "com.sun.jersey" classes.
     */
    @Override
    public <T> Iterator<Class<T>> createClassIterator(
        Class<T> service,
        String serviceName,
        ClassLoader loader,
        boolean ignoreOnClassNotFound
    )
    {
        final Iterator<Class<T>> delegateClassIterator = delegate.createClassIterator(
            service, serviceName, loader, ignoreOnClassNotFound);

        Stream<Class<T>> stream = StreamSupport.stream(
            Spliterators.spliteratorUnknownSize(delegateClassIterator, Spliterator.ORDERED), false);

        return stream.filter(t -> !t.getPackage().getName().startsWith("com.sun.jersey")).collect(Collectors.toList()).iterator();
    }
}
