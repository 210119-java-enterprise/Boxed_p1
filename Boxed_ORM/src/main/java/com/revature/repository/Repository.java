package com.revature.repository;

import com.revature.model.Metamodel;
import com.revature.utilities.Configuration;
import com.revature.utilities.queries.InsertBuilder;
import com.revature.utilities.queries.QueryBuilder;
import com.revature.utilities.queries.ResultSetParser;

import javax.xml.transform.Result;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Repository {
    private final QueryBuilder queryBuilder = new QueryBuilder();
    private final InsertBuilder insertBuilder = new InsertBuilder();

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
    //Build Insert --------------------------------------------------
    private <T> void buildInsert(Class<T> model){


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

    public <T> int executeInsert(Connection connection, Class<T> model){
        buildInsert(model);
        return executeInsert(connection);
    }

    //Retrieve ------------------------------------------------------
    public <T> T getLoginUser(Class<T> model){
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

}
