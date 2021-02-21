package com.revature.Boxed.model.model;

import com.revature.Boxed.annotations.*;
import com.revature.annotations.*;

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
    private PrimaryKeyField primaryKeyField;
    private final List<ColumnField> columnFields;
    private List<ForeignKeyField> foreignKeyFields;

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
        this.foreignKeyFields = new LinkedList<>();
    }

    //Getters and Setters -------------------------------------------
    public Class<T> getClazz() { return clazz; }

    public Constructor<?>[] getConstructors(){ return clazz.getConstructors();}

    public String getClassName(){ return clazz.getSimpleName(); }

    public String getEntityName() { return clazz.getAnnotation(Entity.class).tableName();}

    public PrimaryKeyField getPrimaryKey(){
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            Column_PK primaryKey = field.getAnnotation(Column_PK.class);
            if (primaryKey != null)
                return new PrimaryKeyField(field);
        }
        throw new RuntimeException("Did not find a field annotated with @Column_PK in: "
                + clazz.getName());
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

    public List<ForeignKeyField> getForeignKeys() {
        List<ForeignKeyField> foreignKeyFields = new ArrayList<>();
        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields) {
            Column_FK column = field.getAnnotation(Column_FK.class);
            if (column != null)
                foreignKeyFields.add(new ForeignKeyField(field));
        }
        return foreignKeyFields;
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
