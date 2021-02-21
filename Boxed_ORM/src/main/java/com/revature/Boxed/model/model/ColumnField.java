package com.revature.Boxed.model.model;

import com.revature.Boxed.annotations.Column;
import com.revature.Boxed.annotations.Credential;
import com.revature.Boxed.annotations.Generated;

import java.lang.reflect.Field;

/**
 *
 * @author Wezley Singleton
 */
public class ColumnField {
    //Attributes -------------------------------------------------
    private final Field field;
    private final boolean isDefault, isCredential;

    //Constructors -------------------------------------------------
    public ColumnField (Field field) {
        if (field.getAnnotation(Column.class) == null) {
            throw new IllegalStateException("Cannot create columnField object! Provided Field"
                                            + getName() + " is not annotated with @Column");
        }
        this.field = field;

        //check for Default annotation on field before assigning
        isDefault = field.getAnnotation(Generated.class) != null;
        isCredential = field.getAnnotation(Credential.class) != null;
    }

    //Getters and Setters------------------------------------------
    public String getName() { return field.getName();}

    public Class<?> getType() { return field.getType();}

    public String getColumnName() { return field.getAnnotation(Column.class).columnName();}

    public boolean isDefault() { return isDefault; }

    public boolean isCredential() {
        return isCredential;
    }
}
