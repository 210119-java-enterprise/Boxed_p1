package com.revature.service;

import com.revature.model.Metamodel;
import com.revature.utilities.Configuration;
import com.revature.utilities.connection.ConnectionPool;
import com.revature.utilities.connection.R4ConnectionPool;
import com.revature.utilities.queries.Repository;
import com.revature.utilities.queries.ResultSetParser;

import java.lang.reflect.Constructor;
import java.net.ConnectException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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

    //Transactions --------------------------------------------------
    public boolean runQuery(String sql){
        rs = Repository.executeThisQuery(currentConnection, sql);
        return rs != null;
    }

    public boolean runUpdate(String sql){
        int result = Repository.executeThisUpdate(currentConnection, sql);
        return result > 0;
    }

    //ResultSet -----------------------------------------------------
    public String getQueryResultSummary(){
        return ResultSetParser.getSummary(rs);
    }

    public <T> List<T> getResultInClass(Class<T> model){
        Metamodel <Class<?>> meta = config.getMatchingMetamodel(model);

        List<T> list = new ArrayList<T>();

        try {
            while(rs.next()){
                T t = model.newInstance();
                //noinspection unchecked
                list.add((T) ResultSetParser.getObjFromResult(t, meta.getActiveFields(), rs));
            }
        }catch(Exception e){
            e.printStackTrace();
        }

        return list;
    }

    public List<String[]> getResultInList(){
        return ResultSetParser.getListFromResult(rs);
    }

}
