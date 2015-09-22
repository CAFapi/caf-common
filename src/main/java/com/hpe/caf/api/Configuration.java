package com.hpe.caf.api;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Indicates either that the class is a simple Java object that stores configuration,
 * or for fields within a class, indicates this object is an embedded configuration
 * object that can be overridden by a ConfigurationSource.
 */
@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Configuration
{
}
