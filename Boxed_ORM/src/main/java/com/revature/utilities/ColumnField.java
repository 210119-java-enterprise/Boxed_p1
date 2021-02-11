package com.revature.utilities;

import com.revature.annotations.Column;

import java.lang.reflect.Field;

/**
 *
 * @author Wezley Singleton
 */
public class ColumnField {
    //Attributes -------------------------------------------------
    private Field field;

    //Constructors -------------------------------------------------
    public ColumnField (Field field) {
        if (field.getAnnotation(Column.class) == null) {
            throw new IllegalStateException("Cannot create columnField object! Provided Field"
                                            + getName() + " is not annotated with @Column");
        }
        this.field = field;
    }

    //Getters and Setters------------------------------------------
    public String getName() { return field.getName();}

    public Class<?> getType() { return field.getType();}

    public String getColumnName() { return field.getAnnotation(Column.class).columnName();}
}
