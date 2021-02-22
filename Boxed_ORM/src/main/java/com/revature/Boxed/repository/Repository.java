package com.revature.Boxed.repository;

import com.revature.Boxed.annotations.Column;
import com.revature.Boxed.annotations.Entity;
import com.revature.Boxed.annotations.Generated;
import com.revature.Boxed.model.ColumnType;
import com.revature.Boxed.model.Metamodel;
import com.revature.Boxed.utilities.Configuration;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Repository {
    private final QueryBuilder queryBuilder = new QueryBuilder();
    private final InsertBuilder insertBuilder = new InsertBuilder();
    private final UpdateBuilder updateBuilder = new UpdateBuilder();

    private ResultSet result;
    ResultSetParser resultSetParser;

    Configuration config;

    //Constructor ---------------------------------------------------
    public Repository(Configuration config) {
        //login query
        String configCreds = config.getLoginCredEntityName();
        System.out.println(configCreds);
        String[] creds = configCreds.split(":");
        System.out.println(creds.toString());
        queryBuilder.craftNewTransaction()
                .returnFields()
                .ofClassType(creds[0])
                .addCondition_Operator(creds[1], "=", "?", false)
                .addCondition_Operator(creds[2], "=", "?", false)
                .saveQuery(0);
        this.config = config;
    }

    //Build Query ---------------------------------------------------
    public <T> void buildQueryToReturnField(T obj, String fieldName) throws IllegalAccessException {
        //start query statement
        queryBuilder.craftNewTransaction()
                .ofClassType(obj.getClass().getAnnotation(Entity.class).tableName());

        Metamodel<Class<?>> meta = config.getMatchingMetamodel(obj.getClass());
        List<Field> activeFields = meta.getActiveFields();
        for (Field field :activeFields) {
            field.setAccessible(true);
            Object value = field.get(obj);
            if (field.getName().equals(fieldName))
                queryBuilder.returnFields(field.getAnnotation(Column.class).columnName());
            if (value != null) {
                queryBuilder.addCondition_Operator(field.getAnnotation(Column.class).columnName(),
                        "=", value.toString(), value.getClass() == String.class);
            }
        }
    }

    public <T> void buildQueryToReturnObjectById(T obj, String searchFieldName, String searchFieldValue, boolean isString){
        //start query statement
        queryBuilder.craftNewTransaction()
                .ofClassType(obj.getClass().getAnnotation(Entity.class).tableName())
                .returnFields();
        //Add search parameters
        Metamodel<Class<?>> meta = config.getMatchingMetamodel(obj.getClass());
        List<Field> activeFields = meta.getActiveFields();
        for (Field field : activeFields) {
            field.setAccessible(true);
            System.out.println(field.getName() + " =? " + searchFieldName);
            if (field.getName().equals(searchFieldName)){
                System.out.println("in field == searchFieldName");
                queryBuilder.addCondition_Operator(field.getAnnotation(Column.class).columnName(),
                        "=", searchFieldValue, isString);
                break;
            }
        }
        System.out.println("buildQueryToReturnObjById : " + queryBuilder.getQuery());
    }

    //Build Update --------------------------------------------------
    public <T> void buildUpdateForObjFields(T obj, String updateFieldName, String updatedValue, boolean isString)
            throws IllegalAccessException {
        //start query statement
        updateBuilder.craftNewTransaction()
                .updateType(obj.getClass().getAnnotation(Entity.class).tableName())
                .updateKeyValuePair(updateFieldName, updatedValue, isString);

        //Add conditions
        Metamodel<Class<?>> meta = config.getMatchingMetamodel(obj.getClass());
        List<Field> activeFields = meta.getActiveFields();
        for (Field field : activeFields){
            field.setAccessible(true);
            if(field.getAnnotation(Column.class).type().compareTo(ColumnType.PK) == 0){
                updateBuilder.updateConditions(field.getAnnotation(Column.class).columnName(),
                                "=", Integer.toString(field.getInt(obj)), false);
            }
        }

    }

    //Build Insert --------------------------------------------------
    public <T> void buildInsertForObj(T obj) throws IllegalAccessException{
        //start insert statement
        insertBuilder.craftNewTransaction()
                    .insertType(obj.getClass().getAnnotation(Entity.class).tableName());

        Metamodel<Class<?>> meta = config.getMatchingMetamodel(obj.getClass());
        List<Field> activeFields = meta.getActiveFields();
        for (Field field: activeFields) {
            field.setAccessible(true);
            Object value = field.get(obj);
            if (value != null && field.getAnnotation(Generated.class) == null) {
                System.out.println("key : " + field.getAnnotation(Column.class).columnName() + " value : "
                        + value.toString());
                insertBuilder.insertKeyValuePair(field.getAnnotation(Column.class).columnName(),
                        value.toString(), value.getClass() == String.class);
            }
        }
    }

    //Execute -------------------------------------------------------
    public void executeLoginQuery(Connection connection, String username, String password)
            throws IllegalArgumentException {
        //Validate
        queryBuilder.isValidName(username, password);

        queryBuilder.loadQuery(0);
        PreparedStatement pStmt = null;
        try{
            pStmt = connection.prepareStatement(queryBuilder.getQuery());
            pStmt.setString(1, username);
            pStmt.setString(2, password);

            result = pStmt.executeQuery();
            System.out.println(pStmt);

        }catch (SQLException e){
            System.out.println("Following SQL query failed: " + pStmt);
            e.printStackTrace();
        }
    }

    public void executeQuery(Connection connection){
        String sql = queryBuilder.getQuery();
        System.out.println(sql);
        try {
            PreparedStatement pStmt = connection.prepareStatement(sql);
            result =  pStmt.executeQuery();
        }catch(SQLException e){
            System.out.println("Following SQL query failed: " + sql);
            e.printStackTrace();
        }
    }

    public int executeInsert(Connection connection){
        String insert = insertBuilder.getInsertTransaction();
        try {
            PreparedStatement pStmt = connection.prepareStatement(insert);
            return pStmt.executeUpdate();
        }catch(SQLException e){
            System.out.println("Following SQL update failed: " + insert);
            e.printStackTrace();
        }
        return -1;
    }

    public int executeUpdate(Connection connection){
        String update = updateBuilder.getUpdateTransaction();
        try{
            PreparedStatement pStmt = connection.prepareStatement(update);
            return pStmt.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return -1;
    }

    //Retrieve ------------------------------------------------------
    public <T> T getResultInObj(Class<T> model){
        if (result != null){
            Metamodel<Class<?>> meta = config.getMatchingMetamodel(model);
            try {
                while(result.next()){
                    T t = model.newInstance();
                    //noinspection unchecked
                    return (T) ResultSetParser.getObjFromResult(t, meta.getActiveFields(), result);
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        return null;
    }

    public <T> List<T> getResultInList(T obj){
        System.out.println("In getResultInList : ");
        List<T> list = new ArrayList<>();

        if (result != null){
            Metamodel<Class<?>> meta = config.getMatchingMetamodel(obj.getClass());
            try{
                while(result.next()){
                    T t = (T) obj.getClass().newInstance();
                    //noinspection unchecked
                    list.add((T) ResultSetParser.getObjFromResult(t, meta.getActiveFields(), result));
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        return list;
    }

}

