package com.revature;


import com.revature.service.BlackBox;
import com.revature.service.QueryBuilder;
import com.revature.test_models.User;
import org.postgresql.core.Query;

import java.sql.Connection;

public class Driver {
    private static String configLocation = "src/main/resources/BoxedCfg.properties";
    public static void main(String[] args) {
        //1.)get instance of black box
        BlackBox box = new BlackBox(configLocation);
        System.out.println("Step 1 complete\n");
        //2.) get connection to database
        Connection conn = box.getConnection();
        System.out.println("Step 2 complete\n");
        //3.) set current connection, forcing user to handle Session validation
        try {
            box.setCurrentConnection(conn);
        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("Step 3 complete\n");

        //4.) Craft Query
        QueryBuilder builder = new QueryBuilder();
        String query = builder.craftQuery()
                .returnAllFields()
                .ofClassType("Users_demo\n")
                .getQuery();
        System.out.println(query);
        System.out.println("Step 4 complete\n");

        //5.)Execute Query
        if (box.executeThisQuery(query)){

        }else{
            System.out.println("Query failed\n");
        }
        System.out.println("Step 5 complete\n");

        //6.)Fetch Results
        System.out.println("Result Summary --------------------------");
        System.out.println(box.getQueryResultSummary());
        User newUser = (User) box.getResultInClass();
        System.out.println("Step 6 complete\n");



    }
}

