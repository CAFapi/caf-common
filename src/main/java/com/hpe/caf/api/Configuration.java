package com.hpe.caf.api;


import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


/**
 * Indicates this object can be overridden by a ConfigurationSource.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Configuration
{
}
