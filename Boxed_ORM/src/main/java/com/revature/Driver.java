package com.revature;


import com.revature.service.BlackBox;
import com.revature.service.QueryBuilder;
import com.revature.service.UpdateBuilder;
import com.revature.test_models.User;

import java.sql.Connection;
import java.util.Arrays;
import java.util.List;

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
        String query = builder.craftNewTransaction()
                .returnFields()
                .ofClassType("Users_demo")
                .getQuery();
        System.out.println(query);
        System.out.println("Step 4 complete\n");

        //5.)Execute Query
        if (box.runQuery(query)){

        }else{
            System.out.println("Query failed\n");
        }
        System.out.println("Step 5 complete\n");

        //6.)Fetch Results
        System.out.println("Result Summary --------------------------");
        System.out.println(box.getQueryResultSummary());
        User newUser = new User();
        System.out.println("Results in OBJ --------------------------");
        System.out.println(box.getResultInClass(newUser.getClass()).toString());
        System.out.println("Step 6 complete\n");

        //Get single field results
        //Get multi field results
        System.out.println("Result in List --------------------------");
        box.runQuery(query);
        List<String[]> result = box.getResultInList();
        for (String[] sa:result) {
            System.out.println(Arrays.toString(sa));
        }

        //INSERT
//        UpdateBuilder uBuilder = new UpdateBuilder();
//        String update = uBuilder.craftNewTransaction()
//                .setUpInsertTransaction("Users_demo", "first_name", "last_name", "email_address")
//                .addInsertValues("'Clark'", "'Giffin'", "'griffinCla@sonoma.edu'")
//                .addInsertValues("'Ravin'", "'Rayes'", "'reyesravin@sonoma.edu'")
//                .getInsertTransaction();
//
//        System.out.println(update);
//        box.runUpdate(update);

        //UPDATE

        //DELETE



    }
}

