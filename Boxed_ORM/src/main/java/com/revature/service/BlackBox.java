package com.revature.service;

import com.revature.utilities.Configuration;
import com.revature.utilities.connection.ConnectionPool;
import com.revature.utilities.connection.R4ConnectionPool;
import com.revature.utilities.queries.ResultSetParser;

import java.net.ConnectException;
import java.sql.*;

public class BlackBox {
    //Attributes ----------------------------------------------------
    private Configuration config;
    private ConnectionPool connectionPool;
    private Connection currentConnection;
    private ResultSet rs;


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
    public boolean executeThisQuery(String sql){

        try {
            PreparedStatement pStmt = currentConnection.prepareStatement(sql);
            rs = pStmt.executeQuery();
            return true;
        }catch(SQLException e){
            System.out.println("Following SQL query failed: " + sql);
            e.printStackTrace();
        }

        return false;
    }

    public String getQueryResultSummary(){
        return ResultSetParser.getSummary(rs);
    }

    public <T> T getResultInClass(){
        String entityName = ResultSetParser.getEntityName(rs);
        Class entity;

        return null;
    }

}
