package com.hpe.caf.api;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Used on a field of type Map with String keys to indicate that they keys
 * must contains specific Strings.
 * @since 5.0
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {ContainsStringKeysValidator.class})
public @interface ContainsStringKeys {
    String message() default "{com.hp.caf.api.ContainsStringKeys.message}";
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default { };
    String[] keys() default {};
}
