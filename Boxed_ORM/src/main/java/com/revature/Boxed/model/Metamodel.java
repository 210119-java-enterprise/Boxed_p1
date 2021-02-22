package com.revature.Boxed.model;

import com.revature.Boxed.annotations.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @param <T>
 *
 * @author Wezley Singleton
 */
public class Metamodel <T>{
    //Attributes -------------------------------------------------
    private final Class<T> clazz;
    private final List<ColumnField> columnFields;

    //Static --------------------------------------------------------
    public static <T> Metamodel <T> of (Class<T> clazz){
        if (clazz.getAnnotation(Entity.class) == null){
            throw new IllegalStateException("Cannot create Metamodel object! Provided class, "
                                            + clazz.getName() + " is not annotated with @Entity");
        }
        return new Metamodel<>(clazz);
    }
    //Constructors -------------------------------------------------
    public Metamodel(Class<T> clazz){
        this.clazz = clazz;
        this.columnFields = new LinkedList<>();
    }

    //Getters and Setters -------------------------------------------
    public Class<T> getClazz() { return clazz; }

    public Constructor<?>[] getConstructors(){ return clazz.getConstructors();}

    public String getClassName(){ return clazz.getSimpleName(); }

    public String getEntityName() { return clazz.getAnnotation(Entity.class).tableName();}

    public Field getPrimaryKey(){
        return null;
    }

    public Field getForeignKeys() {
        return null;
    }

    public List<Field> getActiveFields() {
        Field[] fields = clazz.getDeclaredFields();
        List<Field> activeFields = new ArrayList<>();

        for (Field field : fields) {
            Column column = field.getAnnotation(Column.class);
            if (column != null){
                activeFields.add(field);
            }
        }

        if (activeFields.isEmpty())
            throw new RuntimeException("No marked fields found in: " + clazz.getName());

        return activeFields;
    }


    public String getCredentialFields(){
        String response = "";
        if (isLogOnCredentials()){
            for (Field field:getActiveFields()) {
                if (field.getAnnotation(Credential.class) != null)
                    response += ":" + field.getAnnotation(Column.class).columnName();
            }
        }
        System.out.println("Meta response: " + response);
            return response;
    }

    //Other ---------------------------------------------------------
    public boolean isLogOnCredentials(){
        return clazz.getAnnotation(CredentialsClass.class) != null;
    }


}
