package com.revature.utilities.queries;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Repository {

    public static ResultSet executeThisQuery(Connection connection, String sql){
        try {
            PreparedStatement pStmt = connection.prepareStatement(sql);
            ResultSet rs = pStmt.executeQuery();
            return rs;
        }catch(SQLException e){
            System.out.println("Following SQL query failed: " + sql);
            e.printStackTrace();
        }
        return null;
    }

    public static int executeThisUpdate(Connection connection, String sql){
        try {
            PreparedStatement pStmt = connection.prepareStatement(sql);
            return pStmt.executeUpdate();
        }catch(SQLException e){
            System.out.println("Following SQL update failed: " + sql);
            e.printStackTrace();
        }
        return -1;
    }
}
