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
    public <T> T authenticateLogin(Class<T> model, String username, String password){
        repo.executeLoginQuery(currentConnection, username, password);
        return repo.getResultInObj(model);
    }

    public <T> T getFieldValue (String searchFieldName, T obj){
        try {
            repo.buildQueryToReturnField(obj, searchFieldName);
        }catch(IllegalAccessException e){
            e.printStackTrace();
        }
        repo.executeQuery(currentConnection);
        return (T) repo.getResultInObj(obj.getClass());
    }

    public <T> List<T> getObjsMatchingId(String searchFieldName, String value, boolean isString, T obj){
        System.out.println("In getObjsMatchingId: ");
        repo.buildQueryToReturnObjectById(obj, searchFieldName, value, isString);
        repo.executeQuery(currentConnection);
        return repo.getResultInList(obj);
    }

    //Insert --------------------------------------------------------
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
    public <T> boolean updateField(T obj, String updateFieldName, String updatedValue, boolean isString){
        try{
            repo.buildUpdateForObjField(obj, updateFieldName, updatedValue, isString);
        }catch(IllegalAccessException e){
            e.printStackTrace();
        }

        int result = repo.executeUpdate(currentConnection);
        return result > 0;
    }

    //Transactions --------------------------------------------------
    public void runQuery(){
        repo.executeQuery(currentConnection);
    }

    public boolean runInsert(){
        int result = repo.executeInsert(currentConnection);
        return result > 0;
    }

    //ResultSet -----------------------------------------------------
//    public String getQueryResultSummary(){
//        return ResultSetParser.getSummary(rs);
//    }
//
    //
//    public List<String[]> getResultInList(){
//        return ResultSetParser.getListFromResult(rs);
//    }

}
