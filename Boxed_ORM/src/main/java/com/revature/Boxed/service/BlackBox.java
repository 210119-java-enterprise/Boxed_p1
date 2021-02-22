package com.revature.Boxed.service;

import com.revature.Boxed.utilities.Configuration;
import com.revature.Boxed.utilities.ConnectionPool;
import com.revature.Boxed.utilities.R4ConnectionPool;
import com.revature.Boxed.repository.Repository;

import java.net.ConnectException;
import java.sql.*;
import java.util.List;

public class BlackBox {
    //Attributes ----------------------------------------------------
    private Configuration config;

    private ConnectionPool connectionPool;
    private Connection currentConnection;

    private final Repository repo;

    //Constructors --------------------------------------------------

    /**
     * Initializes the Black box with a string holding the location of the config file.
     * This allows it to generate the Metamodels and Configuration class which will be
     * saved to the class. It will also initialize the Connection pool and create its Repo.
     * @param configLocation    a string with the absolute file path to .properties file
     */
    public BlackBox(String configLocation) {
        //Load config file
        config = new Configuration(configLocation);

        //Load connections
        try {
            connectionPool = R4ConnectionPool.create(config.getDbUrl(), config.getDbUsername(),
                                config.getDbPassword(), config.getDbSchema());
        }catch (SQLException e){
            e.printStackTrace();
            System.out.println("Unable to create a connection to database, check config file for login credentials.");
        }

        repo = new Repository(config);
    }

    //Connection ----------------------------------------------------
    public Connection getConnection(){
        try {
            return connectionPool.getConnection();
        }catch(SQLException e){
            System.out.println("SQLException encountered: check config file for accurate database credentials");
        }
        return null;
    }

    public void setCurrentConnection(Connection conn)throws ConnectException, SQLException{
        if(conn == null){
            throw new ConnectException("Invalid Connection");
        }
        if (conn.isValid(100)){
            currentConnection = conn;
        }
    }

    //Queries -------------------------------------------------------
    //Todo: eliminate need for model?
    /**
     * Calls special query that authenticates a login
     * @param model     model of class holding credentials
     * @param username  login credential
     * @param password  login credential
     * @param <T>       class type
     * @return          an object holding the authenticated user
     */
    public <T> T authenticateLogin(Class<T> model, String username, String password){
        repo.executeLoginQuery(currentConnection, username, password);
        return repo.getResultInObj(model);
    }

    //TODO: change return type?
    /**
     * Executes a SEARCH query for a specific field
     * @param searchFieldName   name of field being searched for
     * @param obj               obj, potentially holding value to chekc against
     * @param <T>               obj type
     * @return                  copy of object holding field
     */
    public <T> T getFieldValue (String searchFieldName, T obj){
        try {
            repo.buildQueryToReturnField(obj, searchFieldName);
        }catch(IllegalAccessException e){
            e.printStackTrace();
        }
        repo.executeQuery(currentConnection);
        return (T) repo.getResultInObj(obj.getClass());
    }

    /**
     * Does a search for an object with a matching id
     * @param searchFieldName   name of field holding id
     * @param value             id value
     * @param isString          true is value needs to be wrapped in ' '
     * @param obj               copy of that will hold return
     * @param <T>               obj type
     * @return                  List of objects holding return values
     */
    public <T> List<T> getObjsMatchingId(String searchFieldName, String value, boolean isString, T obj){
        repo.buildQueryToReturnObjectById(obj, searchFieldName, value, isString);
        repo.executeQuery(currentConnection);
        return repo.getResultInObjList(obj);
    }

    //Insert --------------------------------------------------------
    /**
     * Insert new object into database
     * @param obj   object to be inserted
     * @param <T>   object type
     * @return      true if a row in database was inserted
     */
    public <T> boolean insert(T obj){
        try {
            repo.buildInsertForObj(obj);
        }catch(IllegalAccessException e){
            e.printStackTrace();
        }
        int result = repo.executeInsert(currentConnection);
        return result > 0;
    }

    //Update --------------------------------------------------------
    /**
     * Update a value in database base don the primary key of object
     * @param obj               copy of object being updated
     * @param updateFieldName   name of field being updated
     * @param updatedValue      new value
     * @param isString          true is value needs to be wrapped in ' '
     * @param <T>               obj type
     * @return                  true if a row in database was affected
     */
    public <T> boolean updateField(T obj, String updateFieldName, String updatedValue, boolean isString){
        try{
            repo.buildUpdateForObjField(obj, updateFieldName, updatedValue, isString);
        }catch(IllegalAccessException e){
            e.printStackTrace();
        }

        int result = repo.executeUpdate(currentConnection);
        return result > 0;
    }

    //Delete --------------------------------------------------------
    public <T> void deleteEntry(T obj, String fieldName, String fieldValue, boolean isString){
        try{
            repo.buildDeleteForObjWithId(obj, fieldName, fieldValue, isString);
        }catch (IllegalAccessException e){
            e.printStackTrace();
        }
        repo.executeDelete(currentConnection);
    }

    //Transactions --------------------------------------------------
    public void runQuery(){
        repo.executeQuery(currentConnection);
    }

    public boolean runInsert(){
        int result = repo.executeInsert(currentConnection);
        return result > 0;
    }

    public boolean runUpdate(){
        int result = repo.executeUpdate(currentConnection);
        return result > 0;
    }

    public boolean runDelete(){
        int result = repo.executeDelete(currentConnection);
        return result > 0;
    }

}
