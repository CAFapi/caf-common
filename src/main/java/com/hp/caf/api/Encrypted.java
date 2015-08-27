package com.hp.caf.api;


import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


/**
 * Marker annotation that specifies an item, typically in a configuration class, that
 * a SecurityProvider can decrypt. The field that possesses this annotation must also
 * have the appropriate getter and setter methods that match Java convention.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Encrypted
{
}
