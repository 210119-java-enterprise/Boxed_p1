package com.revature.Boxed.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks values that will have a default value set by the database
 * these fields are left empty on Insert transactions.
 *
 * @author Gabrielle Luna
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Generated {
}
