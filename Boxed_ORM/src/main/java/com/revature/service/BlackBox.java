package com.revature.service;

import com.revature.utilities.Configuration;
import com.revature.utilities.connection.ConnectionPool;
import com.revature.utilities.connection.R4ConnectionPool;

import java.sql.SQLException;

public class BlackBox {
    private Configuration config;
    private ConnectionPool connectionPool;
    //Constructors --------------------------------------------------
    public BlackBox() {
        //Load config file
        config = new Configuration();

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
    public boolean isConnected(){

        return false;
    }

}
