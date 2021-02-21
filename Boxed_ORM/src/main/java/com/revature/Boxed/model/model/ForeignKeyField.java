package com.revature.Boxed.model.model;

import com.revature.Boxed.annotations.Column_FK;

import java.lang.reflect.Field;

/**
 *
 * @author Wezley Singleton
 */
public class ForeignKeyField {
    //Attributes -------------------------------------------------
    private final Field field;
    //Not allowing default values for Foreign Keys

    //Constructors -------------------------------------------------
    public ForeignKeyField(Field field) {
        if (field.getAnnotation(Column_FK.class) == null) {
            throw new IllegalStateException("Cannot create ForeignKeyField object! Provided field, "
                                            + getName() + " is not annotated with @Column_FK");
        }
        this.field = field;
    }

    //Getters and Setters------------------------------------------
    public String getName() { return field.getName();}

    public Class<?> getType() { return field.getType(); }

    public String getColumnName() { return field.getAnnotation(Column_FK.class).columnName();}

}
