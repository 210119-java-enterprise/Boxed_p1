package com.revature.Boxed.model;

import com.revature.Boxed.annotations.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Class holds the basic information needed from an annotated class created by user.
 * Intended for internal use.
 * @param <T> the type of class being modeled
 *
 * @author Gabrielle Luna
 */
public class Metamodel <T>{
    //Attributes -------------------------------------------------
    private final Class<T> clazz;
    private final List<Field> activeFields;
    private final String entityName;

    //Static --------------------------------------------------------
    public static <T> Metamodel <T> of (Class<T> clazz){
        if (clazz.getAnnotation(Entity.class) == null){
            throw new IllegalStateException("Cannot create Metamodel object! Provided class, "
                                            + clazz.getName() + " is not annotated with @Entity");
        }
        return new Metamodel<>(clazz);
    }
    //Constructors -------------------------------------------------
    /**
     * Follows a static block of code that confirms the class in question has the Entity
     * Annotation. It will keep a copy of the Class, set a list of active annotated fields
     * and keep a copy of the Classes corresponding Entity name.
     * @param clazz the class being modeled.
     */
    public Metamodel(Class<T> clazz){
        this.clazz = clazz;
        this.activeFields = setActiveFields();
        this.entityName = clazz.getAnnotation(Entity.class).tableName();
    }

    //Getters and Setters -------------------------------------------
    public Class<T> getClazz() { return clazz; }

    public String getClassName(){ return clazz.getSimpleName(); }

    public String getEntityName() { return entityName;}

    /**
     * Adds any field marked with the Column Annotation to a list of active
     * or watched fields.
     * @return  a List of active fields.
     */
    public List<Field> setActiveFields() {
        Field[] fields = clazz.getDeclaredFields();
        List<Field> activeFields = new ArrayList<>();

        for (Field field : fields) {
            Column column = field.getAnnotation(Column.class);
            if (column != null)
                activeFields.add(field);
        }

        if (activeFields.isEmpty())
            throw new RuntimeException("No marked fields found in: " + clazz.getName());

        return activeFields;
    }

    public List<Field> getActiveFields() {
        return activeFields;
    }

    //Credentials ---------------------------------------------------

    /**
     * Only one class is anticipated to be marked as a CredentialClass
     * if this one is it will return true
     * @return boolean, true for a CredentialClass, false otherwise
     */
    public boolean isLogOnCredentials(){
        return clazz.getAnnotation(CredentialsClass.class) != null;
    }

    /**
     * Returns a string, colon separated, with the username and password column names
     * @return String with credentials
     */
    public String getCredentialFields(){
        StringBuilder response = new StringBuilder();
        if (isLogOnCredentials()){
            for (Field field:getActiveFields()) {
                if (field.getAnnotation(Credential.class) != null)
                    response.append(":").append(field.getAnnotation(Column.class).columnName());
            }
        }
        return response.toString();
    }
}
