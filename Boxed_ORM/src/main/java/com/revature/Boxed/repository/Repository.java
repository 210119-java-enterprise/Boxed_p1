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
    private final DeleteBuilder deleteBuilder = new DeleteBuilder();

    private ResultSet result;
    ResultSetParser resultSetParser;

    Configuration config;

    //Constructor ---------------------------------------------------

    /**
     * Initialize query builder with a Login Authentication Query based on
     * Credentials class
     * @param config    copy of configuration file
     */
    public Repository(Configuration config) {
        //login query
        String configCreds = config.getLoginCredEntityName();
        if(configCreds != null){
            String[] creds = configCreds.split(":");
            if (creds.length == 3){
                queryBuilder.newTransaction();
                queryBuilder.returnFields();
                queryBuilder.ofEntityType(creds[0]);
                queryBuilder.addCondition_Operator(creds[1], "=", "?", false);
                queryBuilder.addCondition_Operator(creds[2], "=", "?", false);
                queryBuilder.saveQuery(0);
                System.out.println("\u001B[33m" + queryBuilder.getTransaction() + "\u001B[0m");
            }
        }
        this.config = config;
    }

    //Build Query ---------------------------------------------------
    /**
     * A SELECT Transaction builder that makes a query to return a single column that is
     * either narrowed done by a WHERE clause or not.
     * from a single table search
     * format: SELECT entity FROM fieldName, SELECT entity FROM fieldName WHERE fieldName = obj.fieldName
     * @param obj           a copy of the object that either has a value for the field being searched or doesn't
     * @param fieldName     name of the field being searched
     * @param <T>           the class type for obj
     * @throws IllegalAccessException   if a field is not accessible
     */
    public <T> void buildQueryToReturnField(T obj, String fieldName) throws IllegalAccessException {
        //start query statement
        queryBuilder.newTransaction();
        queryBuilder.ofEntityType(obj.getClass().getAnnotation(Entity.class).tableName());


        Metamodel<Class<?>> meta = config.getMatchingMetamodel(obj.getClass());
        List<Field> activeFields = meta.getActiveFields();
        //search all of the objects annotated fields
        for (Field field :activeFields) {
            field.setAccessible(true);
            Object value = field.get(obj);
            //If the desired field is found add a FROM clause
            if (field.getName().equals(fieldName))
                queryBuilder.returnFields(field.getAnnotation(Column.class).columnName());
            //If the desired field has a value in the sent in obj specify a match in a WHERE clause
            if (value != null) {
                queryBuilder.addCondition_Operator(field.getAnnotation(Column.class).columnName(),
                        "=", value.toString(), value.getClass() == String.class);
            }
        }
    }

    /**
     * A SELECT Transaction builder that makes a query to return an entire object based on an id
     * passed in
     * format: SELECT * FROM entity WHERE searchFieldName = searchFieldValue
     * @param obj                   a copy of the obj representing the table
     * @param searchFieldName       the name of the field that represents the id
     * @param searchFieldValue      the id value
     * @param isString              whether of not the id needs to be wrapped in ' '
     * @param <T>                   the class type
     */
    public <T> void buildQueryToReturnObjectById(T obj, String searchFieldName, String searchFieldValue, boolean isString){
        //start query statement
        queryBuilder.newTransaction();
        queryBuilder.ofEntityType(obj.getClass().getAnnotation(Entity.class).tableName());
        queryBuilder.returnFields();

        //Add search parameters
        Metamodel<Class<?>> meta = config.getMatchingMetamodel(obj.getClass());
        List<Field> activeFields = meta.getActiveFields();
        for (Field field : activeFields) {
            field.setAccessible(true);
            if (field.getName().equals(searchFieldName)){
                queryBuilder.addCondition_Operator(field.getAnnotation(Column.class).columnName(),
                        "=", searchFieldValue, isString);
                break;
            }
        }
    }

    //Build Update --------------------------------------------------
    /**
     * A UPDATE Transaction builder that updates a single field based on the id value store in the obj
     * format: UPDATE entity SET updateFieldName = updatedValue WHERE id = obj.id
     * @param obj               a copy of the object being updated
     * @param updateFieldName   the name of the field whose value is being updated
     * @param updatedValue      the new value
     * @param isString          whether or not the value needs to be wrapped in ' '
     * @param <T>               the obj type
     * @throws IllegalAccessException when a field is inaccessible
     */
    public <T> void buildUpdateForObjField(T obj, String updateFieldName, String updatedValue, boolean isString)
            throws IllegalAccessException {
        //start query statement
        updateBuilder.craftNewTransaction();
        updateBuilder.ofEntityType(obj.getClass().getAnnotation(Entity.class).tableName());
        updateBuilder.updateKeyValuePair(updateFieldName, updatedValue, isString);

        //Add conditions
        Metamodel<Class<?>> meta = config.getMatchingMetamodel(obj.getClass());
        List<Field> activeFields = meta.getActiveFields();
        for (Field field : activeFields){
            field.setAccessible(true);
            //If a primary key is found, use it to create a where statement
            if(field.getAnnotation(Column.class).type().compareTo(ColumnType.PK) == 0){
                updateBuilder.addCondition_Operator(field.getAnnotation(Column.class).columnName(),
                                "=", Integer.toString(field.getInt(obj)), false);
            }
        }

    }

    //Build Insert --------------------------------------------------
    /**
     * A INSERT Transaction Builder that inserts a new object into the database
     * format:  INSERT INTO entity (columns, ...) VALUES (value, ...)
     * @param obj       object being inserted
     * @param <T>       object type
     * @throws IllegalAccessException   when a field is inaccessible
     */
    public <T> void buildInsertForObj(T obj) throws IllegalAccessException{
        //start insert statement
        insertBuilder.newTransaction();
        insertBuilder.ofEntityType(obj.getClass().getAnnotation(Entity.class).tableName());

        Metamodel<Class<?>> meta = config.getMatchingMetamodel(obj.getClass());
        List<Field> activeFields = meta.getActiveFields();
        for (Field field: activeFields) {
            field.setAccessible(true);
            Object value = field.get(obj);
            if (value != null && field.getAnnotation(Generated.class) == null) {
                //if the fields value is not null and it is not a field generated by the database at the
                //key value pair to the Transaction
                insertBuilder.insertKeyValuePair(field.getAnnotation(Column.class).columnName(),
                        value.toString(), value.getClass() == String.class);
            }
        }
    }

    //Build Delete --------------------------------------------------
    /**
     * A DELETE Transaction Builder that deletes an object from the database
     * format: DELETE FROM entity WHERE id = obj.id
     * @param obj       copy of object being deleted
     * @param <T>       obj type
     * @throws IllegalAccessException   thrown if field is inaccessible
     */
    public <T> void buildDeleteForObjWithId(T obj, String fieldName, String fieldValue, boolean isString) throws IllegalAccessException{
        //start delete statement
        deleteBuilder.newTransaction();
        deleteBuilder.ofEntityType(obj.getClass().getAnnotation(Entity.class).tableName());

        //Add conditions
        Metamodel<Class<?>> meta = config.getMatchingMetamodel(obj.getClass());
        List<Field> activeFields = meta.getActiveFields();
        for (Field field : activeFields){
            field.setAccessible(true);
            //If a primary key is found, use it to create a where statement
            if (field.getName().equals(fieldName)){
                deleteBuilder.addCondition_Operator(field.getAnnotation(Column.class).columnName(),
                        "=", fieldValue, isString);
                break;
            }
        }
    }

    //Execute -------------------------------------------------------
    /**
     * Login Query is a special query that can bypass the build because one was created in the
     * constructor and save to query speed dial
     * @param connection    database connection
     * @param username      login credentials
     * @param password      login credentials
     */
    public void executeLoginQuery(Connection connection, String username, String password) {
        //Validate
        queryBuilder.isValidName(username, password);

        queryBuilder.loadQuery(0);
        PreparedStatement pStmt = null;
        try{
            pStmt = connection.prepareStatement(queryBuilder.getTransaction());
            pStmt.setString(1, username);
            pStmt.setString(2, password);

            result = pStmt.executeQuery();
            System.out.println("\u001B[33m" + pStmt + "\u001B[0m");

        }catch (SQLException e){
            System.out.println("Following SQL query failed: " + pStmt);
            e.printStackTrace();
        }
    }

    public void executeQuery(Connection connection){
        String sql = queryBuilder.getTransaction();
        try {
            PreparedStatement pStmt = connection.prepareStatement(sql);
            result =  pStmt.executeQuery();
            System.out.println("\u001B[33m" + pStmt + "\u001B[0m");

        }catch(SQLException e){
            System.out.println("Following SQL query failed: " + sql);
            e.printStackTrace();
        }
    }

    public int executeInsert(Connection connection){
        String insert = insertBuilder.getTransaction();
        return Update(connection, insert);
    }

    public int executeUpdate(Connection connection){
        String update = updateBuilder.getTransaction();
        return Update(connection, update);
    }

    public int executeDelete(Connection connection){
        String delete = deleteBuilder.getTransaction();
        return Update(connection, delete);
    }

    private int Update(Connection connection, String sql){
        try{
            PreparedStatement pStmt = connection.prepareStatement(sql);
            System.out.println("\u001B[33m" + pStmt + "\u001B[0m");
            return pStmt.executeUpdate();
        }catch (SQLException e){
            System.out.println("Following SQL query failed: " + sql);
            e.printStackTrace();
        }
        return -1;
    }

    //Retrieve ------------------------------------------------------
    /**
     * Returns the query result in the matching Class
     * @param model     a model of the class the result will be parsed into
     * @param <T>       the obj type
     * @return          an new object of type T with result inside
     */
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

    /**
     * Returns the query in a list of objects in the matching class
     * @param obj   a model of the class the result will be parsed into
     * @param <T>   the object type
     * @return      a list of new objects with the result inside
     */
    public <T> List<T> getResultInObjList(T obj){
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

