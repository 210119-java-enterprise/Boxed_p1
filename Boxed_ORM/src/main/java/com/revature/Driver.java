package com.revature;


import com.revature.service.BlackBox;
import com.revature.service.QueryBuilder;
import org.postgresql.core.Query;

import java.sql.Connection;

public class Driver {
    private static String configLocation = "src/main/resources/BoxedCfg.properties";
    public static void main(String[] args) {
//        //1.)get instance of black box
//        BlackBox box = new BlackBox(configLocation);
//        //2.) get connection to database
//        Connection conn = box.getConnection();
//        //3.) set current connection, forcing user to handle Session validation
//        try {
//            box.setCurrentConnection(conn);
//        }catch (Exception e){
//            e.printStackTrace();
//        }

        //Query calls
        QueryBuilder builder = new QueryBuilder();
        System.out.println(builder.craftQuery()
                                  .returnAllFields()
                                  .ofClassType("User")
                                  .getQuery());

        System.out.println(builder.craftQuery()
                                  .returnFields("username")
                                  .ofClassType("User")
                                  .getQuery());

        System.out.println(builder.craftQuery()
                                  .returnFields("username", "password")
                                  .ofClassType("User")
                                  .getQuery());


    }
}

