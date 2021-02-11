package com.revature.utilities;

import com.revature.annotations.Column_PK;

import java.lang.reflect.Field;

/**
 *
 * @author Wezley Singleton
 */
public class PrimaryKeyField {
    //Attributes -------------------------------------------------
    private Field field;

    //Constructors -------------------------------------------------
    public PrimaryKeyField(Field field) {
        if (field.getAnnotation(Column_PK.class) == null) {
            throw new IllegalStateException("Cannot create PKField object! Provided field, "
                                            + getName() + "is not annotated with @Column_PK");
        }
        this.field = field;
    }

    //Getters and Setters------------------------------------------
    public String getName() {return field.getName();}

    public Class<?> getType() { return field.getType(); }

    public String getColumnName() { return field.getAnnotation(Column_PK.class).columnName(); }
}
