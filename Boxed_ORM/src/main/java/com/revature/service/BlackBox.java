package com.revature.service;

import com.revature.model.Metamodel;
import com.revature.utilities.Configuration;
import com.revature.utilities.connection.ConnectionPool;
import com.revature.utilities.connection.R4ConnectionPool;
import com.revature.repository.Repository;
import com.revature.utilities.queries.ResultSetParser;

import java.net.ConnectException;
import java.sql.*;
import java.util.ArrayList;
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
        return repo.getLoginUser(model);
    }

    //Insert --------------------------------------------------------
    public <T> boolean insert(Class<T> model){
        int result = repo.executeInsert(currentConnection, model);
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
