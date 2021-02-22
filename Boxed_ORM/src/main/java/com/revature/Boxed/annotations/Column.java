package com.revature.Boxed.annotations;

import com.revature.Boxed.model.ColumnType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Annotation marks a field that will mirror a column in the database.
 * fields that are not marked will be ignored by the Repository
 *
 * @author Gabrielle Luna
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Column {
    /**
     * The type will be either PK, FK, or Default.
     * Marking special use for columns by repository
     * @return an enum called ColumnType
     */
    ColumnType type ();

    /**
     * The name must match exactly the one used by database
     * @return a string holding database name for this field
     */
    String columnName();
}
