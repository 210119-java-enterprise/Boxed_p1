package com.revature.Boxed.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This will mark fields used for log in purposes for a special
 * Query execution. Two are expected with the username listed ahead
 * of the password
 * @author Gabrille Luna
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Credential {
}
