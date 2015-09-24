package com.hpe.caf.api;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Marker annotation that specifies an item, typically in a configuration class, that
 * a SecurityProvider can decrypt. The field that possesses this annotation must also
 * have the appropriate getter and setter methods that match Java convention.
 * @since 2.0
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Encrypted
{
}
