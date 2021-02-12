package com.revature.utilities;

import com.revature.annotations.Column;
import com.revature.annotations.Column_FK;
import com.revature.annotations.Column_PK;
import com.revature.annotations.Entity;

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
    private Class<T> clazz;
    private String entityName;
    private PrimaryKeyField primaryKeyField;
    private List<ColumnField> columnFields;
    private List<ForeignKeyField> foreignKeyFields;

    //Constructors -------------------------------------------------
    public static <T> Metamodel <T> of (Class<T> clazz){
        if (clazz.getAnnotation(Entity.class) == null){
            throw new IllegalStateException("Cannot create Metamodel object! Provided class, "
                                            + clazz.getName() + " is not annotated with @Entity");
        }
        return new Metamodel<>(clazz);
    }

    public Metamodel(Class<T> clazz){
        this.clazz = clazz;
        this.columnFields = new LinkedList<>();
        this.foreignKeyFields = new LinkedList<>();
    }

    //Other ---------------------------------------------------------
    public String getClassName(){ return clazz.getSimpleName(); }

    public String getSimpleClassName() { return clazz.getSimpleName();}

    public PrimaryKeyField getPrimaryKey(){
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            Column_PK primaryKey = field.getAnnotation(Column_PK.class);
            if (primaryKey != null) {
                return new PrimaryKeyField(field);
            }
        }
        throw new RuntimeException("Did not find a field annotated with @Column_PK in: "
                                   + clazz.getName());
    }

    public List<ColumnField> getColumns() {
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            Column column = field.getAnnotation(Column.class);
            if(column != null) {
                columnFields.add(new ColumnField(field));
            }
        }

        if (columnFields.isEmpty()) {
            throw new RuntimeException("No columns found in: " + clazz.getName());
        }

        return columnFields;
    }

    public List<ForeignKeyField> getForeignKeys() {
        List<ForeignKeyField> foreignKeyFields = new ArrayList<>();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            Column_FK column = field.getAnnotation(Column_FK.class);
            if (column != null) {
                foreignKeyFields.add(new ForeignKeyField(field));
            }
        }

        return foreignKeyFields;
    }

}
