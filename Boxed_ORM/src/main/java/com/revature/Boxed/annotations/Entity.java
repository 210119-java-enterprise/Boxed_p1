package com.revature.Boxed.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation marks a class that should be tracked by the ORM
 * only classes with this annotation and listed in the .properties file
 * will be seen by ORM.
 *
 * @author Gabrielle Luna
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Entity {
    /**
     * The name must match exactly the on used by database
     * @return a string holding databases name for this field.
     */
    String tableName();
}
