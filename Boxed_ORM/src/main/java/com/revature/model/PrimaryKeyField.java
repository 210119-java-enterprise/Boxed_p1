package com.revature.model;

import com.revature.annotations.Column_PK;
import com.revature.annotations.Generated;

import java.lang.reflect.Field;

/**
 *
 * @author Wezley Singleton
 */
public class PrimaryKeyField {
    //Attributes -------------------------------------------------
    private final Field field;
    private final boolean isDefault;

    //Constructors -------------------------------------------------
    public PrimaryKeyField(Field field) {
        //check for Column_PK annotation on field before assigning
        if (field.getAnnotation(Column_PK.class) == null) {
            throw new IllegalStateException("Cannot create PKField object! Provided field, "
                                            + getName() + "is not annotated with @Column_PK");
        }
        this.field = field;

        //check for Default annotation on field before assigning
        isDefault = field.getAnnotation(Generated.class) != null;

    }

    //Getters and Setters------------------------------------------
    public String getName() {return field.getName();}

    public Class<?> getType() { return field.getType(); }

    public String getColumnName() { return field.getAnnotation(Column_PK.class).columnName(); }

    public boolean isDefault() { return isDefault; }
}
