package com.revature.Boxed.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation marks a class that holds the primary logon credentials for an application
 * There should only be one class marked as a credentials class.
 *
 * @author Gabrielle Luna
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface CredentialsClass {

}
